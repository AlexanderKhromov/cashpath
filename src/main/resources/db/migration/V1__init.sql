-- ===========================================
-- V1__init.sql
-- Initialization for project CashPath
-- ===========================================
CREATE SCHEMA IF NOT EXISTS public;
SET search_path TO public;

CREATE TABLE games (
    id BIGSERIAL PRIMARY KEY,
    current_turn INT NOT NULL DEFAULT 0,
    status VARCHAR(50) NOT NULL
);

CREATE TABLE players (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    cash DOUBLE PRECISION NOT NULL DEFAULT 0,
    salary DOUBLE PRECISION NOT NULL DEFAULT 0,
    monthly_expenses DOUBLE PRECISION NOT NULL DEFAULT 0,
    on_fast_track BOOLEAN NOT NULL DEFAULT FALSE,
    game_id BIGINT NOT NULL,

    CONSTRAINT fk_players_game
        FOREIGN KEY (game_id)
        REFERENCES games (id)
        ON DELETE CASCADE
);

CREATE INDEX idx_players_game_id ON players (game_id);

CREATE TABLE assets (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    price DOUBLE PRECISION NOT NULL,
    monthly_cash_flow DOUBLE PRECISION NOT NULL DEFAULT 0,
    type VARCHAR(50) NOT NULL,
    owner_id BIGINT,

    CONSTRAINT fk_assets_owner
        FOREIGN KEY (owner_id)
        REFERENCES players (id)
        ON DELETE CASCADE
);

CREATE INDEX idx_assets_owner_id ON assets (owner_id);

CREATE TABLE liabilities (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    monthly_payment DOUBLE PRECISION NOT NULL,
    owner_id BIGINT,

    CONSTRAINT fk_liabilities_owner
        FOREIGN KEY (owner_id)
        REFERENCES players (id)
        ON DELETE CASCADE
);

CREATE INDEX idx_liabilities_owner_id ON liabilities (owner_id);

CREATE TABLE opportunity_cards (
    id BIGSERIAL PRIMARY KEY,
    description TEXT NOT NULL,
    type VARCHAR(50) NOT NULL,
    amount DOUBLE PRECISION NOT NULL DEFAULT 0,
    asset_id BIGINT,

    CONSTRAINT fk_opportunity_asset
        FOREIGN KEY (asset_id)
        REFERENCES assets (id)
        ON DELETE SET NULL
);

CREATE INDEX idx_opportunity_asset_id ON opportunity_cards (asset_id);

CREATE TABLE transactions (
    id BIGSERIAL PRIMARY KEY,
    from_player_id BIGINT,
    to_player_id BIGINT,
    amount DOUBLE PRECISION NOT NULL,
    type VARCHAR(50) NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT NOW(),

    CONSTRAINT fk_transaction_from_player
        FOREIGN KEY (from_player_id)
        REFERENCES players (id)
        ON DELETE SET NULL,

    CONSTRAINT fk_transaction_to_player
        FOREIGN KEY (to_player_id)
        REFERENCES players (id)
        ON DELETE SET NULL
);

CREATE INDEX idx_transactions_from_player_id ON transactions (from_player_id);
CREATE INDEX idx_transactions_to_player_id ON transactions (to_player_id);