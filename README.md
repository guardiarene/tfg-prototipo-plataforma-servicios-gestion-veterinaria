# PSyGCV: Plataforma de Servicios y Gestión de Clínicas Veterinarias

![Java](https://img.shields.io/badge/Java-25-orange?style=for-the-badge&logo=openjdk)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-4.0.2-brightgreen?style=for-the-badge&logo=springboot)
![MySQL](https://img.shields.io/badge/MySQL-8.0-blue?style=for-the-badge&logo=mysql)
![Thymeleaf](https://img.shields.io/badge/Frontend-Thymeleaf%20%2B%20Bootstrap-005F0F?style=for-the-badge&logo=thymeleaf)

Este proyecto es un prototipo desarrollado como parte del Trabajo de Fin de Grado (TFG) titulado **“Plataforma de
Servicios y Gestión de Clínicas Veterinarias”**.

Su objetivo primordial es proporcionar una solución integral para la gestión de clínicas veterinarias, permitiendo la
creación, mantenimiento y acceso compartido a **historias clínicas unificadas**. Facilita la interoperabilidad entre
diferentes sedes o clínicas, optimizando la gestión de animales, turnos médicos, servicios veterinarios y administración
de usuarios.

---

## Tecnologías y Herramientas

### Backend

- **Core:** Java 25 & Spring Boot 4.0.2
- **Seguridad:** Spring Security (Autenticación y Autorización basada en roles)
- **Persistencia:** Spring Data JPA con Hibernate
- **Validación:** Jakarta Bean Validation

### Frontend

- **Motor de plantillas:** Thymeleaf
- **Estilos:** Bootstrap 5
- **Interactividad:** JavaScript

### Infraestructura y datos

- **Base de datos:** MySQL 8.0 (Producción/Dev), H2 (Testing)
- **Gestión de dependencias:** Maven
- **Calidad de código:** Checkstyle (Google Java Style Guide)

### Integración continua (CI/CD)

- **GitHub Actions:** Pipeline automatizado para compilación, verificación de estilo y ejecución de tests
  unitarios/integración.

---

## Arquitectura del Sistema

La aplicación sigue una arquitectura organizada en capas, promoviendo la separación de responsabilidades y la
mantenibilidad:

- **`controller`**: Puntos de entrada de la API y manejo de peticiones web.
- **`service`**: Capa de lógica de negocio y gestión de transacciones.
- **`repository`**: Abstracción de acceso a datos mediante el patrón Repository.
- **`model` / `entity`**: Definición del modelo de dominio y entidades JPA.
- **`config`**: Configuraciones de seguridad, beans y perfiles de entorno.
- **`dto` / `mapper`**: *(En proceso)* Objetos de transferencia de datos y mapeadores para desacoplar la API del modelo
  interno.

---

## Instalación y Configuración

### Requisitos previos

- **Java JDK 25** o superior.
- **Apache Maven 3.9+**.
- **MySQL Server 8.0**.

### Pasos para la ejecución

1. **Clonación del proyecto:**
   ```bash
   git clone https://github.com/guardiarene/tfg-prototipo-plataforma-servicios-gestion-veterinaria.git
   cd tfg-prototipo-plataforma-servicios-gestion-veterinaria
   ```

2. **Preparación de la base de datos:**
   Accede a tu instancia de MySQL y ejecuta:
   ```sql
   CREATE DATABASE psygcs_dev;
   ```

3. **Configuración de entorno:**
   Ajusta las credenciales en `src/main/resources/application-dev.yaml`:
   ```yaml
   spring:
     datasource:
       url: jdbc:mysql://localhost:3306/psygcs_dev
       username: tu_usuario
       password: tu_contraseña
   ```

4. **Compilación y despliegue local:**
   ```bash
   # Verificar estilo y pasar tests
   mvn clean verify

   # Ejecutar la aplicación
   mvn spring-boot:run
   ```

5. **Acceso:**
   La aplicación estará disponible en: [http://localhost:8080](http://localhost:8080)

---

## Futuras Implementaciones

El proyecto se encuentra en una fase de desarrollo continua. Las siguientes tareas representan los próximos hitos
a concretar:

### Modernización del frontend

- [ ] **Migración a React:** Transición de una arquitectura monolítica con Thymeleaf hacia una Single Page Application (
  SPA) moderna.
- [ ] **API First:** Completar la capa de DTOs y Mappers para ofrecer una API REST robusta consumible por el nuevo
  frontend.

### Infraestructura y despliegue

- [ ] **Dockerización:** Creación de imágenes Docker para la aplicación y la base de datos para garantizar entornos
  consistentes.
- [ ] **Orquestación:** Implementar `docker-compose` para despliegues simplificados.
- [ ] **Oracle Cloud Infrastructure (OCI):** Configuración del entorno de producción en la nube de Oracle, aprovechando
  su capa gratuita para el despliegue del prototipo.

### Calidad y robustez

- [ ] **Manejo global de excepciones:** Implementar `@ControllerAdvice` para respuestas de error consistentes.
- [ ] **Ampliación de cobertura:** Incrementar los tests de integración y de capa de presentación.
