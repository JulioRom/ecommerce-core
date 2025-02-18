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

### **Productos**
| Método  | Endpoint                | Descripción |
|---------|-------------------------|-------------|
| `GET`   | `/api/productos`        | Listar productos |
| `GET`   | `/api/productos/{codigoProducto}` | Obtener producto por código |
| `POST`  | `/api/productos`        | Agregar producto (ADMIN) |
| `PUT`   | `/api/productos/{codigoProducto}` | Actualizar producto (ADMIN) |
| `DELETE`| `/api/productos/{codigoProducto}` | Eliminar producto (ADMIN) |

### **Compras**
| Método  | Endpoint                        | Descripción |
|---------|---------------------------------|-------------|
| `POST`  | `/api/compras/{idUsuario}`      | Generar una nueva compra |
| `GET`   | `/api/compras/usuario/id/{idUsuario}` | Obtener órdenes de un usuario por ID |
| `GET`   | `/api/compras/usuario/username/{username}` | Obtener órdenes de un usuario por username |
| `GET`   | `/api/compras/{idOrden}`        | Obtener una orden de compra por ID |
| `PUT`   | `/api/compras/{idOrden}/estado` | Actualizar el estado de una orden (ADMIN) |
| `DELETE`| `/api/compras/{idOrden}/cancelar` | Cancelar una orden de compra |

### **Carrito de Compras**
| Método  | Endpoint                         | Descripción |
|---------|----------------------------------|-------------|
| `POST`  | `/api/carrito/{identificador}`   | Agregar producto al carrito |
| `GET`   | `/api/carrito/{identificador}`   | Obtener el carrito de un usuario |
| `DELETE`| `/api/carrito/{identificador}/{codigoProducto}` | Eliminar un producto del carrito |
| `DELETE`| `/api/carrito/{identificador}`   | Vaciar el carrito de compras |

### **Pruebas y CORS**
| Método  | Endpoint        | Descripción |
|---------|---------------|-------------|
| `GET`   | `/api/test`   | Prueba de conexión con el backend |
| `OPTIONS` | `/api/test` | Verificar si CORS está habilitado |
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

# 🚀🔥 ¡Descubre la API en Acción con Swagger UI! 🔥🚀
💡 **¿Listo para explorar la API como un PRO?** Hemos implementado una **documentación interactiva** donde puedes probar todos los endpoints, enviar solicitudes en tiempo real y autenticarte con JWT.

📌 **¡No te quedes con la teoría, pruébala ahora mismo!** 👇👇

## 🌍 **Accede a Swagger UI en vivo:**
🎯 **[👉 Swagger UI - Ecommerce Core API 👈](https://ecommerce-core-production.up.railway.app/swagger-ui.html)** 🎯

---

## 🎯 **¿Qué puedes hacer en Swagger UI?**
✅ 📄 **Ver toda la documentación de la API** en una interfaz intuitiva.  
✅ 🔑 **Autenticarse con JWT** y probar endpoints protegidos de forma segura.  
✅ 🚀 **Hacer solicitudes GET, POST, PUT, DELETE** con un solo clic.  
✅ 📡 **Obtener respuestas en tiempo real** directamente desde el backend en producción.  
✅ 💡 **Descubrir y probar nuevas funcionalidades** sin escribir código.

> **🔥 TIP:** Inicia sesión primero para obtener el token y úsalo en los endpoints protegidos.

---

### 🔥 **¡No esperes más! Haz clic y explora la API en vivo ahora mismo:**
[![Swagger UI](https://img.shields.io/badge/Swagger-UI-green?style=for-the-badge&logo=swagger)](https://ecommerce-core-production.up.railway.app/swagger-ui.html)

---

### 🔑 **Autenticación en Swagger**
Para probar los endpoints protegidos, sigue estos pasos:

1. **Ir a la sección "Authorize"** en Swagger UI.
2. **Ingresar el token con el prefijo `Bearer`**, ejemplo:
   ```
   Bearer tu_token_aquí
   ```
3. **Hacer clic en "Authorize" y cerrar la ventana.**
4. **Ahora podrás probar los endpoints protegidos.**

📌 **Importante:**  
Para obtener un token, primero debes autenticarte usando el endpoint `/api/auth/login`, enviando las credenciales en el cuerpo de la solicitud.

---

## 🔧 **Configuración y Ejecución en Local**
### 📌 **Requisitos**
- Java 17+
- Maven
- Docker (opcional, para bases de datos)

### 🚀 **Ejecutar el Proyecto en Local**
```bash
# Clonar el repositorio
git clone https://github.com/JulioRom/ecommerce-core
cd tu-repositorio

# Configurar el perfil de desarrollo
export SPRING_PROFILES_ACTIVE=dev  # Linux/macOS
set SPRING_PROFILES_ACTIVE=dev      # Windows

# Ejecutar con Maven
mvn spring-boot:run
```

### 🛠 **Variables de Entorno**
Crea un archivo `.env` en la raíz del proyecto con las siguientes variables (para desarrollo):

```env
DATABASE_URL=jdbc:postgresql://localhost:5432/ecommerce
DATABASE_USER=admin
DATABASE_PASSWORD=admin
MONGODB_URI=mongodb://localhost:27017/ecommerce
JWT_SECRET=ClaveSecretaDesarrollo
JWT_EXPIRATION=3600000
CORS_ALLOWED_ORIGINS=http://localhost:3000
```

---

## 📦 **Despliegue en Producción**
Este proyecto está desplegado en **Railway**, donde Swagger UI está disponible para probar los endpoints.

🔹 **Railway maneja las variables de entorno en la sección "Variables".**  
🔹 **No es necesario un archivo `.env` en producción.**

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