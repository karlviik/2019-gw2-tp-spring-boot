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
    subInfusion3 INTEGER
);

CREATE TABLE recipe_disciplines(
    id INTEGER,
    discipline TEXT
);

CREATE TABLE recipes(
    id INTEGER,
    overwriteTime TIMESTAMP,
    type TEXT,
    minRating SMALLINT,
    learnedFromItem BOOLEAN,
    chatLink TEXT,
    outId INTEGER,
    outCount SMALLINT,
    inIdOne INTEGER,
    inCountOne SMALLINT,
    inIdTwo INTEGER,
    inCountTwo SMALLINT,
    inIdThree INTEGER,
    inCountThree SMALLINT,
    inIdFour INTEGER,
    inCountFour SMALLINT
);

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
    salvage2Sell INTEGER
);
