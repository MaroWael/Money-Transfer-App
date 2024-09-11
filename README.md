Money Transfer API

## Overview

This project is a **Spring Boot application** that provides RESTful APIs for handling money transfers between accounts. It is part of a personal finance management system that allows users to transfer funds, view transaction history, and manage their accounts.

The core features of this application include:
- Transferring money between accounts.
- Viewing transaction history by account.
- User authentication for account operations.

## Features

- **Money Transfer:** Users can transfer money from their account to another account by specifying the recipient's account number and name.
- **Transaction History:** Users can view their transaction history by account ID.
- **Authentication:** The application uses Spring Security for user authentication, ensuring that only authorized users can make transfers from their accounts.

## Technologies Used

- **Spring Boot:** Core framework for building REST APIs.
- **Spring Security:** For authentication and securing endpoints.
- **JWT (JSON Web Token):** For token-based authentication.
- **Hibernate / JPA:** ORM framework for database interaction.
- **PostgreSQL:** Relational database used for storing account and transaction data.
- **Lombok:** For reducing boilerplate code.
- **JUnit 5 & Mockito:** For writing unit tests.

## ER Diagram
<img src="./erd_project.jpg" alt="Alt text" width="500"/>

## Project Structure

```bash
src/
├── main/
│   ├── java/
│   │   └── com/
│   │       └── transfer/
│   │           ├── controller/     # REST controllers
│   │           ├── dto/            # Data Transfer Objects
│   │           ├── entity/         # JPA Entities
│   │           ├── exception/      # Custom exception handling
│   │           ├── repository/     # JPA Repositories
│   │           └── service/        # Business logic services
│   └── resources/
│       ├── application.properties  # Spring Boot configurations
└── test/                           # Unit tests
