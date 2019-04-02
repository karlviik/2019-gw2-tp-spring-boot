--liquibase formatted sql
--changeset db:1
CREATE TABLE item(
  id INTEGER NOT NULL,
  name TEXT NOT NULL,
  chat_link TEXT NOT NULL,
  icon_id INTEGER, --think it's not null, not sure
  rarity TEXT NOT NULL,
  level SMALLINT,  --not null?
  bound BOOLEAN NOT NULL,
  vendor_value INTEGER,  --can be null
  type TEXT NOT NULL,
  sub_type TEXT,  --duno about nullability
  sub_sub_type TEXT,  -- duno about nullability
  UNIQUE (id)
);

CREATE TABLE item_upgrade(
  item_id INTEGER NOT NULL,
  upgrade_item_id INTEGER NOT NULL,
  UNIQUE (item_id, upgrade_item_id)
);

CREATE TABLE item_infusion(
  item_id INTEGER NOT NULL,
  infusion_item_id INTEGER NOT NULL,
  UNIQUE (item_id, infusion_item_id)
);

CREATE TABLE recipe(
  id INTEGER NOT NULL,
  updated_at TIMESTAMP,
  type TEXT NOT NULL,
  min_rating SMALLINT NOT NULL,
  learned_from_item BOOLEAN NOT NULL,
  chat_link TEXT NOT NULL,
  out_item_id INTEGER NOT NULL,
  out_item_count SMALLINT NOT NULL
);
CREATE UNIQUE INDEX recipe_no_overwrite ON recipe (id, updated_at) WHERE updated_at IS NOT NULL;
CREATE UNIQUE INDEX recipe_overwrite ON recipe (id) WHERE updated_at IS NULL;

CREATE TABLE recipe_discipline(
  recipe_id INTEGER NOT NULL,
  discipline TEXT NOT NULL,
  UNIQUE(recipe_id, discipline)
);

CREATE TABLE recipe_component(
  recipe_id INTEGER NOT NULL,
  updated_at TIMESTAMP,
  component_item_id INTEGER NOT NULL,
  component_item_count SMALLINT NOT NULL
);
CREATE UNIQUE INDEX recipe_component_no_overwrite ON recipe_component (recipe_id, updated_at, component_item_id, component_item_count) WHERE updated_at IS NOT NULL;
CREATE UNIQUE INDEX recipe_component_overwrite ON recipe_component (recipe_id, component_item_id, component_item_count) WHERE updated_at IS NULL;

CREATE TABLE price(
  item_id INTEGER NOT NULL,
  created_at TIMESTAMP NOT NULL,
  buy_price INTEGER,
  buy_quantity INTEGER,
  sell_price INTEGER,
  sell_quantity INTEGER,
  craft_buy_price INTEGER,
  craft_sell_price INTEGER,
  open_buy_price INTEGER,
  open_sell_price INTEGER,
  mystic_forge_buy_price INTEGER,
  mystic_forge_sell_price INTEGER,
  UNIQUE(item_id, created_at)
);

CREATE TABLE game_version(
  version INTEGER NOT NULL
);

INSERT INTO game_version(version) VALUES(0);
