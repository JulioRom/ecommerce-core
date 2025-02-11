# Ecommerce API - Spring Boot 🛒

## 📌 Descripción
Ecommerce API es un sistema REST desarrollado con **Spring Boot** que permite la gestión de carritos de compra en **MongoDB** y la realización de transacciones en **PostgreSQL**. Implementa **Spring Security con JWT** para autenticación y autorización, asegurando la protección de los endpoints.

## 🚀 Características
- 🔹 **Gestión de carritos** en **MongoDB**.
- 🔹 **Gestión de productos y compras** en **PostgreSQL**.
- 🔹 **Autenticación y autorización** con **JWT y Spring Security**.
- 🔹 **Roles de usuario (`USER`, `ADMIN`)** para control de accesos.
- 🔹 **Arquitectura basada en capas** (Controladores, Servicios, Repositorios, Modelos).
- 🔹 **Docker y Docker Compose** para facilitar la ejecución.

## 📦 Tecnologías Utilizadas
- **Java 17**
- **Spring Boot 3**
- **Spring Data JPA & MongoDB**
- **PostgreSQL & MongoDB**
- **Spring Security con JWT**
- **Lombok**
- **Maven**
- **Testcontainers** (para pruebas con bases de datos en Docker)
- **Docker & Docker Compose**

## ⚙️ Instalación y Configuración

### 1️⃣ **Clonar el repositorio**
```bash
git clone https://github.com/JulioRom/ecommerce-app.git
cd ecommerce-app
```

### 2️⃣ **Configurar las bases de datos**
#### 📌 PostgreSQL
Crea la base de datos en PostgreSQL:
```sql
    CREATE DATABASE ecommerce;
```
Modifica `application.yml` con las credenciales:
```yaml
spring:
  datasource:
    url: jdbc:postgresql://localhost:5432/ecommerce
    username: postgres
    password: admin
    driver-class-name: org.postgresql.Driver
  jpa:
    hibernate:
      ddl-auto: update
    show-sql: true
    properties:
      hibernate.format_sql: true
```
#### 📌 MongoDB
Asegúrate de tener MongoDB ejecutándose en `localhost:27017` y configura:
```yaml
spring:
  data:
    mongodb:
      uri: mongodb://localhost:27017/ecommerce
```

### 3️⃣ **Ejecutar la aplicación sin Docker**
Si no deseas usar Docker, ejecuta la aplicación de la siguiente manera:
```bash
mvn clean install
mvn spring-boot:run
```

---

## 🐳 Ejecución con Docker

### **1️⃣ Construcción de la imagen**
Ejecuta el siguiente comando para construir la imagen Docker del backend:
```bash
docker build -t ecommerce-api .
```

### **2️⃣ Ejecutar con Docker Compose**
Para ejecutar la aplicación junto con PostgreSQL y MongoDB, usa **Docker Compose**:
```bash
docker-compose up -d
```

Esto iniciará:
- **PostgreSQL** en el puerto `5432`
- **MongoDB** en el puerto `27017`
- **Ecommerce API** en el puerto `8080`

### **3️⃣ Detener los contenedores**
Si necesitas detener los servicios, ejecuta:
```bash
docker-compose down
```

---

## 🔑 Endpoints Principales
### **Autenticación**
- `POST /api/auth/register` → Registro de usuario
- `POST /api/auth/login` → Generación de JWT

### **Carrito de Compra (MongoDB)**
- `POST /api/carrito` → Agregar carrito
- `GET /api/carrito/{idUsuario}` → Obtener carrito

### **Compras (PostgreSQL)**
- `POST /api/compra/{idUsuario}` → Generar compra desde un carrito

---

## 🔐 Seguridad y Roles
- `USER`: Puede agregar carritos y comprar.
- `ADMIN`: Puede gestionar productos y revisar transacciones.

---

## 🧑‍💻 Autor

- **Desarrollado por JulioRom**
- 📧 **Correo:** [julioandrescampos@gmail.com](mailto:julioandrescampos@gmail.com)
- 🔗 **GitHub:** [https://github.com/JulioRom](https://github.com/JulioRom)

---

## 📜 Licencia
Este proyecto es de código abierto y se distribuye bajo la licencia MIT.

---
🚀 **¡Listo para empezar!** Corre la API y prueba los endpoints con **Postman** o **Swagger**. 😃
