# Demo – API RESTful de Creación de Usuarios

Este proyecto expone una API RESTful para registrar y gestionar usuarios, con persistencia en memoria (H2), JPA, Spring Boot, y documentación Swagger.

---

## Prerrequisitos

- **Java 8+** instalado y configurado en `PATH`.
- **Maven** instalado.
- **Git** para clonar el repositorio.

---

## Cómo levantar la aplicación

1. **Clonar el repositorio**

2. **Configurar propiedades**
   Revisa `src/main/resources/application.properties` y confirma

3. **Construir y ejecutar**
   - Con Maven:
     ```bash
     mvn clean install
     mvn spring-boot:run
     ```

4. **Verificar que arranca**
   Deberías ver en consola un mensaje similar a:
   > Tomcat started on port 8080 (http)

   Y luego:
   > Started DemoApplication in X seconds.

---

## 📋 Endpoints disponibles

| Método | URL                   | Descripción                          | Código éxito | Cuerpo petición / respuesta    |
|--------|-----------------------|--------------------------------------|--------------|--------------------------------|
| POST   | `/api/users`          | Registro de un nuevo usuario         | 201 Created  | JSON con datos del nuevo usuario (ver ejemplo) |
| GET    | `/api/users/{id}`     | Obtiene un usuario por su ID         | 200 OK       | JSON con datos del usuario      |
| PUT    | `/api/users/{id}`     | Actualiza datos de un usuario        | 200 OK       | JSON con datos actualizados     |
| DELETE | `/api/users/{id}`     | Elimina un usuario                   | 204 No Content | —                              |

### Ejemplo: Registrar usuario

```bash
curl -X POST http://localhost:8080/api/users \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Juan Rodriguez",
    "email": "juan@rodriguez.org",
    "password": "Hunter123!",
    "phones": [
      {"number": "1234567", "citycode": "1", "contrycode": "57"}
    ]
  }'
```

**Respuesta (201)**:
```json
{
  "id": "550e8400-e29b-41d4-a716-446655440000",
  "created": "2025-04-16T17:00:00",
  "modified": "2025-04-16T17:00:00",
  "last_login": "2025-04-16T17:00:00",
  "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
  "isactive": true,
  "name": "Juan Rodriguez",
  "email": "juan@rodriguez.org",
  "phones": [
    {"number": "1234567", "citycode": "1", "contrycode": "57"}
  ]
}
```

### Errores comunes (400 Bad Request)

```json
{ "mensaje": "El correo ya registrado" }
```
```json
{ "mensaje": "El formato de correo es inválido" }
```

---

## 🔍 Consola H2

1. Abre tu navegador y ve a `http://localhost:8080/h2-console`.
2. **JDBC URL**: `jdbc:h2:mem:usuariosdb`
3. **Usuario**: `sa` (sin contraseña)
4. Haz clic en **Connect** y luego prueba:
   ```sql
   SELECT * FROM "USER";
   ```

---

## 📖 Documentación Swagger / OpenAPI

- Abre: `http://localhost:8080/swagger-ui.html`
- Ahí verás todos los endpoints, modelos y podrás probar la API desde la UI.

---

