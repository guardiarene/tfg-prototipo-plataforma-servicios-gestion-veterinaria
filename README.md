# Prototipo de Plataforma de Servicios y Gestión de Clínicas Veterinarias

Este proyecto forma parte del Trabajo de Fin de Grado titulado *Plataforma de Servicios y Gestión de Clínicas
Veterinarias*.  

El objetivo del mismo consistió en permitir la creación, mantenimiento y acceso compartido a historias clínicas
unificadas entre diferentes clínicas veterinarias.

## Tecnologías utilizadas

El prototipo fue desarrollado con las siguientes tecnologías:

- **Backend:**
    - Java 21
    - Spring Boot 3.3.5
    - Spring Security 6.4.0
    - Spring Data JPA 3.4.0

- **Frontend:**
    - Thymeleaf 3.1.2
    - Bootstrap 5.3.3

- **Base de datos:**
    - **MySQL 9.1.0**

- **Pruebas:**
    - JUnit 5.11.3
    - Mockito 5.14.2

## Requisitos previos

Antes de instalar y ejecutar el proyecto, es necesario tener instaladas las siguientes tecnologías:

- **Java JDK 21**
- **Apache Maven**
- **Servidor de base de datos MySQL**

## Instrucciones de instalación y ejecución

1. **Clonar el repositorio:**
   ```bash
   git clone https://github.com/tu-usuario/tfg-prototipo-plataforma-servicios-gestion-vet.git
   cd tfg-prototipo-plataforma-servicios-gestion-vet
   ```

2. **Configurar la base de datos:**

Crear la base de datos en MySQL:

```sql
CREATE DATABASE veterinaria;
```

Configurar las credenciales de la base de datos en el archivo `application.properties`:

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/veterinaria
spring.datasource.username=tu_usuario
spring.datasource.password=tu_contraseña
```

3. **Compilar y ejecutar el proyecto:**

```bash
mvn clean install
mvn spring-boot:run
```

4. **Abrir el navegador y visitar** `http://localhost:8080` para acceder a la aplicación.

## Video explicativo
