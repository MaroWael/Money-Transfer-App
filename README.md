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
```
# Create a README file for the provided OpenAPI specification in text format.

openapi_spec = """
# Transfer Service API Documentation

## API Version: 1.0.0

### Base URL: https://sha256-1f39a1226a97.onrender.com

### Overview
This is a sample Transfer Service API that allows customers to manage their accounts, conduct money transfers, and manage favorite recipients. Authentication is done via JWT tokens.

### Endpoints

---

#### Customer Controller

1. **Change Password**  
   `PUT /api/v1/customer/{customerId}/change-password`  
   Allows a customer to change their password.
   - Parameters:  
     - `customerId` (path) - integer, required  
   - Request Body:  
     - `ChangePasswordDTO`  
       - `oldPassword` (string) - required  
       - `newPassword` (string) - required  
   - Responses:  
     - `200 OK`
     - `400 Bad Request`

2. **Update Customer Profile**  
   `PUT /api/v1/customer/update/{customerId}`  
   Allows a customer to update their profile.
   - Parameters:  
     - `customerId` (path) - integer, required  
   - Request Body:  
     - `UpdateCustomerDTO`  
       - `name` (string)  
       - `email` (string)  
       - `country` (string)  
       - `dateOfBirth` (string) - format: date  
   - Responses:  
     - `200 OK`
     - `400 Bad Request`

3. **Get Customer by ID**  
   `GET /api/v1/customer/{customerId}`  
   Retrieves the customer details by ID.
   - Parameters:  
     - `customerId` (path) - integer, required  
   - Responses:  
     - `200 OK`
     - `400 Bad Request`

---

#### Account Controller

1. **Deposit Money**  
   `PUT /api/v1/account/{accountId}/deposit`  
   Deposit money into an account.
   - Parameters:  
     - `accountId` (path) - integer, required  
   - Request Body:  
     - `DepositRequestDTO`  
       - `amount` (number) - required  
   - Responses:  
     - `200 OK`
     - `400 Bad Request`

2. **Get Account by ID**  
   `GET /api/v1/account/{accountId}`  
   Retrieves account details by ID.
   - Parameters:  
     - `accountId` (path) - integer, required  
   - Responses:  
     - `200 OK`
     - `400 Bad Request`

3. **Get Balance by Account ID**  
   `GET /api/v1/account/balance/{accountId}`  
   Retrieves the balance for a specific account ID.
   - Parameters:  
     - `accountId` (path) - integer, required  
   - Responses:  
     - `200 OK`
     - `400 Bad Request`

---

#### Transaction Controller

1. **Transfer Money Between Accounts**  
   `POST /api/v1/transactions/transfer`  
   Transfers money between accounts.
   - Request Body:  
     - `TransactionRequestDTO`  
       - `toAccountNumber` (string) - required  
       - `amount` (number) - required  
       - `recipientName` (string) - required  
   - Responses:  
     - `200 OK`
     - `400 Bad Request`

2. **Get Transaction History by Account ID**  
   `GET /api/v1/transactions/history/{accountId}`  
   Retrieves the transaction history for a specific account ID.
   - Parameters:  
     - `accountId` (path) - integer, required  
   - Responses:  
     - `200 OK`
     - `400 Bad Request`

---

#### Favorite Recipient Controller

1. **Add a Favorite Recipient**  
   `POST /api/v1/favorites/add`  
   Adds a favorite recipient to the customer's account.
   - Request Body:  
     - `FavoriteRecipientDTO`  
       - `recipientName` (string)  
       - `recipientAccountNumber` (string)  
   - Responses:  
     - `200 OK` - Favorite recipient added successfully  
     - `400 Bad Request` - Duplicate favorite recipient  

2. **Get All Favorite Recipients**  
   `GET /api/v1/favorites`  
   Retrieves a list of all favorite recipients.
   - Responses:  
     - `200 OK`

3. **Delete a Favorite Recipient**  
   `DELETE /api/v1/favorites/delete`  
   Deletes a favorite recipient.
   - Request Body:  
     - `DeleteFavoriteRecipientRequestDTO`  
       - `recipientAccountNumber` (string)  
   - Responses:  
     - `200 OK` - Favorite recipient deleted successfully  
     - `400 Bad Request` - Favorite recipient not found  

---

#### Customer Authentication Controller

1. **Register New Customer**  
   `POST /api/v1/auth/register`  
   Registers a new customer.
   - Request Body:  
     - `RegisterCustomerRequest`  
       - `name` (string) - required  
       - `country` (string) - required  
       - `email` (string) - required  
       - `password` (string) - minLength: 6, required  
       - `dateOfBirth` (string) - format: date, required  
   - Responses:  
     - `200 OK`
     - `400 Bad Request`

2. **Login and Generate JWT**  
   `POST /api/v1/auth/login`  
   Logs in the customer and generates a JWT token.
   - Request Body:  
     - `LoginRequestDTO`  
       - `email` (string) - required  
       - `password` (string) - required  
   - Responses:  
     - `200 OK`
     - `401 Unauthorized`

3. **Logout Customer**  
   `POST /api/v1/auth/logout`  
   Logs out the customer by invalidating their JWT token.
   - Responses:  
     - `200 OK`
     - `400 Bad Request`

---

### Components

#### Schemas

1. **ChangePasswordDTO**  
   - `oldPassword` (string) - required  
   - `newPassword` (string) - required  

2. **UpdateCustomerDTO**  
   - `name` (string)  
   - `email` (string)  
   - `country` (string)  
   - `dateOfBirth` (string) - format: date  

3. **DepositRequestDTO**  
   - `amount` (number) - required  

4. **TransactionRequestDTO**  
   - `toAccountNumber` (string) - required  
   - `amount` (number) - required  
   - `recipientName` (string) - required  

5. **TransactionResponseDTO**  
   - `fromAccountNumber` (string)  
   - `toAccountNumber` (string)  
   - `fromAccountName` (string)  
   - `toAccountName` (string)  
   - `amount` (number)  
   - `transactionDate` (string)  

6. **FavoriteRecipientDTO**  
   - `recipientName` (string)  
   - `recipientAccountNumber` (string)  

7. **DeleteFavoriteRecipientRequestDTO**  
   - `recipientAccountNumber` (string)  

8. **RegisterCustomerRequest**  
   - `name` (string) - required  
   - `country` (string) - required  
   - `email` (string) - required  
   - `password` (string) - minLength: 6, required  
   - `dateOfBirth` (string) - format: date, required  

9. **RegisterCustomerResponse**  
   - `id` (integer)  
   - `name` (string)  
   - `email` (string)  
   - `country` (string)  
   - `createdAt` (string)  
   - `updatedAt` (string)  

10. **LoginRequestDTO**  
   - `email` (string) - required  
   - `password` (string) - required  

11. **LoginResponseDTO**  
   - `id` (integer)  
   - `token` (string)  
   - `tokenType` (string)  
   - `message` (string)  
   - `status` (string)  

12. **ErrorDetails**  
   - `timestamp` (string) - format: date-time  
   - `message` (string)  
   - `details` (string)  
   - `httpStatus` (string)  

### Security
This API uses bearer token authentication (JWT).
