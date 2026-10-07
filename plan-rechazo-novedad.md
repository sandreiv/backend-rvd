# Plan de implementación — Rechazo de novedades y nuevo modelo de detalle

## Contexto

Hoy el flujo de novedades permite **crear** (coordinador) y **aprobar** (desarrollo
académico). La aprobación solo cambia `NOVEDADCARGADOCENTE.NOCD_ESTADONOVEDAD` y
refresca `CARG_VALOR`. No existe el **rechazo**, y el detalle de novedad
(`DETALLENOVEDADCARGADOCENTE`) se escribía como un delta destructivo en sitio
(actualizar sobrescribía, eliminar borraba físicamente), lo que impide revertir.

### Cambio de modelo acordado

- `NOVEDADCARGADOCENTE` conserva `CADO_ID` (referencia a la carga) y gana `NOCD_ID`
  como PK subrogada.
- `DETALLENOVEDADCARGADOCENTE` reemplaza `CADO_ID` por `NOCD_ID` y agrega
  `DNCD_VIGENTE`.
- Cada novedad **genera su propio conjunto completo de detalles** (foto/historial)
  bajo su `NOCD_ID`; nunca se actualiza ni se borra en sitio.

### Regla de lectura

- **Detalle efectivo:** filas `DNCD_VIGENTE = '1'`; si no hay, `DETALLECARGADOCENTE`.
- **Detalle en revisión:** filas del `NOCD` con `NOCD_ESTADONOVEDAD = '0'`, para que
  desarrollo vea qué propone esa novedad.

### Estado de avance

- Entities `NovedadCargaDocenteEntity` y `DetalleNovedadCargaDocenteEntity`: HECHO.
- Fase 0 (BD): HECHO.
- Fase 1 (mapper y repositorios): HECHO.
- Fase 2 (creacion con foto por `NOCD_ID`): HECHO.
- Fase 3 (aprobacion): HECHO.
- Fase 4 (rechazo): HECHO.
- Fases 5 y 6: PENDIENTE.

---

## Fase 0 — Pendientes en base de datos

Agregaste las columnas, pero faltan piezas para que el modelo funcione:

```sql
-- 1) PK y generacion de NOCD_ID (hoy NOT NULL sin secuencia ni trigger)
CREATE SEQUENCE RVD.SEQ_NOVEDADCARGADOCENTE START WITH 1 INCREMENT BY 1 NOCACHE;
ALTER TABLE RVD.NOVEDADCARGADOCENTE ADD CONSTRAINT PK_NOVEDADCARGADOCENTE PRIMARY KEY (NOCD_ID);

-- 2) Indices (la tabla hoy no tiene ninguno)
CREATE INDEX RVD.IDX_NOCD_CADO ON RVD.NOVEDADCARGADOCENTE (CADO_ID);
CREATE INDEX RVD.IDX_DNCD_NOCD ON RVD.DETALLENOVEDADCARGADOCENTE (NOCD_ID);
CREATE INDEX RVD.IDX_DNCD_VIGENTE ON RVD.DETALLENOVEDADCARGADOCENTE (NOCD_ID, DNCD_VIGENTE);

-- 3) Integridad de las columnas nuevas
ALTER TABLE RVD.DETALLENOVEDADCARGADOCENTE MODIFY (NOCD_ID NOT NULL);
ALTER TABLE RVD.DETALLENOVEDADCARGADOCENTE MODIFY (DNCD_VIGENTE DEFAULT '0' NOT NULL);
ALTER TABLE RVD.DETALLENOVEDADCARGADOCENTE
    ADD CONSTRAINT CK_DNCD_VIGENTE CHECK (DNCD_VIGENTE IN ('0','1'));

-- 4) Tipo correcto del enlace a proyectos (hoy VARCHAR2 vs NUMBER)
ALTER TABLE RVD.RELACIONCARGAPROYECTO MODIFY (DNCD_ID NUMBER(30,0));
```

Nota sobre `NOCD_ID`: usar secuencia explicita (no trigger), porque el flujo necesita
conocer el `NOCD_ID` recien creado para colgarle los detalles.

Decidir ademas si `AUD_DETALLENOVEDADCARGADOCENTE` y
`PR_RVD_D_DETALLENOVEDADCARGADOCENTE` siguen existiendo (con el historial ya no se borra).

## Fase 1 — Mapper y repositorios de detalle (HECHO)

Realizado:

- `DetalleNovedadCargaDocenteMapper.toEntity(...)` recibe el `NOCD_ID` y asigna
  `vigente = "0"`.
- `DetalleNovedadCargaDocenteMapper.toEntityFromDto(...)` recibe el `NOCD_ID`
  explicito y asigna `vigente = "0"` (ya no reutiliza el `id` base).
- `insertDetails` y `fillDetalle` reciben el `NOCD_ID` y asignan `vigente = "0"`.
- `actualizarDetalleNovedad` recibe el `NOCD_ID` y lo pasa al mapper.
- Se corrigieron las 6 consultas nativas que referenciaban la columna eliminada
  `DNCD.CADO_ID`, resolviendo la carga con
  `JOIN NOVEDADCARGADOCENTE NOCD ON NOCD.NOCD_ID = DNCD.NOCD_ID`.
- Se elimino `NovedadCargaDocenteEntityId` (huerfano) y el repositorio paso a
  `JpaRepository<NovedadCargaDocenteEntity, Long>`.

Pendiente dentro de la fase (se hace junto a Fase 2/5, porque cambia la semantica de
los llamadores):

- Reemplazar `NovedadCargaDocenteRepository.findByIdCargaDocente(...)` (ambigua con
  varias novedades) por `findById(nocdId)`.
- Nota temporal: en `saveNoveltyProjectActivities` y `saveContractModalityProfessor`
  todavia se pasa `dto.idCargaDocente()` como si fuera el `NOCD_ID`; la Fase 2 lo
  reemplaza por el `NOCD_ID` generado.

## Fase 2 — Creación de novedades (foto completa por `NOCD_ID`) — HECHO

Realizado:

- Las **13 inserciones** de `NOVEDADCARGADOCENTE` ahora incluyen `NOCD_ID` tomado de
  `SEQ_NOVEDADCARGADOCENTE.NEXTVAL` (columna + valor en el `SELECT`).
- `NovedadCargaDocenteRepository.currentNovedadCargaDocenteId()` usa `CURRVAL` para
  recuperar ese `NOCD_ID` dentro de la misma transacción.
- `insertDetails` y `fillDetalle` cuelgan el detalle del `NOCD_ID` y marcan
  `DNCD_VIGENTE = '0'`.
- `saveNoveltyProjectActivities` construye la **foto completa** del detalle con
  `guardarFotoDetalleNovedad`: parte de la base efectiva, aplica
  nuevos/actualizados/eliminados y la inserta toda bajo el `NOCD_ID` nuevo. No borra
  ni actualiza filas históricas.
- `saveContractModalityProfessor` inserta su lista de detalles bajo el `NOCD_ID`.
- La base efectiva se resuelve con la CTE `findByIdCargaDocente`, que ahora filtra a
  la última novedad no rechazada **con detalle** (evita mezclar historial).
- `resolveIdCoordinacionByNovedadCargaDocente` y
  `validatePreassignmentWriteAllowedByNovedadCargaDocente` ya no usan
  `findByIdCargaDocente` (era ambiguo con varias novedades por carga); resuelven
  desde `CARGADOCENTE`.
- La modalidad usada en la validacion de horas por programa se resuelve con
  `resolveModalidadEfectiva`: ultima novedad no rechazada, con respaldo en
  `CARGADOCENTE`.

Pendiente / a limpiar:

- `actualizarDetalleNovedad` y `deleteDetalleNovedad` quedaron sin uso (ya no aplican
  al modelo de foto). Se pueden eliminar junto con
  `validatePreassignmentWriteAllowedByNovedadDetalle` y
  `PR_RVD_D_DETALLENOVEDADCARGADOCENTE`.
- `DetalleNovedadCargaDocenteRepository.findEffectiveByIdCargaDocente` quedó
  disponible para Fase 5 (aún sin uso).
- Nota de transacción: `CURRVAL` exige que la inserción y la lectura ocurran en la
  misma sesión; por eso los flujos siguen siendo `@Transactional`.

## Fase 3 — Aprobación — HECHO

Realizado en `approveProfessorNovelty`:

- Se localiza la novedad en revision (`NOCD_ESTADONOVEDAD='0'`) y se obtiene su
  `NOCD_ID`.
- `updateEstadoNovedadInReview` se reemplazo por `approveNoveltyById`, que aprueba
  por el `NOCD_ID` exacto (antes usaba `MAX(NOCD_FECHACAMBIO)`, que puede empatar al
  segundo y aprobar la fila equivocada).
- `clearVigenteByIdCargaDocente` sigue marcando una sola novedad vigente por carga.
- Se activa la foto de detalle de la novedad aprobada **solo si tiene detalle**:

```sql
UPDATE RVD.DETALLENOVEDADCARGADOCENTE SET DNCD_VIGENTE='0'
 WHERE NOCD_ID IN (SELECT NOCD_ID FROM RVD.NOVEDADCARGADOCENTE WHERE CADO_ID = :cadoId);
UPDATE RVD.DETALLENOVEDADCARGADOCENTE SET DNCD_VIGENTE='1'
 WHERE NOCD_ID = :nocdId;
```

Si la novedad no tiene detalle (p. ej. `change-professor`), no se toca
`DNCD_VIGENTE`, para no borrar el conjunto efectivo anterior.

- Se mantiene `refreshCargValor` y el manejo de `NOCD_ESTADOELIMINADO`
  (`delete-professor`).

Nota: las lecturas aun resuelven por "ultima novedad no rechazada con detalle"; el
uso explicito de `DNCD_VIGENTE='1'` para el detalle efectivo (y la novedad en estado
`'0'` para el detalle en revision) se completa en Fase 5.

## Fase 4 — Rechazo — HECHO

Realizado:

- Endpoint `PUT /reject-professor-novelty/{idCargaDocente}` con `ObservacionDecanoDTO`
  (idPersonaGeneral + observacion).
- `NovedadCargaDocenteRepository.rejectNoveltyById(...)`: pasa
  `NOCD_ESTADONOVEDAD` a `'2'` y `NOCD_VIGENTE` a `'0'` por el `NOCD_ID` exacto.
- `rejectProfessorNovelty`:
  - Valida que exista novedad en revision; si no, `409`.
  - No toca `DNCD`, ni `CARG_VALOR`, ni `refreshCargValor`. La foto de la novedad
    rechazada queda en `DNCD_VIGENTE='0'` y la vigente anterior permanece.
  - Guarda el motivo en `RVD.OBSERVACIONES` (misma fuente que usa el resumen).
  - Si la novedad es `add-professor`, elimina la carga creada y sus detalles
    (`RELACIONCARGAPROYECTO`, `DETALLECARGADOCENTE`, `DETALLENOVEDADCARGADOCENTE` y
    `CARGADOCENTE`) y refresca los totales de la preasignacion.
- Idempotencia: sin novedad en `'0'` responde `409`; aprobar una rechazada falla.
- Tras rechazar, se puede crear una nueva novedad.

Correccion de BD asociada: `PR_RVD_D_DETALLENOVEDADCARGADOCENTE` habia quedado
`INVALID` porque aun referenciaba la columna `CADO_ID` retirada de
`DETALLENOVEDADCARGADOCENTE`. Se recreo para usar `NOCD_ID` (la tabla
`AUD_DETALLENOVEDADCARGADOCENTE` ya tenia la columna `NOCD_ID`).

## Fase 5 — Lectura y visualización

- Efectivo: `DNCD_VIGENTE='1'`; si no, `DECD`.
- En revision: filas del `NOCD` en estado `'0'`.
- Consultas a cambiar:
  - `DetalleNovedadCargaDocenteRepository.findResumenByIdCargaDocente`.
  - `DetalleNovedadCargaDocenteRepository.findByIdCargaDocente` (listado con proyectos).
  - `findHorasByProgramaAndCargaDocente` y validaciones de horas.
  - `NovedadCargaDocenteRepository` `tieneActividades` (EXISTS).
  - `resolveIdCoordinacionByNovedadCargaDocente` y
    `validatePreassignmentWriteAllowedByNovedadDetalle` (usar `NOCD_ID`).
- Endpoints:
  - `listNoveltyDetailProfessorPreload`: parametro de modo (`vigente` / `revision`).
  - `getProfessorNoveltySummary`: efectivo vs pendiente.
- Decision pendiente: si `/professor-load-summary`, `/activity-hours`,
  `/cost-centers`, `/activities-hours` y `/total-preload` deben pasar a resolver con
  `DNCD_VIGENTE='1' -> DECD` (hoy leen solo `DECD`).

Nota: - `DetalleNovedadCargaDocenteRepository.findEffectiveByIdCargaDocente` no se utiliza
para hallar los detalles efectivos, eso se realiza mediante `findResumenByIdNovedadCargaDocente`
dentro del mismo repositorio.
- `DetalleNovedadCargaDocenteRepository.findByIdCargaDocente` pasa a ser
`DetalleNovedadCargaDocenteRepository.findEffectiveDetailsByIdCargaDocente` para usar en
las siguientes partes de la aplicación



## Fase 6 — Pruebas

- Crear novedad y ver "en revision" sin afectar el efectivo.
- Aprobar: el detalle efectivo cambia; valor y `CARG_VALOR` actualizados.
- Rechazar: el efectivo anterior permanece; la rechazada no se filtra.
- Cadena A a B a C con aprobaciones y rechazos alternados.
- Aprobar una novedad sin detalles no borra el conjunto efectivo.
- `add-professor` rechazado elimina la carga creada.
- Restriccion de horas: no cuenta detalles de novedades rechazadas.
