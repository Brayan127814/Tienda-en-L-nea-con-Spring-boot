


# 🛒 Tienda en Línea – Backend


Backend de una aplicación de e-commerce desarrollado con Spring Boot, que gestiona autenticación de usuarios, productos, carrito de compras y órdenes de venta.
Pensado como API REST para ser consumida por un frontend (Angular u otro).



## 📌 Características principales

- Autenticación y autorización con JWT
- Gestión de usuarios y roles
- Gestión de productos y categorías
- Carrito de compras
- Creación de seguimiento de ordenes
- Arquitectura en capas (Controller, Servic, Repository)



## Arquitectura del proyecto 

com.TiendaEnLinea.TiendaEnLinea
│
├── config # Configuración de seguridad y CORS
├── controllers # Controladores REST
├── services # Lógica de negocio
├── Repository # Acceso a datos (JPA)
├── Entity # Entidades JPA
├── dtos # DTOs de request y response
├── utils # JWT, mappers y validaciones
├── Exceptions # Manejo global de errores
└── Enum # Enumeraciones del dominio




## Tecnologías utilizadas

- Java 21
- Spring Boot
- Spring Security
- Jwt (Json Web Token)
- Spring Data JPA
- MYSQL
- Maven


