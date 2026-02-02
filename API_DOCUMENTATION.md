# LMS API Documentation - Enhanced Endpoints

## 🚀 Getting Started

**Base URL:** `http://localhost:8080`

**Server Start:**
```bash
java -cp ".:server:controller:service:repository:util:model:lib/postgresql-42.7.9.jar" LmsHttpServer
```

---

## 📋 API Endpoints

### **1. Customer Management**

#### Create Customer
**POST** `/customers`

Creates a new customer in the system.

**Request Body:**
```json
{
  "first_name": "John",
  "last_name": "Doe",
  "dob": "1990-01-15",
  "address": "123 Main St",
  "city": "Mumbai",
  "state": "Maharashtra",
  "pincode": "400001",
  "mobile": "9876543210",
  "account_no": "ACC001"
}
```

**Response (201):**
```json
{
  "customer_id": 1,
  "message": "Customer created successfully"
}
```

**cURL Example:**
```bash
curl -X POST http://localhost:8080/customers \
  -H "Content-Type: application/json" \
  -d '{
    "first_name": "Jane",
    "last_name": "Smith",
    "dob": "1995-05-20",
    "address": "456 Oak St",
    "city": "Delhi",
    "state": "Delhi",
    "pincode": "110001",
    "mobile": "9876543211",
    "account_no": "ACC002"
  }'
```

---

#### Get All Customers
**GET** `/customers`

Retrieves all customers.

**Response (200):**
```json
[
  {
    "customer_id": 1,
    "first_name": "John",
    "last_name": "Doe",
    "mobile": "9876543210",
    "account_no": "ACC001"
  },
  {
    "customer_id": 2,
    "first_name": "Jane",
    "last_name": "Smith",
    "mobile": "9876543211",
    "account_no": "ACC002"
  }
]
```

**cURL Example:**
```bash
curl http://localhost:8080/customers
```

---

#### Get Customer by ID
**GET** `/customers/{id}`

Retrieves a specific customer by ID.

**Response (200):**
```json
{
  "customer_id": 1,
  "first_name": "John",
  "last_name": "Doe",
  "dob": "1990-01-15",
  "address": "123 Main St",
  "city": "Mumbai",
  "state": "Maharashtra",
  "pincode": "400001",
  "mobile": "9876543210",
  "account_no": "ACC001"
}
```

**cURL Example:**
```bash
curl http://localhost:8080/customers/1
```

---

### **2. Loan Management**

#### Create Loan
**POST** `/loans`

Creates a new loan for a customer.

**Request Body:**
```json
{
  "cust_id": 1,
  "amount": 100000,
  "start_date": "2026-02-01",
  "rate": 12.5,
  "tenure": 12
}
```

**Response (200):**
```json
{
  "loan_id": "1"
}
```

**cURL Example:**
```bash
curl -X POST http://localhost:8080/loans \
  -H "Content-Type: application/json" \
  -d '{
    "cust_id": 1,
    "amount": 100000,
    "start_date": "2026-02-01",
    "rate": 12.5,
    "tenure": 12
  }'
```

---

#### Get All Loans
**GET** `/loans/all`

Retrieves all loans with summary information.

**Response (200):**
```json
[
  {
    "loan_id": 1,
    "account_no": "LN123456",
    "customer_name": "John Doe",
    "sanctioned_amount": 100000.0,
    "outstanding_balance": 85000.0,
    "emi": 8884.88,
    "tenure": 12,
    "start_date": "2026-02-01",
    "paid_installments": 2,
    "total_installments": 12
  }
]
```

**cURL Example:**
```bash
curl http://localhost:8080/loans/all
```

---

#### Get Loans by Customer
**GET** `/loans/customer/{customer_id}`

Retrieves all loans for a specific customer.

**Response (200):**
```json
[
  {
    "loan_id": 1,
    "account_no": "LN123456",
    "customer_name": "John Doe",
    "sanctioned_amount": 100000.0,
    "outstanding_balance": 85000.0,
    "emi": 8884.88,
    "tenure": 12,
    "start_date": "2026-02-01",
    "paid_installments": 2,
    "total_installments": 12
  }
]
```

**cURL Example:**
```bash
curl http://localhost:8080/loans/customer/1
```

---

#### Get Loan Details
**GET** `/getloandetails`

Retrieves detailed information for a specific loan.

**Request Body (JSON):**
```json
{
  "cust_id": 1,
  "loan_id": 1
}
```

**Response (200):**
```json
{
  "loan_id": 1,
  "account_no": "LN123456",
  "customer_id": 1,
  "sanctioned_amount": 100000.0,
  "interest_rate": 12.5,
  "tenure": 12,
  "start_date": "2026-02-01",
  "emi": 8884.88,
  "outstanding_balance": 85000.0
}
```

**cURL Example:**
```bash
curl http://localhost:8080/getloandetails \
  -H "Content-Type: application/json" \
  -d '{"cust_id": 1, "loan_id": 1}'
```

---

#### Get Loan Schedule
**GET** `/getloanSchedule`

Retrieves the installment schedule for a loan.

**Request Body (JSON):**
```json
{
  "cust_id": 1,
  "loan_id": 1
}
```

**Response (200):**
```json
[
  {
    "due_date": "2026-03-01",
    "days_past_due": 0,
    "status": "unpaid",
    "amount": 8884.88
  },
  {
    "due_date": "2026-04-01",
    "days_past_due": 0,
    "status": "unpaid",
    "amount": 8884.88
  }
]
```

**cURL Example:**
```bash
curl http://localhost:8080/getloanSchedule \
  -H "Content-Type: application/json" \
  -d '{"cust_id": 1, "loan_id": 1}'
```

---

### **3. Payment Management**

#### Make Payment
**POST** `/payments`

Makes a payment towards a loan installment.

**Request Body:**
```json
{
  "loan_id": 1,
  "payment_mode": "upi",
  "amount": 8884.88,
  "timestamp": "2026-02-15T10:30:00"
}
```

**Response (200):**
```json
{
  "payment_ref_id": "12345"
}
```

**cURL Example:**
```bash
curl -X POST http://localhost:8080/payments \
  -H "Content-Type: application/json" \
  -d '{
    "loan_id": 1,
    "payment_mode": "upi",
    "amount": 8884.88,
    "timestamp": "2026-02-15T10:30:00"
  }'
```

---

#### Get All Payments
**GET** `/payments/all`

Retrieves all payment transactions.

**Response (200):**
```json
[
  {
    "payment_ref_id": 1,
    "loan_id": 1,
    "payment_mode": "upi",
    "payment_amount": 8884.88,
    "payment_status": "success",
    "payment_timestamp": "2026-02-15T10:30:00"
  }
]
```

**cURL Example:**
```bash
curl http://localhost:8080/payments/all
```

---

#### Get Payment History by Loan
**GET** `/payments/loan/{loan_id}`

Retrieves payment history for a specific loan.

**Response (200):**
```json
[
  {
    "payment_ref_id": 1,
    "loan_id": 1,
    "payment_mode": "upi",
    "payment_amount": 8884.88,
    "payment_status": "success",
    "payment_timestamp": "2026-02-15T10:30:00"
  }
]
```

**cURL Example:**
```bash
curl http://localhost:8080/payments/loan/1
```

---

### **4. Dashboard & Analytics**

#### Get Dashboard Statistics
**GET** `/dashboard`

Retrieves system-wide statistics and analytics.

**Response (200):**
```json
{
  "total_customers": 10,
  "total_loans": 25,
  "active_loans": 18,
  "total_disbursed": 2500000.0,
  "total_outstanding": 1750000.0,
  "total_collected": 750000.0,
  "overdue_loans": 3
}
```

**cURL Example:**
```bash
curl http://localhost:8080/dashboard
```

---

## 📊 Usage Examples

### Complete Workflow Example

```bash
# 1. Create a customer
curl -X POST http://localhost:8080/customers \
  -H "Content-Type: application/json" \
  -d '{
    "first_name": "Alice",
    "last_name": "Johnson",
    "dob": "1992-08-10",
    "address": "789 Pine Ave",
    "city": "Bangalore",
    "state": "Karnataka",
    "pincode": "560001",
    "mobile": "9876543212",
    "account_no": "ACC003"
  }'

# 2. Create a loan for the customer
curl -X POST http://localhost:8080/loans \
  -H "Content-Type: application/json" \
  -d '{
    "cust_id": 3,
    "amount": 200000,
    "start_date": "2026-02-01",
    "rate": 10.5,
    "tenure": 24
  }'

# 3. View all loans
curl http://localhost:8080/loans/all

# 4. Make a payment
curl -X POST http://localhost:8080/payments \
  -H "Content-Type: application/json" \
  -d '{
    "loan_id": 2,
    "payment_mode": "credit_card",
    "amount": 10000,
    "timestamp": "2026-03-01T14:00:00"
  }'

# 5. Check dashboard
curl http://localhost:8080/dashboard
```

---

## 🔧 Error Responses

All endpoints return error responses in the following format:

**Error Response (500):**
```json
{
  "error": "Error message describing what went wrong"
}
```

**Error Response (404):**
```
Not Found - Endpoint or resource does not exist
```

---

## 📝 Notes

- All dates should be in format: `YYYY-MM-DD`
- All timestamps should be in format: `YYYY-MM-DDTHH:MM:SS`
- Payment modes: `upi`, `credit_card`, `debit_card`, `net_banking`
- All amounts are in INR (Indian Rupees)
- EMI is auto-calculated based on amount, rate, and tenure
- Account numbers are auto-generated for loans

---

## 🎯 Quick Test Commands

Test all endpoints quickly:

```bash
# Get dashboard
curl http://localhost:8080/dashboard

# List all customers
curl http://localhost:8080/customers

# List all loans
curl http://localhost:8080/loans/all

# List all payments
curl http://localhost:8080/payments/all
```
