-- ===========================================
-- V5 Switch to daily calculations and remove transactions table
-- ===========================================

-- 1. Drop table transactions if exists
DROP TABLE IF EXISTS transactions;

-- 2. games: replace current_day DATE → INT
-- Firstly drop old column
ALTER TABLE games
    DROP COLUMN IF EXISTS current_day;

-- then create new column current_day as INT
ALTER TABLE games
    ADD COLUMN current_day INT NOT NULL DEFAULT 1;

-- 3. assets: monthly_cash_flow → daily_cash_flow
-- rename column
ALTER TABLE assets
    RENAME COLUMN monthly_cash_flow TO daily_cash_flow;

-- 4. liabilities: monthly_payment → daily_payment
ALTER TABLE liabilities
    RENAME COLUMN monthly_payment TO daily_payment;

-- 5. players: monthly_expenses → daily_expenses
ALTER TABLE players
    RENAME COLUMN monthly_expenses TO daily_expenses;