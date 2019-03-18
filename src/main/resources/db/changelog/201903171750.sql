--liquibase formatted sql
--changeset db:1
CREATE TABLE items(
  id INTEGER,
  name TEXT,
  chatLink TEXT,
  iconId INTEGER,
  rarity TEXT,
  level SMALLINT,
  bound BOOLEAN,
  vendorValue INTEGER,
  type TEXT,
  type2 TEXT,
  type3 TEXT,
  subItem INTEGER,
  subItem2 INTEGER,
  subInfusion INTEGER,
  subInfusion2 INTEGER,
  subInfusion3 INTEGER,
  UNIQUE (id)
);

CREATE TABLE recipe_disciplines(
  id INTEGER,
  discipline TEXT,
  UNIQUE(id, discipline)
);

CREATE TABLE recipes(
  id INTEGER,
  overwrite_time TIMESTAMP,
  type TEXT,
  min_rating SMALLINT,
  learned_from_item BOOLEAN,
  chat_link TEXT,
  out_id INTEGER,
  out_count SMALLINT,
  in_id_1 INTEGER,
  in_count_1 SMALLINT,
  in_id_2 INTEGER,
  in_count_2 SMALLINT,
  in_id_3 INTEGER,
  in_count_3 SMALLINT,
  in_id_4 INTEGER,
  in_count_4 SMALLINT
);
CREATE UNIQUE INDEX recipes_no_overwrite ON recipes (id, overwriteTime) WHERE overwriteTime IS NOT NULL;
CREATE UNIQUE INDEX recipes_overwrite ON recipes (id) WHERE overwriteTime IS NULL;

CREATE TABLE prices(
  id INTEGER,
  time TIMESTAMP,
  buyPrice INTEGER,
  buyQuantity INTEGER,
  sellPrice INTEGER,
  sellQuantity INTEGER,
  buyCraft INTEGER,
  sellCraft INTEGER,
  buyOpen INTEGER,
  sellOpen INTEGER,
  buyMysticForge INTEGER,
  sellMysticForge INTEGER,
  salvage1Id SMALLINT,
  salvage1Buy INTEGER,
  salvage1Sell INTEGER,
  salvage2Id SMALLINT,
  salvage2Buy INTEGER,
  salvage2Sell INTEGER,
  UNIQUE(id, time)
);
