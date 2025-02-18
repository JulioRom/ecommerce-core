# 📦 Ecommerce API

Ecommerce API es un backend desarrollado en **Spring Boot** para la gestión de una tienda en línea. Incluye autenticación con **JWT**, gestión de usuarios, productos, carritos de compra y compras.

## 🚀 Características
👉 **Autenticación y autorización con JWT**  
👉 **CRUD de usuarios** (registro, login, búsqueda, actualización y eliminación)  
👉 **Gestión de productos** (crear, listar, buscar, actualizar y eliminar)  
👉 **Carrito de compras** (agregar, eliminar y vaciar productos)  
👉 **Proceso de compra** (validación de stock y confirmación de pedido)  
👉 **Documentación con Swagger** (Exploración y prueba de endpoints)

---

## 🎠 Tecnologías Utilizadas
- **Spring Boot 3.4.2** - Framework principal
- **Spring Security & JWT** - Autenticación y autorización
- **Spring Data JPA** - Acceso a PostgreSQL
- **Spring Data MongoDB** - Acceso a MongoDB
- **Hibernate** - ORM para PostgreSQL
- **MongoDB** (Gestión del carrito de compras)
- **Jakarta Validation** - Validaciones de datos
- **Lombok** - Reducción de código repetitivo
- **MapStruct** - Mapeo de DTOs
- **Swagger OpenAPI** - Documentación de la API
- **JUnit 5 & Mockito** - Pruebas unitarias e integración
- **Docker & Docker Compose** - Contenerización del sistema
- **Maven** (Gestión de dependencias)

---

## 🛠️ Instalación y Configuración

1. Clonar el repositorio:
   ```sh
   git clone https://github.com/tu-repo/ecommerce-api.git
   cd ecommerce-api
   ```
2. Configurar el archivo `.env` o `application.yml` con las credenciales de BD.
3. Ejecutar con Docker Compose:
   ```sh
   docker-compose up -d
   ```
4. Para ejecutar las pruebas:
   ```sh
   mvn test
   ```
---

## 🚀 Configuración de Variables de Entorno

Este proyecto utiliza un archivo `.env` para almacenar configuraciones sensibles. **Por seguridad, el archivo `.env` no se incluye en el repositorio**. Sigue estos pasos para configurarlo:

1. **Copia el archivo de referencia:**
   cp .env.example .env
2. **Edita el archivo `.env` y completa los valores según tu entorno.**

📌 **Importante:** Nunca subas el archivo `.env` al repositorio, ya que contiene información sensible.

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

## 🔒 Autenticación y Seguridad

1. **Registro de Usuario:**
   ```http
   POST api/auth/register
   ```
2. **Inicio de Sesión:**
   ```http
   POST api/auth/login
   ```
   - Respuesta: JWT Token
3. **Acceso a Endpoints Protegidos:**
   - Enviar el token en el encabezado:
     ```http
     Authorization: Bearer <token>
     ```
---

## ✅ Pruebas y Cobertura

Se han implementado **pruebas unitarias y de integración** para validar la funcionalidad:

| Servicio                | Pruebas Unitarias | Pruebas de Integración | Observaciones |
|-------------------------|------------------|------------------------|--------------|
| `UsuarioService`        | ✅ Sí            | ✅ Necesario           | Validar autenticación y persistencia |
| `ProductoService`       | ✅ Sí            | ⚠️ Opcional            | Cobertura completa con unitarias |
| `CompraService`         | ✅ Sí            | ✅ Necesario           | Validar persistencia en PostgreSQL |
| `CarritoCompraService`  | ✅ Sí            | ✅ Necesario           | Validar persistencia en MongoDB |

---

## 📖 Documentación con Swagger
Swagger permite explorar y probar la API de manera interactiva.

📄 **Acceder a la documentación Swagger:**
```url
http://localhost:8080/swagger-ui/index.html
```

📄 **Obtener el archivo OpenAPI en JSON:**
```url
http://localhost:8080/v3/api-docs
```

📄 **Obtener el archivo OpenAPI en YAML:**
```url
http://localhost:8080/v3/api-docs.yaml
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
   ┗📂 dataaccess    # Acceso a base de datos (PostgreSQL/MongoDB)
   ┗📂 exception     # Manejo de excepciones globales
   ┗📂 config        # Configuraciones de la aplicación

   ┗📚 EcommerceApplication.java  # Clase principal
 ┗📂 src/main/resources
   ┗📚 application.properties  # Configuración del proyecto
 ┗📚 pom.xml  # Dependencias del proyecto
```

---

## 🚀 Mejoras Futuras

- Integración con **pasarelas de pago**.
- Implementación de **WebSockets** para notificaciones en tiempo real.
- Desarrollo del **frontend en React o Angular**.
- Optimización de consultas con **Redis Cache**.
---

## 🧑‍💻 Autor

- **Desarrollado por JulioRom**
- 📧 **Correo:** [julioandrescampos@gmail.com](mailto:julioandrescampos@gmail.com)
- 🔗 **GitHub:** [https://github.com/JulioRom](https://github.com/JulioRom)

## 📜 Licencia

Este proyecto está bajo la **Licencia MIT**. Consulta el archivo LICENSE para más detalles.