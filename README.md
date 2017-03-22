# simple-badge
A very simple implementation of a badge service. 
The key functionality is operations with account(e.g. sign up), 
creating/modifying badges(by authorized users), and
giving badges to users.
The project is mean to showcase how to develop a normal web
application with Spring.

## Technologies

This project is implemented using Spring Boot and specifically the following stack:
1. **JPA / Hibernate** (for accessing DB)
2. **Spring Data** (for Repositories / DAO)
3. **Spring MVC** (for REST controllers)
4. **Spring Security** (for authentication and access control)
5. **MySQL** as a target DB and **H2** as a test DB
6. **MapStruct** as a mapper between entities and DTOs
7. **Thymeleaf** (a template engine for rendering HTML)