package gwapi.entity;

import java.util.List;

public class Item {

  private Integer id;
  private String name;
  private String chatLink;
  private Integer iconId; // not 100% sure this ID is always an integer, probably is though
  private ItemRarity rarity;
  private Integer level;
  private Boolean bound; // true if flags have either acc bound or souldbound on aquire
  private Integer vendorValue; // should not be populated if flags include NoSell
  private ItemType type;
  private ItemSubType type2;
  private ItemSubSubType type3;
  private List<Integer> itemUpgrades;
  private List<Integer> itemInfusions;

  protected Item() {

  }

  public Item(
      Integer id,
      String name,
      String chatLink,
      Integer iconId,
      ItemRarity rarity,
      Integer level,
      Boolean bound,
      Integer vendorValue,
      ItemType type,
      ItemSubType type2,
      ItemSubSubType type3,
      List<Integer> itemUpgrades,
      List<Integer> itemInfusions
  ) {
    this.id = id;
    this.name = name;
    this.chatLink = chatLink;
    this.iconId = iconId;
    this.rarity = rarity;
    this.level = level;
    this.bound = bound;
    this.vendorValue = vendorValue;
    this.type = type;
    this.type2 = type2;
    this.type3 = type3;
    this.itemUpgrades = itemUpgrades;
    this.itemInfusions = itemInfusions;
  }

  @Override
  public String toString() {
    return String.join(", ",
        String.valueOf(id),
        String.valueOf(name),
        String.valueOf(type));
  }

  public Integer getId() {
    return id;
  }

  public String getName() {
    return name;
  }

  public String getChatLink() {
    return chatLink;
  }

  public Integer getIconId() {
    return iconId;
  }

  public ItemRarity getRarity() {
    return rarity;
  }

  public Integer getLevel() {
    return level;
  }

  public Boolean getBound() {
    return bound;
  }

  public Integer getVendorValue() {
    return vendorValue;
  }

  public ItemType getType() {
    return type;
  }

  public ItemSubType getType2() {
    return type2;
  }

  public ItemSubSubType getType3() {
    return type3;
  }

  public List<Integer> getItemUpgrades() {
    return itemUpgrades;
  }

  public List<Integer> getItemInfusions() {
    return itemInfusions;
  }
}
