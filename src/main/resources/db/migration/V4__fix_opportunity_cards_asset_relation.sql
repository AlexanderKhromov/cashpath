-- ===========================================
-- V4__fix_opportunity_cards_asset_relation.sql
-- Fix nullable asset_id in opportunity_cards
-- ===========================================

ALTER TABLE opportunity_cards
DROP CONSTRAINT IF EXISTS fk_opportunity_asset;

ALTER TABLE opportunity_cards
ALTER COLUMN asset_id DROP NOT NULL;

ALTER TABLE opportunity_cards
ADD CONSTRAINT fk_opportunity_asset
    FOREIGN KEY (asset_id)
    REFERENCES assets (id)
    ON DELETE SET NULL;