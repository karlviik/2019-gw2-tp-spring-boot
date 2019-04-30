package gwapi.entity;

/**
 * Includes all possible item rarity types.
 * NONE for if it's empty (shouldn't be used)
 * NEW for if it's new
 * right one otherwise
 */
public enum ItemRarity {
  NONE,
  NEW,
  JUNK,
  BASIC,
  FINE,
  MASTERWORK,
  RARE,
  EXOTIC,
  ASCENDED,
  LEGENDARY
}
