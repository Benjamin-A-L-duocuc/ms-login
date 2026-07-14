# Login — Gestor de Sesiones

## Que es

No es un servicio de autenticacion (no valida contrasenas). Es un **rastreador de sesiones**: registra que un usuario esta "conectado", y permite al resto del sistema preguntar: "esta usuario X activo ahora?". Piensa en el como el que lleva la lista de personas dentro de la tienda en este momento.

## Concepto clave: una sola sesion activa

La regla principal es que **un usuario no puede tener mas de una sesion activa al mismo tiempo**. Si alguien intenta iniciar sesion cuando ya tiene una abierta, el sistema rechaza la peticion con un error. Para conectarse desde otro dispositivo, primero hay que cerrar la sesion anterior.

## Ciclo de vida

```
(iniciar sesion)  -->  Activa  -->  (cerrar sesion)  -->  Cerrada
                           |
                           +---> Expirada  [planeado, aun no implementado]
```

El estado `Expirada` existe en el codigo como placeholder para un futuro sistema de timeout automatico, pero actualmente nunca se usa — las sesiones se mantienen `Activa` indefinidamente hasta que se cierren manualmente.

## Como se usa en el sistema

El servicio **TiendaWeb** (tienda en linea) verifica la sesion del usuario en Practicamente cada operacion: agregar al carrito, comprar, ver perfil, etc. Sin una sesion valida, no se puede hacer nada en la tienda.

## Ejecutar

```cmd
cd Login
.\mvnw.cmd spring-boot:run
```

Puerto: **8092** | DB: `login_usuario`

## Endpoints

| Metodo | Ruta | Que hace |
|--------|------|----------|
| POST | `/api/v1/sesiones/iniciar` | Iniciar sesion (body: `{"idUsuario": 1}`) |
| POST | `/api/v1/sesiones/{id}/cerrar` | Cerrar sesion |
| GET | `/api/v1/sesiones/usuario/{id}` | Verificar si un usuario tiene sesion activa |
| GET | `/api/v1/sesiones` | Listar todas las sesiones |
| GET | `/api/v1/sesiones/{id}` | Ver una sesion por ID |
| DELETE | `/api/v1/sesiones/{id}` | Eliminar sesion (uso administrativo) |
