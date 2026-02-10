# Prototipo de Plataforma de Servicios y Gestión de Clínicas Veterinarias

Este proyecto es un prototipo desarrollado como parte del Trabajo de Fin de Grado (TFG) titulado  
**“Plataforma de Servicios y Gestión de Clínicas Veterinarias”**.

El objetivo principal es permitir la creación, mantenimiento y acceso compartido a historias clínicas unificadas entre
diferentes clínicas veterinarias, facilitando la gestión integral de pacientes, citas, servicios médicos y usuarios.

---

## Tecnologías utilizadas

### Backend

- Java 25
- Spring Boot 4
- Spring Security
- Spring Data JPA
- Hibernate

### Frontend

- Thymeleaf
- Bootstrap

### Base de datos

- MySQL (desarrollo / producción)
- H2 (tests)

### Testing

- JUnit 5
- Mockito

### Herramientas y calidad

- Maven
- Checkstyle (Google Java Style)
- Google Java Formatter
- GitHub Actions (CI)

---

## Requisitos previos

- **Java JDK 25**
- **Apache Maven**
- **MySQL Server**

---

## Instalación y ejecución

### 1. Clonar el repositorio

```bash
 git clone https://github.com/guardiarene/tfg-prototipo-plataforma-servicios-gestion-veterinaria.git
 cd tfg-prototipo-plataforma-servicios-gestion-vet
```

### 2. Configurar la base de datos

Crear la base de datos en MySQL:

```sql
CREATE
DATABASE prototipo.bd;
```

Configurar las credenciales en el archivo correspondiente al perfil activo
(ejemplo: `application-dev.yaml`):

```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/veterinaria
    username: tu_usuario
    password: tu_contraseña
```

### 3. Compilar y ejecutar

```bash
mvn clean verify
mvn spring-boot:run
```

### 4. Acceso a la aplicación

Abrir el navegador y visitar:
`http://localhost:8080`

---

## Arquitectura general

El proyecto sigue una arquitectura en capas:

* **Controller**: manejo de solicitudes HTTP y validación de entrada.
* **Service**: lógica de negocio y control transaccional.
* **Repository**: acceso a datos mediante Spring Data JPA.
* **Entity**: modelo de dominio persistente.
* **DTO**: objetos de transferencia para entrada y salida. *(Actualmente no implementados, priorizar su creación)*
* **Mapper**: conversión explícita entre entidades y DTOs. *(Actualmente no implementados, priorizar su creación)*
* **Exception**: excepciones de dominio y manejo global. *(Actualmente no implementadas, priorizar su creación)*
* **Config**: configuración de seguridad, perfiles y beans.

---

## Calidad y buenas prácticas aplicadas

* Separación clara de responsabilidades.
* Uso de perfiles (`dev`, `test`, `prod`).
* Checkstyle con **Google Java Style**.
* Formateo consistente del código.
* Pipeline de CI con GitHub Actions.
* Tests ejecutados automáticamente en cada push.
* Uso controlado de Lombok.
* Transacciones declaradas solo en la capa de servicio.

---

## Pendientes y mejoras planificadas

Estas tareas representan **mejoras progresivas**, alineadas con un nivel junior profesional y el estado actual del
proyecto:

### Arquitectura y código

* [ ] Crear los paquetes/folders `dto`, `mapper` y `exception`.
* [ ] Implementar DTOs para todas las operaciones públicas (no exponer entidades en controladores).
* [ ] Implementar mappers explícitos entre entidades y DTOs.
* [ ] Añadir validaciones con `jakarta.validation` en DTOs de entrada.
* [ ] Refactorizar controladores para eliminar cualquier lógica de negocio.
* [ ] Revisar nombres de clases, métodos y endpoints para mayor claridad.

### Manejo de errores

* [ ] Definir excepciones de dominio específicas.
* [ ] Centralizar el manejo de errores con `@ControllerAdvice`.
* [ ] Garantizar respuestas HTTP claras y coherentes.
* [ ] Evitar exposición de trazas internas.

### Testing

* [ ] Ampliar tests unitarios de servicios.
* [ ] Añadir tests de controladores con `@WebMvcTest`.
* [ ] Añadir tests de repositorios con `@DataJpaTest`.
* [ ] Aumentar cobertura en flujos críticos.

### Calidad y mantenimiento

* [ ] Revisar logs para evitar información sensible.
* [ ] Documentar lógica no trivial.
* [ ] Mantener el cumplimiento de Checkstyle en todo el código.
* [ ] No añadir dependencias sin justificación clara.
* [ ] Mantener commits siguiendo **Conventional Commits**.

