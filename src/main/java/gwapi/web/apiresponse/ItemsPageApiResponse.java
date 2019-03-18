package gwapi.web.apiresponse;

import java.util.ArrayList;

import com.fasterxml.jackson.annotation.JsonProperty;
import gwapi.web.apiresponse.ItemsPageApiResponse.ItemResponse;

public class ItemsPageApiResponse extends ArrayList<ItemResponse> {

    public static class ItemResponse {

        private int id;

        private String name;

        @JsonProperty("chat_link")
        private String chatLink;

        private Integer level;

        private String type;

        private String rarity;

        private String icon;

        private String[] flags;

        @JsonProperty("vendor_value")
        private Integer vendorValue;

        @JsonProperty("details")
        private Detail detail;

        public int getId() {
            return id;
        }

        public String getName() {
            return name;
        }

        public String getChatLink() {
            return chatLink;
        }

        public Integer getLevel() {
            return level;
        }

        public String getType() {
            return type;
        }

        public String getRarity() {
            return rarity;
        }

        public String getIcon() {
            return icon;
        }

        public String[] getFlags() {
            return flags;
        }

        public Integer getVendorValue() {
            return vendorValue;
        }

        public Detail getDetail() {
            return detail;
        }

        public static class Detail {

            private String type;

            @JsonProperty("weight_class")
            private String weightClass;

            @JsonProperty("suffix_item_id")
            private Integer subItem;

            @JsonProperty("secondary_suffix_item_id")
            private Integer subItem2;

            @JsonProperty("infusion_slots")
            private ArrayList<InfusionSlot> infusions;

            public String getType() {
                return type;
            }

            public String getWeightClass() {
                return weightClass;
            }

            public Integer getSubItem() {
                return subItem;
            }

            public Integer getSubItem2() {
                return subItem2;
            }

            public ArrayList<InfusionSlot> getInfusions() {
                return infusions;
            }

            public static class InfusionSlot {

                @JsonProperty("item_id")
                private Integer id;

                public Integer getId() {
                    return id;
                }
            }
        }

    }
}
