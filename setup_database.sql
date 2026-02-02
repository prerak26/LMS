-- ============================================
-- LMS Database Setup Script
-- ============================================
-- This script will:
-- 1. Create lms_user with password
-- 2. Create lms schema
-- 3. Move existing tables to lms schema
-- 4. Grant permissions to lms_user
-- ============================================

-- Step 1: Create the lms_user
CREATE USER lms_user WITH PASSWORD 'lms_pass123';

-- Step 2: Grant privileges on the database
GRANT ALL PRIVILEGES ON DATABASE lms_db TO lms_user;

-- Step 3: Create lms schema
CREATE SCHEMA IF NOT EXISTS lms;

-- Step 4: Grant usage on lms schema to lms_user
GRANT ALL ON SCHEMA lms TO lms_user;
GRANT ALL PRIVILEGES ON ALL TABLES IN SCHEMA lms TO lms_user;
GRANT ALL PRIVILEGES ON ALL SEQUENCES IN SCHEMA lms TO lms_user;

-- Step 5: Move existing tables from public to lms schema
ALTER TABLE public.payment_table SET SCHEMA lms;
ALTER TABLE public.customer_details SET SCHEMA lms;
ALTER TABLE public.loan_details SET SCHEMA lms;
ALTER TABLE public.installment_details SET SCHEMA lms;

-- Step 6: Grant permissions on moved tables
GRANT ALL PRIVILEGES ON ALL TABLES IN SCHEMA lms TO lms_user;
GRANT ALL PRIVILEGES ON ALL SEQUENCES IN SCHEMA lms TO lms_user;

-- Step 7: Set default privileges for future objects
ALTER DEFAULT PRIVILEGES IN SCHEMA lms GRANT ALL ON TABLES TO lms_user;
ALTER DEFAULT PRIVILEGES IN SCHEMA lms GRANT ALL ON SEQUENCES TO lms_user;

-- Verify the setup
SELECT schemaname, tablename
FROM pg_tables
WHERE schemaname = 'lms';
