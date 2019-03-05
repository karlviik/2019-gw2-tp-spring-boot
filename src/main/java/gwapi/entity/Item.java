package gwapi.entity;


import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "items")
@JsonDeserialize(using = ItemDeserializer.class)
public class Item {
    @Id
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
    private Integer subItem;
    private Integer subItem2;
    private Integer subInfusion;
    private Integer subInfusion2;
    private Integer subInfusion3;

    protected Item() {

    }

    public Item(Integer id, String name, String chatLink, Integer iconId, ItemRarity rarity, Integer level,
                Boolean bound, Integer vendorValue, ItemType type, ItemSubType type2, ItemSubSubType type3,
                Integer subItem, Integer subItem2, Integer subInfusion, Integer subInfusion2, Integer subInfusion3) {
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
        this.subItem = subItem;
        this.subItem2 = subItem2;
        this.subInfusion = subInfusion;
        this.subInfusion2 = subInfusion2;
        this.subInfusion3 = subInfusion3;
    }

    @Override
    public String toString() {
        return String.join(", ",
                String.valueOf(id),
                String.valueOf(name),
                String.valueOf(type),
                String.valueOf(subItem),
                String.valueOf(subItem2),
                String.valueOf(subInfusion),
                String.valueOf(subInfusion2),
                String.valueOf(subInfusion3));
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getChatLink() {
        return chatLink;
    }

    public void setChatLink(String chatLink) {
        this.chatLink = chatLink;
    }

    public Integer getIconId() {
        return iconId;
    }

    public void setIconId(Integer iconId) {
        this.iconId = iconId;
    }

    public ItemRarity getRarity() {
        return rarity;
    }

    public void setRarity(ItemRarity rarity) {
        this.rarity = rarity;
    }

    public Integer getLevel() {
        return level;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }

    public Boolean getBound() {
        return bound;
    }

    public void setBound(Boolean bound) {
        this.bound = bound;
    }

    public Integer getVendorValue() {
        return vendorValue;
    }

    public void setVendorValue(Integer vendorValue) {
        this.vendorValue = vendorValue;
    }

    public ItemType getType() {
        return type;
    }

    public void setType(ItemType type) {
        this.type = type;
    }

    public ItemSubType getType2() {
        return type2;
    }

    public void setType2(ItemSubType type2) {
        this.type2 = type2;
    }

    public ItemSubSubType getType3() {
        return type3;
    }

    public void setType3(ItemSubSubType type3) {
        this.type3 = type3;
    }

    public Integer getSubItem() {
        return subItem;
    }

    public void setSubItem(Integer subItem) {
        this.subItem = subItem;
    }

    public Integer getSubItem2() {
        return subItem2;
    }

    public void setSubItem2(Integer subItem2) {
        this.subItem2 = subItem2;
    }

    public Integer getSubInfusion() {
        return subInfusion;
    }

    public void setSubInfusion(Integer subInfusion) {
        this.subInfusion = subInfusion;
    }

    public Integer getSubInfusion2() {
        return subInfusion2;
    }

    public void setSubInfusion2(Integer subInfusion2) {
        this.subInfusion2 = subInfusion2;
    }

    public Integer getSubInfusion3() {
        return subInfusion3;
    }

    public void setSubInfusion3(Integer subInfusion3) {
        this.subInfusion3 = subInfusion3;
    }
}
