# 🛒 Tienda en Línea – Backend

Backend de una aplicación de e-commerce desarrollado con Spring Boot, que gestiona autenticación de usuarios, productos,
carrito de compras y órdenes de venta.
Pensado como API REST para ser consumida por un frontend (Angular u otro).

## 📌 Características principales

- Autenticación y autorización con JWT
- Gestión de usuarios y roles
- Gestión de productos y categorías
- Carrito de compras
- Creación de seguimiento de ordenes
- Arquitectura en capas (Controller, Servic, Repository)

## 🧱 Arquitectura del proyecto

```text
com.TiendaEnLinea.TiendaEnLinea
├── config          # Configuración de seguridad y CORS
├── controllers     # Controladores REST
├── services        # Lógica de negocio
├── repository      # Acceso a datos (JPA)
├── entity          # Entidades JPA
├── dtos            # DTOs de request y response
├── utils           # JWT, mappers y validaciones
├── exceptions      # Manejo global de errores
└── enums           # Enumeraciones del dominio


```

## Tecnologías utilizadas

- Java 21
- Spring Boot
- Spring Security
- Jwt (Json Web Token)
- Spring Data JPA
- MYSQL
- Maven

## Variables de entorno

### Base de Datos

- DB_URL=jdbc:mysql://localhost:3306/TIENDA_EN_LINEA?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true
- DB_USER=root
- DB_PASSWORD=tu_password

### JWT

- JWT_SECRET=clave_secreta_minimo_32_caracteres
- JWT_EXPIRATION=86400000

### Server

- Server_PORT:8082

## Como ejecutar el proyecto

#### Clanar el repositorio
- git clone <url-del-repositorio>>
- cd TiendaEnLinea


#### Configuración de la base de datos

- Crear la base de datos TIENDA_EN_LIENEA
- Ajustar credenciales en las variables de entorno

Ejecutar la apa ./mvnw spring-boot:run la API quedará disponible en http://localhost:8082


## Autencación (JWT)

EL sistema utiliza JWT para proteger endpoints.

Flujo:
 
- Login del usuario
- El backend devuelve un token JWT
- El token se envía en cada request protegido

## Edpoint principales

Usuarios:

- POST http://localhost:8082/usuarios/register
- PUT http://localhost:8082/usuarios/updateData/2
- PATCH http://localhost:8082/usuarios/updateRoles/1
- GET http://localhost:8082/usuarios/2
- GET http://localhost:8082/usuarios/all
