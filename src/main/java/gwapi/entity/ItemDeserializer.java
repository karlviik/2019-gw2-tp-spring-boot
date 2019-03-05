package gwapi.entity;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;

import java.io.IOException;
import java.util.LinkedList;

public class ItemDeserializer extends StdDeserializer<Item> {

    public ItemDeserializer() {
        this(null);
    }

    public ItemDeserializer(Class<?> vc) {
        super(vc);
    }

    // gotten from https://www.baeldung.com/jackson-deserialization
    @Override
    public Item deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException {

        JsonNode node = jp.getCodec().readTree(jp);

        Integer id = node.get("id").asInt();
        String name = node.get("name").asText();
        String chatLink = node.get("chat_link").asText();

        String[] regexSplit = node.get("icon").asText().split("/");
        Integer iconId = Integer.parseInt(regexSplit[regexSplit.length - 1].split("\\.")[0]);

        ItemRarity rarity = ItemRarity.valueOf(node.get("rarity").asText().toUpperCase());
        Integer level = node.get("level").asInt();

        String flags = node.get("flags").toString();
        Boolean bound = flags.contains("AccountBound") || flags.contains("SoulBindOnAcquire");
        Integer vendorValue;
        if (flags.contains("NoSell")) {
            vendorValue = null;
        } else {
            vendorValue = node.get("vendor_value").asInt();
        }

        ItemType type = ItemType.valueOf(node.get("type").asText().toUpperCase());

        ItemSubType type2 = null;
        ItemSubSubType type3 = null;
        Integer subItem = null;
        Integer subItem2 = null;
        LinkedList<Integer> subInfusionIds = new LinkedList<>();

        switch (type) {
            case BACK:
                subItem = getSubItem(node);
                subItem2 = getSubItem2(node);
                subInfusionIds = getInfusionIds(node);
                break;
            case ARMOR:
                type2 = getSubType(node);
                try {
                    type3 = ItemSubSubType.valueOf(node.get("details").get("weight_class").asText().toUpperCase());
                } catch (IllegalArgumentException e) {
                    type3 = ItemSubSubType.NONE;
                }
                subItem = getSubItem(node);
                subItem2 = getSubItem2(node);
                subInfusionIds = getInfusionIds(node);
                break;
            case WEAPON:
                type2 = getSubType(node);
                subItem = getSubItem(node);
                subItem2 = getSubItem2(node);
                subInfusionIds = getInfusionIds(node);
                break;
            case TRINKET:
                type2 = getSubType(node);
                subItem = getSubItem(node);
                subItem2 = getSubItem2(node);
                subInfusionIds = getInfusionIds(node);
                break;
            case GIZMO:
                type2 = getSubType(node);
                break;
            case TOOL:
                type2 = getSubType(node);
                break;
            case CONTAINER:
                type2 = getSubType(node);
                break;
            case GATHERING:
                type2 = getSubType(node);
                break;
            case CONSUMABLE:
                type2 = getSubType(node);
                break;
            case UPGRADECOMPONENT:
                type2 = getSubType(node);
                break;
        }
        for (int i = 0; i < 3; i++) {
            subInfusionIds.add(null);
        }

        return new Item(id, name, chatLink, iconId, rarity, level, bound, vendorValue, type, type2, type3, subItem, subItem2, subInfusionIds.get(0), subInfusionIds.get(1), subInfusionIds.get(2));
    }

    private ItemSubType getSubType (JsonNode node) {
        try {
            return ItemSubType.valueOf(node.get("details").get("type").asText().toUpperCase());
        } catch (IllegalArgumentException e) {
            return ItemSubType.NONE;
        }
    }

    private Integer getSubItem(JsonNode node) {
        if (node.get("details").has("suffix_item_id")) {
            return node.get("details").get("suffix_item_id").asInt();
        }
        return null;

    }

    private Integer getSubItem2(JsonNode node) {
        Integer id = node.get("details").get("secondary_suffix_item_id").asInt();
        if (id == 0) {
            return null;
        }
        return id;
    }

    private LinkedList<Integer> getInfusionIds(JsonNode node) {
        LinkedList<Integer> ids = new LinkedList<>();
        for (JsonNode subnode : node.get("details").get("infusion_slots")) {
            if (subnode.has("item_id")) {
                ids.add(subnode.get("item_id").asInt());
            }
        }
        return ids;
    }
}
