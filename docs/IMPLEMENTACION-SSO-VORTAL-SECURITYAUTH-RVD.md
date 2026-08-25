# Seguridad RVD: SSO Vortal → SecurityAuth → roles y funcionalidades

Documento de funcionamiento del sistema de seguridad de **RVD Nuevo**.
RVD **solo** se ingresa desde Vortal (sin login local).

> Contrato detallado backend ↔ Angular: [`SEGURIDAD-BACKEND-FRONTEND-RVD.md`](./SEGURIDAD-BACKEND-FRONTEND-RVD.md).

**Estado validado en pruebas:** `apli_id = 55100` (RVD - UDEC), usuario demo `pmduran` / rol `Coordinador`.

> En `application.yml` el `application-id` de pruebas es `55100`. Confirmar el ID definitivo de producción antes de desplegar.

---

## 1. Idea en una frase

> El **JWT** solo trae **identidad + roles por aplicación**.  
> El **menú y los botones** salen del catálogo de **funcionalidades en Vortal** (vía SecurityAuth).  
> El **backend RVD** autoriza de verdad cada API con authorities `METHOD:URL`; Angular solo oculta UI.

---

## 2. Piezas del ecosistema

| Pieza | Responsabilidad |
| ----- | --------------- |
| **Vortal** | Login, catálogo (`aplicacion`, `rol`, `funcionalidad`, `rolaplicacionfuncionalidad`, `usuariorol`), entrega del JWT al abrir RVD |
| **SecurityAuth** | Emite JWT (RS256); expone `/funcionalidad/arbol-roles` y `/funcionalidad/rol-aplicacion` |
| **Backend RVD** | Resource Server: valida JWT, filtra app configurada, arma authorities, responde 401/403 |
| **Angular RVD** | Bootstrap de sesión, arma menú, arma flags de botones, llama APIs con Bearer |

```text
Usuario
  → Vortal (login)
  → SecurityAuth (JWT con aplicaciones[{id, roles}])
  → Angular RVD (#access_token)
       ├─ POST /rvd/api/auth/bootstrap          → sesión SPA
       ├─ GET  SecurityAuth /arbol-roles        → menú + hijas (botones)
       └─ APIs /rvd/configuration/...           → 200 | 401 | 403
```

El JWT **no** lleva la lista de botones; solo app + roles.

---

## 3. Datos Vortal (fuente de verdad)

### 3.1 Aplicación y tablas

| Tabla | Uso |
| ----- | --- |
| `aplicacion` | App RVD (`55100` en pruebas) |
| `rol` | Roles de la app (Coordinador, Decano, …) |
| `funcionalidad` | Menús (padres) + acciones (hijas): `func_codigo`, `func_urlrecurso`, `func_nombrefuncion` |
| `rolaplicacionfuncionalidad` | Qué ve/puede cada rol |
| `usuariorol` | Usuario ↔ rol |

### 3.2 Árbol funcionalidad (estado actual de pruebas)

| func_id | padre | codigo | nombre | url_recurso | nombrefuncion |
| ------- | ----- | ------ | ------ | ----------- | ------------- |
| 78545 | — | 01 | Convocatoria precarga | `/configuration/preload-call/**` | (vacío → módulo) |
| 78546 | — | 02 | Precarga Docente | `/configuration/coordination/**` | (vacío → módulo) |
| 78549 | 78546 | 02_01 | Listar docentes | `/configuration/coordination/list-professors-modality` | `LISTAR` |
| 78550 | 78546 | 02_02 | Agregar docente | `/configuration/coordination/add-professor` | `GUARDAR` |
| 78551 | 78546 | 02_03 | Actualizar docente | `/configuration/coordination/update-professor` | `ACTUALIZAR` |
| 78552 | 78546 | 02_04 | Eliminar docente | `/configuration/coordination/delete-professor` | `ELIMINAR` |
| 78553 | 78546 | 02_05 | Guardar detalle | `/configuration/coordination/save-detail-professor-preload` | `GUARDAR` |
| 78554 | 78546 | 02_06 | Aprobar docente | `/configuration/coordination/approve-professor-preassignment` | `ACTUALIZAR` |
| 78547 | — | 03 | Administración | `/configuration/administration/**` | (vacío → módulo) |

**Importante:** para que `/arbol-roles` arme el menú, el rol debe tener asignado también el **padre**. Si solo se asignan hijas, el árbol puede devolver `[]`.

---

## 4. Flujo completo

```text
Vortal login → SecurityAuth emite JWT
  claims: { sub, idPersona, aplicaciones: [{ id: 55100, roles: ["Coordinador"] }] }
        │
        ▼
Vortal abre RVD → Angular lee #access_token=<JWT>
        │
        ▼
POST /rvd/api/auth/bootstrap { "accessToken": "<JWT>" }
  → valida JWKS + iss
  → exige rol en application-id configurado
  → responde sesión SPA (username, idPersona, roles, idAplicacion)
  → NO genera otro JWT: reutiliza el de SecurityAuth
        │
        ▼
GET {SecurityAuth}/funcionalidad/arbol-roles?roles=Coordinador&idAplicacion=55100
  Authorization: Bearer <JWT>
  → nodos padre = menú
  → funHijas = flags de botones
        │
        ▼
Cada API RVD con Authorization: Bearer <mismo JWT>
  → Resource Server valida firma
  → ExternalJwtUserResolver filtra roles de la app
  → SecurityAuthPermissionsService consulta /rol-aplicacion (caché 1h)
  → arma authorities METHOD:URL
  → FuncionalidadAuthorizationManager hace match Ant
  → 200 | 401 | 403
```

---

## 5. Contrato de permisos: módulo vs acciones

### 5.1 Modo A — Módulo completo (padre)

En Vortal:

```text
func_urlrecurso     = /configuration/coordination/**
func_nombrefuncion  = (vacío)
```

Authority resultante:

```text
*:/configuration/coordination/**
```

→ el rol ejecuta **todos** los endpoints bajo ese prefijo (cualquier verbo HTTP).

### 5.2 Modo B — Solo hijas (granular)

| nombrefuncion | HTTP | Ejemplo URL → authority |
| ------------- | ---- | ----------------------- |
| `LISTAR` / `LIST` | GET | `…/list-professors-modality` → `GET:…/list-professors-modality/**` |
| `GUARDAR` | POST | `…/add-professor` → `POST:…/add-professor/**` |
| `ACTUALIZAR` | PUT | `…/update-professor` → `PUT:…/update-professor/**` |
| `ELIMINAR` | DELETE | `…/delete-professor` → `DELETE:…/delete-professor/**` |

A las URLs de acción se les añade `/**` automáticamente para path variables (`/{id}`).

Los controllers de coordinación usan **PUT** (no PATCH) en actualizar/aprobar.
Hay compatibilidad PUT↔PATCH en el `AuthorizationManager` por legado.

### 5.3 Unión

Si el rol tiene **padre `/**` y también hijas**:

- En **API** gana el comodín (puede todo el módulo).
- Las **hijas** siguen sirviendo al front para mostrar/ocultar botones.

### 5.4 Errores típicos en Vortal

| Situación | Efecto |
| --------- | ------ |
| “Aprobar” como `GUARDAR` pero endpoint es `PUT` | **403** → usar `ACTUALIZAR` |
| Solo hijas asignadas, sin padre | Árbol vacío / menú incompleto |
| Padre sin `/**` y sin verbo | Puede no cubrir subrutas; preferir `…/**` en menús |

---

## 6. Backend RVD (componentes)

| Componente | Rol |
| ---------- | --- |
| `SecurityAuthProperties` | `jwk-set-uri`, `issuer`, `base-url`, `application-id`, `enforce-funcionalidad` |
| `SecurityAuthTokenValidator` | Decoder JWKS / PEM + validación `iss` |
| `SecurityAuthJwtAuthenticationConverter` | Convierte JWT → `Authentication` |
| `ExternalJwtUserResolver` | Principal desde claims (sin BD local de login); carga authorities |
| `SecurityAuthPermissionsService` | Llama `/funcionalidad/rol-aplicacion` + caché Caffeine 1h; normaliza `METHOD:URL` |
| `FuncionalidadAuthorizationManager` | Match Ant `METHOD:URL`; rechaza anónimo → 401 |
| `POST /api/auth/bootstrap` | Entrada SSO pública (`SecurityAuthBootstrapService`) |
| `JwtAuthEntryPoint` / `JwtAccessDeniedHandler` | 401 / 403 |
| `SecurityUtils` | Lee `idPersona` del `SecurityContext` |

### 6.1 Rutas públicas

```text
/api/auth/bootstrap
/swagger-ui/**
/api-docs/**
```

El resto pasa por `FuncionalidadAuthorizationManager`.

### 6.2 Configuración (`application.yml`)

```yaml
rvd:
  security:
    security-auth:
      jwk-set-uri: http://127.0.0.1:8171/oauth2/jwks
      issuer: http://127.0.0.1:8171
      base-url: http://127.0.0.1:8171
      application-id: 55100
      enforce-funcionalidad: true
```

| Flag | Comportamiento |
| ---- | -------------- |
| `enforce-funcionalidad: false` | Rutas **fuera** del catálogo del usuario pasan si el JWT es válido; ruta del catálogo con verbo incorrecto → **403** |
| `enforce-funcionalidad: true` | Solo `METHOD:URL` autorizados; sin match → **403** |
| Sin Bearer / anónimo | Siempre **401** (aunque `AnonymousAuthenticationToken.isAuthenticated()` sea true) |

Context path: `/rvd` → bootstrap real: `POST /rvd/api/auth/bootstrap`.

### 6.3 Authorities de ejemplo

**Módulo completo (padre `…/coordination/**`):**

```text
*:/configuration/coordination/**
```

**Granular (solo hijas):**

```text
GET:/configuration/coordination/list-professors-modality/**
POST:/configuration/coordination/add-professor/**
PUT:/configuration/coordination/update-professor/**
DELETE:/configuration/coordination/delete-professor/**
POST:/configuration/coordination/save-detail-professor-preload/**
PUT:/configuration/coordination/approve-professor-preassignment/**
```

### 6.4 Qué NO hace el backend

- No arma el menú Angular.
- No decide qué botón mostrar (UX).
- No confía solo en “soy Coordinador”: usa **funcionalidad → URL/método**.

---

## 7. Frontend Angular (contrato)

### 7.1 Sesión / token

1. Leer `access_token` (hash Vortal).
2. `POST {apiRvd}/api/auth/bootstrap` con `{ "accessToken": "..." }`.
3. Guardar: `accessToken`, `roles`, `idPersona`, `idAplicacion`.
4. Interceptor: `Authorization: Bearer` hacia RVD **y** SecurityAuth.
5. Logout: limpiar token, árbol y flags.

Respuesta bootstrap (ejemplo):

```json
{
  "accessToken": "...",
  "type": "Bearer",
  "username": "pmduran",
  "idPersona": "231326",
  "roles": ["Coordinador"],
  "usuario": {
    "username": "pmduran",
    "idPersona": 231326,
    "roles": ["Coordinador"],
    "idAplicacion": 55100
  }
}
```

| HTTP | Acción FE |
| ---- | --------- |
| **401** | Re-bootstrap o volver a Vortal |
| **403** | Toast “sin permiso”; no necesariamente logout |

### 7.2 Menú

```http
GET {securityAuth}/funcionalidad/arbol-roles?roles=Coordinador&idAplicacion=55100
Authorization: Bearer <JWT>
```

- Roles = los del bootstrap (no hardcodear).
- Sidebar: **solo padres** (navegación).
- Strip `/**` para `routerLink`:
  `/configuration/coordination/**` → `/configuration/coordination`
- **No** pintar hijas `02_01`… en el menú.

### 7.3 Botones / permisos por funcionalidad

Al entrar al módulo, usar `funHijas` del nodo padre:

```ts
const codes = new Set(nodo.funHijas.map(h => h.codigo));

permissions = {
  list: codes.has('02_01'),
  add: codes.has('02_02'),
  update: codes.has('02_03'),
  delete: codes.has('02_04'),
  saveDetail: codes.has('02_05'),
  approve: codes.has('02_06'),
};
```

Preferible mapear por `codigo` o `urlRecurso` (puede haber varios `GUARDAR`/`ACTUALIZAR`).

```html
<button *ngIf="permissions.add">Agregar</button>
<button *ngIf="permissions.approve">Aprobar</button>
```

**No** usar `if (rol === 'Coordinador')`.

### 7.4 Checklist FE

- [ ] Environments: `apiRvd`, `apiSecurityAuth`, `applicationId`
- [ ] Bootstrap + interceptor Bearer
- [ ] Servicio menú (`arbol-roles`)
- [ ] Sidebar solo padres; strip `/**`
- [ ] Flags por módulo desde `funHijas`
- [ ] Route guard si la ruta no está en el menú
- [ ] Manejo 401 / 403
- [ ] Limpieza en logout

---

## 8. Matriz rápida (ejemplo Coordinador)

| Recurso | ¿En menú? | ¿Botón? | API (`enforce: true`) |
| ------- | --------- | ------- | --------------------- |
| Precarga Docente | Sí (padre) | — | Cubierto si padre `…/**` |
| Listar docentes | — | `02_01` | GET list-professors-modality → 200 |
| Agregar docente | — | `02_02` | POST add-professor → 200 |
| Convocatoria / preload-call | Solo si el rol tiene el padre | — | Sin asignación → **403** |

---

## 9. Prueba rápida (Postman / curl)

1. Obtener JWT (login SecurityAuth o token de Vortal).
2. `POST {RVD}/api/auth/bootstrap` con `{ "accessToken": "..." }`.
3. `GET {SecurityAuth}/funcionalidad/arbol-roles?roles=Coordinador&idAplicacion=55100` con Bearer.
4. `GET {RVD}/configuration/coordination/list-professors-modality?...` con Bearer → **200** si tiene permiso.
5. Endpoint de un módulo no asignado → **403**.
6. Sin header Authorization → **401**.

Tras cambios en Vortal (`rolaplicacionfuncionalidad`), reiniciar RVD o esperar expiración de caché (1 h) / pedir token nuevo según el caso.

---

## 10. Resumen

| Pregunta | Respuesta |
| -------- | --------- |
| ¿Quién autentica? | SecurityAuth (JWT); RVD valida firma + `iss` |
| ¿Quién dice el rol? | Claim `aplicaciones` filtrado a `application-id` |
| ¿Quién arma el menú? | Angular con `/arbol-roles` |
| ¿Quién decide botones? | Angular con `funHijas` (UX) |
| ¿Quién autoriza de verdad? | Backend RVD (`METHOD:URL` + `enforce-funcionalidad`) |
| ¿Dónde se administra? | Vortal (roles ↔ funcionalidades) |

---

*Adopción SecurityAuth / roles por funcionalidad en RVD — actualizado agosto 2026.*
