//package gwapi.entity;
//
//import com.fasterxml.jackson.core.JsonParser;
//import com.fasterxml.jackson.databind.DeserializationContext;
//import com.fasterxml.jackson.databind.JsonNode;
//import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
//
//import java.io.IOException;
//import java.util.LinkedList;
//
///**
// * Deserializes Json from gw2api Item json object
// * @see <a href=https://wiki.guildwars2.com/wiki/API:2/items>GW2Wiki page on API2 item endpoint</a>
// * @see <a href=https://www.baeldung.com/jackson-deserialization>place where I got template</a>
// */
//public class ItemDeserializer extends StdDeserializer<Item> {
//
//    public ItemDeserializer() {
//        this(null);
//    }
//
//    public ItemDeserializer(Class<?> vc) {
//        super(vc);
//    }
//
//    /**
//     * Actual deserializer. See comments.
//     *
//     * @return Item POJO
//     */
//    @Override
//    public Item deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException {
//
//        JsonNode node = jp.getCodec().readTree(jp);
//
//        // this block includes easy and definitely there components
//        Integer id = node.get("id").asInt();
//        String name = node.get("name").asText();
//        String chatLink = node.get("chat_link").asText();
//        Integer level = node.get("level").asInt();
//
//        // puts type as none if type is empty, new if not in current enum, proper otherwise
//        // acts as future proofing, the new enum
//        ItemType type;
//        {
//            String typeAsString = node.get("type").asText().toUpperCase();
//            if (typeAsString.length() == 0) {
//                type = ItemType.NONE;
//            } else {
//                try {
//                    type = ItemType.valueOf(typeAsString);
//                } catch (IllegalArgumentException e) {
//                    type = ItemType.NEW;
//                }
//            }
//        }
//
//        // puts as none if empty, new if new, right one otherwise
//        ItemRarity rarity;
//        {
//            String rarityAsString = node.get("rarity").asText().toUpperCase();
//            if (rarityAsString.length() == 0) {
//                rarity = ItemRarity.NONE;
//            } else {
//                try {
//                    rarity = ItemRarity.valueOf(rarityAsString);
//                } catch (IllegalArgumentException e) {
//                    rarity = ItemRarity.NEW;
//                }
//            }
//        }
//
//        // regex url gotten from the icon field, split it with regex and get ID part
//        String[] regexSplit = node.get("icon").asText().split("/");
//        Integer iconId = Integer.parseInt(regexSplit[regexSplit.length - 1].split("\\.")[0]);
//
//        // get if flags indicate the item is bound on acquire.
//        // also set vendor value as null if item can not be sold
//        // otherwise after loop get correct vendorValue
//        boolean bound = false;
//        Integer vendorValue = 0;
//        {
//            JsonNode flags = node.get("flags");
//            for (JsonNode flagNode : flags) {
//                if (!bound && (flagNode.asText().equals("AccountBound") || flagNode.asText().equals("SoulBindOnAcquire"))) {
//                    bound = true;
//                }
//                if (flagNode.asText().equals("NoSell")) {
//                    vendorValue = null;
//                    if (bound) {
//                        break;
//                    }
//                }
//            }
//            if (vendorValue != null) {
//                vendorValue = node.get("vendor_value").asInt();
//            }
//        }
//
//        ItemSubType type2 = null;
//        ItemSubSubType type3 = null;
//        Integer subItem;
//        Integer subItem2;
//        LinkedList<Integer> subInfusionIds;
//
//        // for more complex items return new Items right away to avoid complications
//        // otherwise put values into variables for later new item call
//        switch (type) {
//            case ARMOR:
//                try {
//                    type3 = ItemSubSubType.valueOf(node.get("details").get("weight_class").asText().toUpperCase());
//                } catch (IllegalArgumentException e) {
//                    type3 = ItemSubSubType.NEW;
//                }
//            case WEAPON:
//            case TRINKET:
//                type2 = getSubType(node);
//            case BACK:
//                subItem = getSubItem(node);
//                subItem2 = getSubItem2(node);
//                subInfusionIds = getInfusionIds(node);
//                return new Item(id, name, chatLink, iconId, rarity, level, bound, vendorValue, type, type2, type3, subItem, subItem2, subInfusionIds.get(0), subInfusionIds.get(1), subInfusionIds.get(2));
//            case GIZMO:
//            case TOOL:
//            case CONTAINER:
//            case GATHERING:
//            case CONSUMABLE:
//            case UPGRADECOMPONENT:
//                type2 = getSubType(node);
//                break;
//        }
//
//        return new Item(id, name, chatLink, iconId, rarity, level, bound, vendorValue, type, type2, null, null, null, null, null, null);
//    }
//
//    /**
//     * Get subtype of details object.
//     *
//     * @return ItemSubType: NEW if new, NONE if none, proper otherwise
//     */
//    private ItemSubType getSubType (JsonNode node) {
//        try {
//            String subTypeAsString = node.get("details").get("type").asText().toUpperCase();
//            return subTypeAsString.length() == 0 ? ItemSubType.NONE : ItemSubType.valueOf(subTypeAsString);
//        } catch (IllegalArgumentException e) {
//            return ItemSubType.NEW;
//        }
//    }
//
//    private Integer getSubItem(JsonNode node) {
//        if (node.get("details").has("suffix_item_id")) {
//            return node.get("details").get("suffix_item_id").asInt();
//        }
//        return null;
//    }
//
//    private Integer getSubItem2(JsonNode node) {
//        int id = node.get("details").get("secondary_suffix_item_id").asInt();
//        if (id == 0) {
//            return null;
//        }
//        return id;
//    }
//
//    private LinkedList<Integer> getInfusionIds(JsonNode node) {
//        LinkedList<Integer> ids = new LinkedList<>();
//        int i = 0;
//        for (JsonNode subnode : node.get("details").get("infusion_slots")) {
//            if (subnode.has("item_id")) {
//                ids.add(subnode.get("item_id").asInt());
//                i++;
//            }
//        }
//        while (i < 3) {
//            ids.add(null);
//            i++;
//        }
//        return ids;
//    }
//}
