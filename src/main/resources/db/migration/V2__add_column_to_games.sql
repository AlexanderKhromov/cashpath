-- ===========================================
-- V2__adding column current day to games.sql
-- ===========================================
ALTER TABLE games
ADD COLUMN current_day DATE NOT NULL DEFAULT CURRENT_DATE;