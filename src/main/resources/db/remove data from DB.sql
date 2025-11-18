-- Disable checking FK for a moment of cleaning
SET session_replication_role = 'replica';

-- Remove all records
TRUNCATE TABLE
    transactions,
    opportunity_cards,
    liabilities,
    assets,
    players,
    games
RESTART IDENTITY;

-- Enable checking FK
SET session_replication_role = 'origin';
