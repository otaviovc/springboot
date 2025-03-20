# 🛒 Spring Boot Test - Order Management System

This is a **Spring Boot REST API** for managing **Clients, Orders, and Products**.  
The system allows users to **create, retrieve, update, and delete** clients, orders, and products while ensuring database integrity.

## 🚀 Features

✅ **CRUD operations** for Clients, Orders, and Products  
✅ **One-to-Many relationship** example between Clients and Orders  
✅ **Many-to-Many relationship** example between Orders and Products  
✅ **Exception Handling** with a Global Exception Handler  
✅ **Data validation** using `@Valid` DTOs  
✅ **Database persistence** with PostgreSQL or MySQL with Spring Data JPA  
✅ **Error messages** for data integrity violations  
✅ **HATEOAS support** for enhanced API responses  
✅ **Spring Profiles** allow seamless switching between **PostgreSQL** and **MySQL** by setting the active profile (`spring.profiles.active`) in the configuration or via command-line arguments.

## 🏗️ Technologies Used

- **Java** (Spring Boot 3+)
- **Spring Data JPA**
- **Hibernate**
- **PostgreSQL**
- **MySQL**
- **Maven**
- **RESTful APIs**
- **Postman** (for testing)


## 📌 API Endpoints

### **Client Endpoints**
| Method | Endpoint         | Description              |
|--------|-----------------|--------------------------|
| GET    | `/clients`       | Get all clients         |
| GET    | `/clients/{id}`  | Get client by ID        |
| POST   | `/clients`       | Create a new client     |
| PUT    | `/clients/{id}`  | Update an existing client |
| DELETE | `/clients/{id}`  | Delete a client         |

### **Product Endpoints**
| Method | Endpoint         | Description                |
|--------|-----------------|----------------------------|
| GET    | `/products`       | Get all products           |
| GET    | `/products/{id}`  | Get product by ID          |
| POST   | `/products`       | Create a new product       |
| PUT    | `/products/{id}`  | Update an existing product |
| DELETE | `/products/{id}`  | Delete a product           |

### **Order Endpoints**
| Method | Endpoint         | Description                  |
|--------|-----------------|------------------------------|
| GET    | `/orders`       | Get all orders              |
| GET    | `/orders/{id}`  | Get order by ID             |
| POST   | `/orders`       | Create an order (with products) |
| PUT    | `/orders/{id}`  | Update an existing order    |
| DELETE | `/orders/{id}`  | Delete an order             |

**Request Body for Creating a Client**
```json
{
    "name": "John Doe",
    "login": "johndoe",
    "email": "johndoe@example.com"
}
```

**Request Body for Creating a Product**
```json
{
  "name": "Laptop",
  "value": 2500.00
}
```

**Request Body for Creating an Order**
```json
{
    "clientId": "76c5fe4f-7eea-462f-9077-ed272b1362ec",
    "productIds": ["79d7d713-6c20-4a11-a883-bd4539d7576c", "ff5e8037-0b2a-4273-a12e-3b09d0802da2"]
}
```

## 🔄 Running the Application with a Specific Database

This project supports **multiple databases** (**MySQL** and **PostgreSQL**) using **Spring Profiles**.

### 📌 How It Works
- The `application.properties` file contains the active profile setting:
  ```properties
  spring.profiles.active=postgres

- The `application-postgres.properties` and `application-mysql.properties` files contain the database-specific configurations, that needs to be updated accordingly:
  ```properties
  spring.application.name=springboot
  spring.datasource.url=jdbc:postgresql://localhost:5432/products-api
  spring.datasource.username=postgres
  spring.datasource.password=root
  spring.jpa.hibernate.ddl-auto=update
  spring.jpa.properties.hibernate.jdbc.lob.non_contextual_creation=true
  spring.jpa.show-sql=true

- Alternatively, the profile can be updated at runtime:
  ```sh
  mvn spring-boot:run "-Dspring-boot.run.profiles=mysql"
