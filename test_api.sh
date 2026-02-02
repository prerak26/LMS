#!/bin/bash

# LMS API Test Script
# Run this after starting the server

BASE_URL="http://localhost:8080"

echo "🧪 Testing LMS API Endpoints..."
echo "================================"

# Test 1: Get Dashboard
echo ""
echo "1️⃣ Testing Dashboard Endpoint..."
curl -s $BASE_URL/dashboard | python3 -m json.tool 2>/dev/null || curl -s $BASE_URL/dashboard

# Test 2: Get All Customers
echo ""
echo ""
echo "2️⃣ Testing Get All Customers..."
curl -s $BASE_URL/customers | python3 -m json.tool 2>/dev/null || curl -s $BASE_URL/customers

# Test 3: Get Customer by ID
echo ""
echo ""
echo "3️⃣ Testing Get Customer by ID (ID=1)..."
curl -s $BASE_URL/customers/1 | python3 -m json.tool 2>/dev/null || curl -s $BASE_URL/customers/1

# Test 4: Get All Loans
echo ""
echo ""
echo "4️⃣ Testing Get All Loans..."
curl -s $BASE_URL/loans/all | python3 -m json.tool 2>/dev/null || curl -s $BASE_URL/loans/all

# Test 5: Get Loans by Customer
echo ""
echo ""
echo "5️⃣ Testing Get Loans by Customer (Customer ID=1)..."
curl -s $BASE_URL/loans/customer/1 | python3 -m json.tool 2>/dev/null || curl -s $BASE_URL/loans/customer/1

# Test 6: Get All Payments
echo ""
echo ""
echo "6️⃣ Testing Get All Payments..."
curl -s $BASE_URL/payments/all | python3 -m json.tool 2>/dev/null || curl -s $BASE_URL/payments/all

echo ""
echo ""
echo "✅ All tests completed!"
echo ""
echo "To create a new customer:"
echo 'curl -X POST http://localhost:8080/customers -H "Content-Type: application/json" -d '"'"'{"first_name":"Test","last_name":"User","dob":"1990-01-01","address":"Test Address","city":"TestCity","state":"TestState","pincode":"123456","mobile":"9999999999","account_no":"ACC999"}'"'"
echo ""
echo "To create a new loan:"
echo 'curl -X POST http://localhost:8080/loans -H "Content-Type: application/json" -d '"'"'{"cust_id":1,"amount":100000,"start_date":"2026-02-01","rate":12.5,"tenure":12}'"'"
