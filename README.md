# 🛒 Tienda en Línea - Backend

Backend de una aplicación de e-commerce desarrollada con **Spring Boot**, que incluye
autenticación con JWT, manejo de usuarios, productos, carrito de compras y órdenes.

---

## 🚀 Tecnologías
- Java 17
- Spring Boot
- Spring Security + JWT
- Spring Data JPA
- MySQL
- Maven

---

## ⚙️ Variables de entorno

El proyecto usa variables de entorno para manejar credenciales y datos sensibles.

Crear un archivo `.env` o definirlas en el sistema:

```env
DB_URL=jdbc:mysql://localhost:3306/TIENDA_EN_LINEA?useSSL=false&serverTimezone=UTC
DB_USER=root
DB_PASSWORD=tu_password
JWT_SECRET=tu_clave_secreta
---
---


#### Base de Datos
 - CREATE DATABASE TIENDA_EN_LINEA;
