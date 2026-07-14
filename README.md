# Login (Sesiones)

Gestiona sesiones de usuario: iniciar sesion, cerrar sesion, y consultar sesiones activas.

## Puerto

**8092** | DB: `login_usuario`

## Endpoints

| Metodo | Ruta | Descripcion |
|--------|------|-------------|
| POST | `/api/v1/sesiones/iniciar` | Iniciar sesion (body: `{"idUsuario": <Long>}`) |
| POST | `/api/v1/sesiones/{id}/cerrar` | Cerrar sesion |
| GET | `/api/v1/sesiones/usuario/{idUsuario}` | Sesiones activas de un usuario |
| GET | `/api/v1/sesiones` | Listar todas las sesiones |
| GET | `/api/v1/sesiones/{id}` | Obtener sesion por ID |
| DELETE | `/api/v1/sesiones/{id}` | Eliminar sesion |

## Reglas de negocio

- Un usuario no puede tener mas de una sesion activa simultaneamente. Intentar iniciar una segunda sesion retorna error 400.
- Estados posibles: `Activa`, `Cerrada`, `Expirada`.

## Ejecucion

```cmd
cd Login
.\mvnw.cmd spring-boot:run
```

## Entidades principales

- **Sesion**: `id`, `idUsuario`, `fechaInicio`, `fechaFin`, `estado`
