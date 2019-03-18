package gwapi.entity;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

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
}
