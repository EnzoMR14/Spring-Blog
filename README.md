# [Spring Blog]

Aplicación web hecha con Spring Boot y Thymeleaf para la actividad "Tu App vitaminada".

## Funcionalidades

- Diseño propio con Bootstrap 5
- Formulario de contacto con validaciones (los mensajes se guardan en la base de datos)
- Gestión de usuarios: registro, login y logout con Spring Security y contraseñas BCrypt
- Zona de administradores para gestionar imágenes (subir, borrar, elegir las de portada)
- Portada con carrusel administrable desde el panel
- Panel de administración de mensajes de contacto

## Tecnologías

Java 21, Spring Boot, Spring Data JPA, Spring Security, Thymeleaf, MySQL, Bootstrap 5

## Cómo ejecutarlo

1. Tener MySQL en marcha y crear la base de datos `springblog`.
2. Configurar usuario y contraseña de MySQL en `src/main/resources/application.properties`.
3. Arrancar:
   ```bash
   ./mvnw spring-boot:run
   ```
4. Abrir http://localhost:8081

## Crear un administrador

1. Registrarse desde `/register`.
2. En MySQL:
   ```sql
   UPDATE users SET role = 'ROLE_ADMIN' WHERE email = 'tu@email.com';
   ```
3. Cerrar sesión y volver a entrar.

## Rutas principales

| Ruta                      | Acceso               |
| ------------------------- | -------------------- |
| `/`, `/about`, `/contact` | Público              |
| `/login`, `/register`     | Público              |
| `/admin/images`           | Solo administradores |
| `/admin/contacts`         | Solo administradores |

## Autor

[Tu nombre]
