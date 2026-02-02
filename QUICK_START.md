# 🚀 Quick Start Guide

## Start Your Enhanced LMS in 3 Steps

### Step 1: Compile
```bash
cd /Users/prerak/Documents/friends/LMS
javac -cp "lib/postgresql-42.7.9.jar" \
  server/*.java controller/*.java service/*.java repository/*.java util/*.java model/*.java
```

### Step 2: Run Server
```bash
java -cp ".:server:controller:service:repository:util:model:lib/postgresql-42.7.9.jar" LmsHttpServer
```

You should see:
```
Postgres JDBC driver loaded
🚀 LMS Server running on http://localhost:8080
📋 Available Endpoints:
   POST   /loans - Create a loan
   GET    /loans/all - Get all loans
   GET    /loans/customer/{id} - Get loans by customer
   ...
```

### Step 3: Test APIs
```bash
# In a new terminal
./test_api.sh
```

---

## 🎯 Try These Now!

### Check Dashboard
```bash
curl http://localhost:8080/dashboard
```

### List All Customers
```bash
curl http://localhost:8080/customers
```

### List All Loans
```bash
curl http://localhost:8080/loans/all
```

### Create a New Customer
```bash
curl -X POST http://localhost:8080/customers \
  -H "Content-Type: application/json" \
  -d '{
    "first_name": "Test",
    "last_name": "User",
    "dob": "1990-01-01",
    "address": "123 Test St",
    "city": "TestCity",
    "state": "TestState",
    "pincode": "123456",
    "mobile": "9999999999",
    "account_no": "ACCTEST"
  }'
```

---

## 📚 Documentation

- **Full API Documentation**: See `API_DOCUMENTATION.md`
- **Enhancement Summary**: See `ENHANCEMENT_SUMMARY.md`
- **Original README**: See `README.md`

---

## 🎉 You Now Have:

✅ **13 API Endpoints** (Customer, Loan, Payment, Dashboard)
✅ **Complete CRUD Operations**
✅ **Dashboard Analytics**
✅ **Payment History**
✅ **Comprehensive Documentation**

**Happy Coding! 🚀**
