
# Loan Management System (LMS) – Backend

This repository contains a **Java-based backend Loan Management System (LMS)**.

The system supports:

- Loan creation
- EMI schedule generation
- Installment payments (arbitrary amounts)
- Loan detail retrieval
- Loan schedule retrieval with DPD (Days Past Due)

The backend uses:

- Java HTTP Server (`com.sun.net.httpserver.HttpServer`)
- PostgreSQL (via JDBC)
- Plain Java services, repositories, and models
- No frameworks (Spring, Hibernate, etc.)

---

## Features

- Create a loan for a customer
- Calculate EMI and generate monthly installments
- Accept partial, full, and advance payments
- Allocate payments to the oldest unpaid installment first
- Track paid / unpaid / partial installments
- Fetch loan details
- Fetch loan schedule with DPD
- Persist all data in PostgreSQL

---

## Database Setup

Includes 4 major tables
customer_details
installment_details
loan_details
payment_table

### Create database

<img width="2048" height="617" alt="Screenshot 2026-01-17 at 10 53 53 PM" src="https://github.com/user-attachments/assets/570ac879-2056-4755-a1e5-58a9e19659c5" />
<img width="944" height="584" alt="Screenshot 2026-01-17 at 10 54 21 PM" src="https://github.com/user-attachments/assets/6a097642-f5f4-4b4c-9bb0-148ce2f58de1" />


```sql
CREATE DATABASE lms_db;

CREATE SCHEMA lms;

CREATE TABLE customer_details (
    customer_id        BIGSERIAL PRIMARY KEY,
    first_name         VARCHAR(50) NOT NULL,
    last_name          VARCHAR(50) NOT NULL,
    dob                DATE NOT NULL,
    address            TEXT,
    city               VARCHAR(50),
    state              VARCHAR(50),
    pincode            VARCHAR(10),
    mobile             VARCHAR(15) UNIQUE NOT NULL,
    account_no         VARCHAR(30) UNIQUE NOT NULL,
    created_at         TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);


CREATE TABLE loan_details (
    loan_id               BIGSERIAL PRIMARY KEY,
    account_no            VARCHAR(30) NOT NULL,
    sanctioned_amount     NUMERIC(12,2) NOT NULL CHECK (sanctioned_amount > 0),
    loan_period           INTEGER NOT NULL CHECK (loan_period > 0), -- months
    loan_start_date       DATE NOT NULL,
    annual_interest_rate  NUMERIC(5,2) NOT NULL CHECK (annual_interest_rate > 0),
    emi_per_month         NUMERIC(10,2) NOT NULL,
    outstanding_balance   NUMERIC(12,2) NOT NULL,
    customer_id           BIGINT NOT NULL,
    created_at            TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_loan_customer
        FOREIGN KEY (customer_id)
        REFERENCES customer_details(customer_id)
        ON DELETE CASCADE
);

CREATE TABLE installment_details (
    installment_id       BIGSERIAL PRIMARY KEY,
    installment_due_date DATE NOT NULL,
    emi_amount           NUMERIC(10,2) NOT NULL,
    installment_amount   NUMERIC(10,2) NOT NULL,
    installment_status   VARCHAR(20) CHECK (installment_status IN ('paid', 'unpaid', 'overdue')),
    days_past_due        INTEGER DEFAULT 0 CHECK (days_past_due >= 0),
    payment_id           BIGINT,
    loan_id              BIGINT NOT NULL,
    created_at           TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_date         DaTE

    CONSTRAINT fk_installment_payment
        FOREIGN KEY (payment_id)
        REFERENCES payment_table(payment_ref_id)
        ON DELETE SET NULL,

    CONSTRAINT fk_installment_loan
        FOREIGN KEY (loan_id)
        REFERENCES loan_details(loan_id)
        ON DELETE CASCADE
);

CREATE TABLE payment_table (
    payment_ref_id     BIGSERIAL PRIMARY KEY,
    payment_gateway    VARCHAR(50),
    payment_mode       VARCHAR(20) CHECK (payment_mode IN ('upi', 'credit_card', 'debit_card', 'net_banking')),
    payment_amount     NUMERIC(10,2) NOT NULL CHECK (payment_amount > 0),
    payment_status     VARCHAR(20) CHECK (payment_status IN ('success', 'failed', 'pending')),
    payment_timestamp  TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);


```

## JDBC Configuration

```
private static final String URL =
    "jdbc:postgresql://localhost:5432/lms_db";
private static final String USER = "postgres";
private static final String PASSWORD = "your_password";
```

Ensure the PostgreSQL JDBC JAR exists in:

```
lib/postgresql-42.7.9.jar
```

##Compile & Run

```
javac -cp "lib/postgresql-42.7.9.jar" \
server/*.java controller/*.java service/*.java repository/*.java util/*.java model/*.java
java -cp ".:server:controller:service:repository:util:model:lib/postgresql-42.7.9.jar" LmsHttpServer
```
Expected output :

```
🚀 LMS Server running on http://localhost:8080
```

## API Documentation

The LMS backend exposes the following REST-style APIs using Java’s built-in HTTP server.
<img width="356" height="737" alt="Screenshot 2026-01-23 at 12 21 06 PM" src="https://github.com/user-attachments/assets/3b2259b3-809f-44f3-a356-b00752303423" />


### Create Loan

Creates a new loan for a customer and generates the EMI schedule.
{POST}http://localhost:8080/loans

### Make Installment Payment

For making payments to installments.
{POST}http://localhost:8080/payments

### Get Loan Details

Fetch loan details corresponding to a particular customer.
{GET} http://localhost:8080/getloandetails

### Get Loan Schedule

Get the loan schedule.
{GET}http://localhost:8080/getloanSchedule

## Business Rules Implemented

- EMI is calculated using standard reducing balance formula
- Installments are generated monthly from start date
- Payments are allocated to the oldest unpaid installment first
- Partial payments update installment status to PARTIAL
- Full payments update installment status to PAID
- Advance payments can close future EMIs
- DPD (Days Past Due) is calculated as: current_date - installment_due_date

  Allocation Logic:
  <img width="408" height="629" alt="Screenshot 2026-01-17 at 10 54 11 PM" src="https://github.com/user-attachments/assets/e074ddcf-882e-4a4a-b14c-b5e5f1a948cc" />


## Assumptions

- No penalties or late fees
- Arbitrary payment amounts allowed
- Single active loan per loan_id
- No authentication/authorization
- No concurrency handling
- GET requests accept JSON body (for simplicity)
