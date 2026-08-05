# Implementación SSO: Vortal → SecurityAuth → RVD

Documento de adopción del modelo SecurityAuth en **RVD Nuevo** (`application-id = 90001`).
RVD **solo** se ingresa desde Vortal (sin login local).

> Referencia BTAA: `btaa/docs/IMPLEMENTACION-SSO-VORTAL-SECURITYAUTH-BTAA.md`
> y el paquete `IMPLEMENTACION-SSO-VORTAL-SECURITYAUTH-BTAA.md`.

---

## 1. Objetivo

1. Login único en Vortal → JWT de SecurityAuth.
2. Al abrir RVD (`apli_id = 90001`), Vortal entrega el token al frontend.
3. Angular llama `POST /rvd/api/auth/bootstrap`.
4. Cada API lleva `Authorization: Bearer <JWT>`; RVD valida firma (JWKS + `iss`)
   y autoriza con authorities `METHOD:URL` resueltas desde roles + funcionalidad.

---

## 2. Datos Vortal (contrato)

| Concepto | Valor |
|----------|--------|
| Aplicación | `90001` — RVD Nuevo — `https://rvd.unipamplona.edu.co` |
| Roles | `901 COORDINADOR`, `902 DESARROLLO_ACADEMICO`, `903 DECANO` |

### Árbol funcionalidad (preasignación)

| func_id | padre | codigo | nombre | url_recurso | tipo |
|---------|-------|--------|--------|-------------|------|
| 900100 | — | 01 | Preasignación docente | `/coordination/preload` | menú |
| 900101 | 900100 | 01_01 | Listar | `/configuration/coordination/list-professors-modality` | LISTAR → GET |
| 900102 | 900100 | 01_02 | Guardar | `/configuration/coordination/add-professor` | GUARDAR → POST |
| 900103 | 900100 | 01_03 | Actualizar | `/configuration/coordination/update-professor/**` | ACTUALIZAR → PATCH |
| 900104 | 900100 | 01_04 | Eliminar | `/configuration/coordination/delete-professor/**` | ELIMINAR → DELETE |
| 900105 | 900100 | 01_05 | Guardar | `/configuration/coordination/save-detail-professor-preload` | GUARDAR → POST |
| 900106 | 900100 | 01_06 | Actualizar | `/configuration/coordination/approve-professor-preassignment/**` | ACTUALIZAR → PATCH |

En botones (`tipo = 1`), el nombre/nombrefuncion `LISTAR|GUARDAR|ACTUALIZAR|ELIMINAR`
se mapea al verbo HTTP (igual que SecurityAuth `PermisoRolCacheService`).

---

## 3. Flujo

```text
Vortal login → SecurityAuth POST /auth/login
  JWT { sub, idPersona, aplicaciones: [{ id: 90001, roles: ["COORDINADOR"] }] }
        │
        ▼
Vortal abre RVD → Angular #access_token=<JWT>
        │
        ▼
POST /rvd/api/auth/bootstrap { accessToken }
  → valida JWKS + iss + roles app 90001
  → sesión SPA (username, idPersona, roles)
        │
        ▼
GET SecurityAuth /funcionalidad/arbol-roles?roles=COORDINADOR&idAplicacion=90001
  → menú + hijas (flags de botones en frontend)
        │
        ▼
API RVD con Bearer
  → RS resuelve permisos (caché) → METHOD:URL
  → 200 | 401 | 403
```

El JWT **no** lleva la lista de botones; solo app + roles.

---

## 4. Backend RVD (piezas)

| Componente | Rol |
|------------|-----|
| `SecurityAuthProperties` | `jwk-set-uri`, `issuer`, `base-url`, `application-id: 90001`, `enforce-funcionalidad` |
| `SecurityAuthTokenValidator` | Decoder JWKS / PEM + validación `iss` |
| `ExternalJwtUserResolver` | Principal desde claims (sin BD local de login) |
| `SecurityAuthPermissionsService` | Llama `/funcionalidad/rol-aplicacion` + caché Caffeine 1h |
| `FuncionalidadAuthorizationManager` | Match `METHOD:URL` (Ant `/**`) |
| `POST /api/auth/bootstrap` | Entrada SSO |
| `SecurityUtils` | `idPersona` del SecurityContext |

### Configuración (`application.yml`)

```yaml
rvd:
  security:
    security-auth:
      jwk-set-uri: http://127.0.0.1:8171/oauth2/jwks
      issuer: http://127.0.0.1:8171
      base-url: http://127.0.0.1:8171
      application-id: 90001
      enforce-funcionalidad: false
```

- `enforce-funcionalidad: false` (migración): rutas **fuera** del catálogo del
  usuario pasan con JWT válido; rutas del catálogo con verbo incorrecto → **403**.
- `true` (producción estricto): solo METHOD:URL autorizados.

### Authorities de ejemplo (COORDINADOR)

```text
GET:/configuration/coordination/list-professors-modality
POST:/configuration/coordination/add-professor
PATCH:/configuration/coordination/update-professor/**
DELETE:/configuration/coordination/delete-professor/**
POST:/configuration/coordination/save-detail-professor-preload
PATCH:/configuration/coordination/approve-professor-preassignment/**
```

---

## 5. Frontend (resumen)

1. Leer `#access_token` → `POST /rvd/api/auth/bootstrap`.
2. Guardar token y llamar APIs con Bearer.
3. Menú: `GET /funcionalidad/arbol-roles` en SecurityAuth.
4. Botones: hijas del nodo (`01_02`, …) o `urlRecurso` en `sessionStorage`.
5. No preguntar “¿eres COORDINADOR?”; preguntar “¿tengo `01_02`?”.

---

## 6. Vortal (pendiente de despliegue)

Generalizar el caso BTAA (`55000`) en `hms_ace_apl_tmp.jsp` para `90001`
redirigiendo al front RVD con `#access_token=<JWT>`.

---

## 7. Prueba rápida (Postman)

1. `POST {SecurityAuth}/auth/login` → accessToken.
2. `POST {RVD}/api/auth/bootstrap` con `{ "accessToken": "..." }`.
3. `GET {RVD}/configuration/coordination/list-professors-modality?...`
   con `Authorization: Bearer ...`.
4. Con rol sin `POST:.../add-professor` → `POST .../add-professor` → **403**
   (si el permiso está en el catálogo del token / `enforce` activo).

---

*Adopción SecurityAuth en RVD — agosto 2026.*
