package gwapi.entity;

/**
 * Includes all types item can have.
 * NONE is used for when type is empty.
 * NEW is used for when type exists but is not in this list.
 */
public enum ItemType {
  NONE,
  NEW,
  ARMOR,
  BACK,
  BAG,
  CONSUMABLE,
  CONTAINER,
  CRAFTINGMATERIAL,
  GATHERING,
  GIZMO,
  MINIPET,
  TOOL,
  TRAIT,
  TRINKET,
  TROPHY,
  UPGRADECOMPONENT,
  WEAPON,
  KEY
}
