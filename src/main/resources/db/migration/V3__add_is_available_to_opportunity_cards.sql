-- ===========================================
-- V3__add_is_available_to_opportunity_cards.sql
-- ===========================================

ALTER TABLE opportunity_cards
ADD COLUMN is_available BOOLEAN NOT NULL DEFAULT TRUE;