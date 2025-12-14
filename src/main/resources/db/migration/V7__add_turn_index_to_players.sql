-- ===========================================
-- V7__add_turn_index_to_players.sql
-- ===========================================
ALTER TABLE players
ADD COLUMN turn_index INTEGER DEFAULT 0;