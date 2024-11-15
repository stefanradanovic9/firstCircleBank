# Basic Banking Service
This project is a simple banking service developed in Java Spring Boot that simulates basic banking operations such as account creation, deposits, withdrawals, and transfers between accounts. The system uses an in-memory H2 database to store accounts and transaction records. For simplicity, this implementation does not include authentication or authorization, though these should be added for real-world applications.

## Features
* Account Creation: Users can create accounts with an initial deposit amount.
* Deposit: Asynchronous deposits can be made to an account, updating the balance and transaction status.
* Withdrawal: Asynchronous withdrawals are supported, with safeguards to prevent overdrafts.
* Account Balance Check: Provides the current balance of an account.
* Transfer: Allows funds to be transferred between accounts.
## Technologies Used
* Java 17
* Spring Boot - for creating the service and managing dependencies.
* Spring Data JPA - for data persistence.
* H2 Database - in-memory database for simplicity.
* Async Operations - Spring's @Async for handling deposits and withdrawals asynchronously.
* Optimistic Locking - Ensures data consistency during concurrent balance updates.

## Project Structure
* AccountService: Handles account creation and balance management.
* TransactionService: Manages asynchronous deposits and withdrawals with transaction status tracking.
* Transaction Entity: Represents transactions, including type (deposit/withdrawal), timestamp, and status (PENDING, SUCCESS, FAILED).
* Account Entity: Represents bank accounts with fields for balance and account holder information. 

## Areas for Improvement
### Authentication and Authorization:

Currently, no authentication or authorization is implemented. A production-ready application should integrate with an authentication provider (e.g., Spring Security, OAuth2) to secure endpoints and ensure that only authorized users can access or modify account information.

### Concurrency and Locking:

The system uses optimistic locking to handle concurrent balance updates. In high-concurrency scenarios, pessimistic locking could provide stronger consistency at the cost of potential performance impacts.

### Error Handling and Logging:

Additional error handling and logging mechanisms could enhance the application's robustness. For example, logging transaction attempts and failures for later analysis.

### Database Persistence:

The current implementation uses an in-memory H2 database, which does not persist data after the application shuts down. For production, a persistent database (e.g., PostgreSQL, MySQL) should be configured.

### Transaction Rollbacks:

In some cases, failed transactions could be improved by implementing compensating transactions or rollback mechanisms for partially completed operations.

### Unit and Integration Testing:

While basic tests may be in place, comprehensive unit and integration tests should be added to ensure the system's reliability under various scenarios, such as concurrent transactions, invalid data, and system failures.
