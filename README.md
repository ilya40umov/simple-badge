# Simple Badge
This is a very basic implementation of a badge service. 
The key functionality is operations with account(e.g. sign up, granting privileges), 
creating/modifying badges(by authorized users), and
giving badges to users(as well as taking them away).
The project is meant to showcase how to develop a modern, DB-centric, REST-based web
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