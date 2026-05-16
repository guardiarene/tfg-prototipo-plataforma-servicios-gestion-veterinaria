# PSyGCV — Plataforma de servicios y gestión de clínicas veterinarias

![Java](https://img.shields.io/badge/Java-25-orange?style=for-the-badge&logo=openjdk)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-4.0.5-brightgreen?style=for-the-badge&logo=springboot)
![MySQL](https://img.shields.io/badge/MySQL-8.0-blue?style=for-the-badge&logo=mysql)
![Thymeleaf](https://img.shields.io/badge/Thymeleaf%20%2B%20Bootstrap-5-005F0F?style=for-the-badge&logo=thymeleaf)

Prototipo desarrollado como Trabajo de Fin de Grado (TFG). PSyGCV es una aplicación web para la gestión integral de
clínicas veterinarias: administración de mascotas, historias clínicas unificadas, citas médicas y personal, con acceso
diferenciado por rol.

---

## Características principales

### Autenticación y roles

El sistema define cuatro roles con acceso y vistas diferenciados:

| Rol                       | Descripción                                                              |
|---------------------------|--------------------------------------------------------------------------|
| Administrador del sistema | Gestión global de usuarios y configuración de la plataforma.             |
| Veterinario               | Acceso a historias clínicas, visitas y gestión de su clínica y personal. |
| Recepcionista             | Programación, reprogramación y seguimiento del estado de las citas.      |
| Cliente                   | Consulta de clínicas disponibles, mascotas e historial de citas propias. |

Tras el inicio de sesión, cada usuario es redirigido automáticamente al dashboard correspondiente a su rol.

### Módulos implementados

- **Clínicas veterinarias:** registro, búsqueda, gestión de personal y catálogo de servicios médicos.
- **Mascotas:** alta, edición y eliminación con atributos de especie, raza, sexo, estado reproductivo y temperamento.
- **Historia clínica:** creación y edición de historias clínicas con registro de visitas, anamnesis, examen clínico,
  diagnósticos, tratamientos y vacunas.
- **Citas:** reserva por el cliente y programación por la recepcionista, con estados pendiente, confirmada y cancelada,
  y soporte para reprogramación.
- **Estadísticas y reportes:** paneles con datos de diagnósticos, tratamientos y actividad de citas para veterinarios y
  recepcionistas.
- **Administración de usuarios:** creación, edición y desactivación de cuentas desde el panel de administración.

---

## Tecnologías

### Backend

| Tecnología                  | Versión | Uso                                          |
|-----------------------------|---------|----------------------------------------------|
| Java                        | 25      | Lenguaje principal                           |
| Spring Boot                 | 4.0.5   | Framework de aplicación                      |
| Spring Security             | 6       | Autenticación y autorización basada en roles |
| Spring Data JPA + Hibernate | —       | Persistencia y ORM                           |
| Jakarta Bean Validation     | —       | Validación de datos de entrada               |
| Lombok                      | 1.18.44 | Reducción de código boilerplate              |

### Frontend

| Tecnología  | Uso                                       |
|-------------|-------------------------------------------|
| Thymeleaf   | Motor de plantillas del lado del servidor |
| Bootstrap 5 | Estilos y componentes de interfaz         |
| JavaScript  | Confirmaciones e interactividad básica    |

### Infraestructura

| Herramienta    | Uso                                                  |
|----------------|------------------------------------------------------|
| MySQL 8.0      | Base de datos en entornos de desarrollo y producción |
| H2             | Base de datos en memoria para el entorno de tests    |
| Maven          | Gestión de dependencias y ciclo de build             |
| Checkstyle     | Verificación de estilo (Google Java Style Guide)     |
| GitHub Actions | Pipeline de integración continua (CI)                |

---

## Arquitectura del sistema

La aplicación sigue una arquitectura en capas con separación estricta de responsabilidades:

```
controller   →   Recibe peticiones HTTP, delega al servicio y devuelve la vista
service      →   Toda la lógica de negocio y gestión de transacciones
repository   →   Acceso a datos mediante interfaces de Spring Data JPA
entity       →   Modelo de dominio y entidades JPA
config       →   Seguridad, auditoría y configuración de la aplicación
dto / mapper →   (En proceso) Desacoplamiento entre la API y el modelo interno
```

Los perfiles de entorno (`dev`, `test`, `prod`) permiten aislar la configuración de base de datos y logging sin
modificar el código fuente.

---

## Instalación y configuración

### Requisitos previos

- Java JDK 25
- Apache Maven 3.9+
- MySQL 8.0

### Pasos

1. **Clonar el repositorio:**
   ```bash
   git clone https://github.com/guardiarene/tfg-prototipo-plataforma-servicios-gestion-veterinaria.git
   cd tfg-prototipo-plataforma-servicios-gestion-veterinaria
   ```

2. **Crear la base de datos:**
   ```sql
   CREATE DATABASE psygcs_dev;
   ```

3. **Configurar credenciales** en `src/main/resources/application-dev.yaml`:
   ```yaml
   spring:
     datasource:
       url: jdbc:mysql://localhost:3306/psygcs_dev
       username: tu_usuario
       password: tu_contraseña
   ```

4. **Compilar y verificar:**
   ```bash
   mvn clean verify
   ```

5. **Ejecutar la aplicación:**
   ```bash
   mvn spring-boot:run -Dspring-boot.run.profiles=dev
   ```

La aplicación estará disponible en [http://localhost:8080](http://localhost:8080).

---

## Futuras implementaciones

### Arquitectura y calidad

- [ ] Capa de DTOs y mappers para desacoplar la API del modelo interno.
- [ ] Manejo global de excepciones con `@ControllerAdvice`.
- [ ] Cobertura de tests: unitarios de servicio, integración y controladores.

### Infraestructura y despliegue

- [ ] Pipeline de despliegue continuo (CD) en GitHub Actions.
- [ ] Dockerización de la aplicación y la base de datos.
- [ ] Orquestación con Docker Compose.
- [ ] Despliegue en Oracle Cloud Infrastructure (OCI).

### Frontend

- [ ] Migración a React (SPA) con API REST como backend.
