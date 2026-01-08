# 🔐 Spring Boot JWT Authentication & Authorization

A **production-ready Spring Boot application** implementing **JWT-based authentication and role-based authorization** using **Spring Security**, **JPA**, and **BCrypt password encryption**.

This project follows **clean architecture**, **DTO-based APIs**, and **stateless security** best practices.

---

## 🚀 Features

- User Signup & Login
- Secure password hashing using **BCrypt**
- **JWT (JSON Web Token)** based authentication
- Role-based authorization (`ADMIN`, `USER`, etc.)
- Stateless session management
- DTO-based request/response handling
- Centralized exception handling
- Clean separation of layers (Controller, Service, Repository)

---

## 🛠 Tech Stack

- **Java 17**
- **Spring Boot 3**
- **Spring Security**
- **JWT (jjwt)**
- **Spring Data JPA**
- **Hibernate**
- **MySQL / PostgreSQL**
- **Maven**



## 🔐 Authentication Flow

1. User signs up or logs in
2. Credentials are validated
3. Password verified using BCrypt
4. JWT generated and returned
5. Client sends JWT in Authorization header

```
Authorization: Bearer <JWT_TOKEN>
```

---

## 📌 API Endpoints

### 🔓 Signup
```
POST /auth/signup
```

```json
{
  "email": "user@gmail.com",
  "password": "password123",
  "name": "User Name",
  "rollNO": "CS101",
  "role": "USER"
}
```

### 🔓 Login
```
POST /auth/login
```

```json
{
  "data": "user@gmail.com",
  "password": "password123"
}
```

---

## 🧪 Sample Response

```json
{
  "token": "eyJhbGciOiJIUzI1NiJ9...",
  "email": "user@gmail.com",
  "role": "USER"
}
```




## 👨‍💻 Author

**Subham Kumar Navratan**
