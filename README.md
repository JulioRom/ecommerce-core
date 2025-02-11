# 📦 Ecommerce API

Ecommerce API es un backend desarrollado en **Spring Boot** para la gestión de una tienda en línea. Incluye autenticación con **JWT**, gestión de usuarios, productos, carritos de compra y compras.

## 🚀 Características
👉 **Autenticación y autorización con JWT**  
👉 **CRUD de usuarios** (registro, login, búsqueda, actualización y eliminación)  
👉 **Gestión de productos** (crear, listar, buscar, actualizar y eliminar)  
👉 **Carrito de compras** (agregar, eliminar y vaciar productos)  
👉 **Proceso de compra** (validación de stock y confirmación de pedido)

---

## 🎠 Tecnologías Utilizadas
- **Spring Boot 3**
- **Spring Security** (Autenticación JWT)
- **Spring Data JPA** (Persistencia con PostgreSQL)
- **Spring Web** (API REST)
- **Lombok** (Reducción de código boilerplate)
- **Hibernate Validator** (Validación de datos)
- **Jakarta Validation**
- **PostgreSQL** (Base de datos)
- **MongoDB** (Gestión del carrito de compras)
- **JWT (Json Web Token)** (Autenticación)
- **Maven** (Gestión de dependencias)

---

## 📌 Instalación y Configuración

### **1️⃣ Clonar el repositorio**
```bash
git clone https://github.com/tu-repositorio/ecommerce-api.git
cd ecommerce-api
```

### **2️⃣ Configurar Base de Datos**
Asegúrate de que PostgreSQL y MongoDB estén instalados y configurados.

📉 **Crea la base de datos en PostgreSQL:**
```sql
CREATE DATABASE ecommerce_db;
```

📉 **Configura `application.properties`:**
```properties
# Configuración PostgreSQL
spring.datasource.url=jdbc:postgresql://localhost:5432/ecommerce_db
spring.datasource.username=tu_usuario
spring.datasource.password=tu_contraseña

# Configuración MongoDB
spring.data.mongodb.uri=mongodb://localhost:27017/ecommerce

# JWT Config
jwt.secret=clave-secreta-segura
jwt.expiration=3600000  # 1 hora en milisegundos
```

### **3️⃣ Compilar y Ejecutar**
```bash
mvn clean install
mvn spring-boot:run
```

📉 **La API se iniciará en** `http://localhost:8080`

---

## 🔥 Endpoints Principales

### **Autenticación**
| Método | Endpoint               | Descripción |
|--------|------------------------|-------------|
| `POST` | `/api/auth/register`   | Registrar usuario |
| `POST` | `/api/auth/login`      | Iniciar sesión y obtener JWT |

### **Usuarios**
| Método | Endpoint               | Descripción |
|--------|------------------------|-------------|
| `GET`  | `/api/usuarios`        | Listar usuarios (ADMIN) |
| `GET`  | `/api/usuarios/{id}`   | Obtener usuario por ID |
| `GET`  | `/api/usuarios/buscar` | Buscar usuario por username |
| `PUT`  | `/api/usuarios/{username}` | Actualizar usuario |
| `DELETE` | `/api/usuarios/{id}` | Eliminar usuario (ADMIN) |

---

## 🛡️ Autenticación con JWT
1. **Registrarse (`/api/auth/register`)**
2. **Iniciar sesión (`/api/auth/login`)** y obtener el token JWT.
3. **Usar el token en las solicitudes protegidas**, agregándolo en los headers:
   ```
   Authorization: Bearer <TOKEN>
   ```

---

## 📚 Estructura del Proyecto
```
📂 ecommerce-api
 ┗📂 src/main/java/com/springproject/ecommercecore
   ┗📂 controller    # Controladores REST
   ┗📂 model         # Modelos de la base de datos
   ┗📂 repository    # Repositorios JPA y MongoDB
   ┗📂 security      # Configuración de JWT y seguridad
   ┗📂 service       # Lógica de negocio
   ┗📚 EcommerceApplication.java  # Clase principal
 ┗📂 src/main/resources
   ┗📚 application.properties  # Configuración del proyecto
 ┗📚 pom.xml  # Dependencias del proyecto
```

---

## 🚀 Mejoras Futuras
✅ Implementación de WebSockets para actualizaciones en tiempo real.  
✅ Manejo avanzado de roles y permisos.  
✅ Implementación de pagos en línea con Stripe o PayPal.  
✅ Integración con frontend en **React/Vue/Angular**.

---

## 🧑‍💻 Autor

- **Desarrollado por JulioRom**
- 📧 **Correo:** [julioandrescampos@gmail.com](mailto:julioandrescampos@gmail.com)
- 🔗 **GitHub:** [https://github.com/JulioRom](https://github.com/JulioRom)

## 📜 Licencia

Este proyecto está bajo la **Licencia MIT**. Consulta el archivo LICENSE para más detalles.
