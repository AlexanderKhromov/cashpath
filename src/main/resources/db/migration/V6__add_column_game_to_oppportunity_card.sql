-- ===========================================
-- V6__add_game_relation_to_opportunity_cards.sql
-- ===========================================

-- 1. Add column game_id (temporarily nullable)
ALTER TABLE opportunity_cards
ADD COLUMN game_id BIGINT;

-- 2. (OPTIONAL, but safe)
-- If there are existing cards, you MUST decide to which game they belong.
-- Example (only if you already have games):
UPDATE opportunity_cards
SET game_id = (SELECT id FROM games LIMIT 1);

-- 3. Add foreign key constraint
ALTER TABLE opportunity_cards
ADD CONSTRAINT fk_opportunity_cards_game
    FOREIGN KEY (game_id)
    REFERENCES games (id)
    ON DELETE CASCADE;

-- 4. Enforce NOT NULL after data is consistent
ALTER TABLE opportunity_cards
ALTER COLUMN game_id SET NOT NULL;

-- 5. Add index for performance
CREATE INDEX idx_opportunity_cards_game_id
    ON opportunity_cards (game_id);