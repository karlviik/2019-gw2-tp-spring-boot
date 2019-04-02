package gwapi.web.apiresponse;

import java.util.ArrayList;

import com.fasterxml.jackson.annotation.JsonProperty;

import gwapi.web.apiresponse.RecipePageApiResponse.RecipeResponse;

// example: https://api.guildwars2.com/v2/recipes?page_size=200&page=60
public class RecipePageApiResponse extends ArrayList<RecipeResponse> {

    public static class RecipeResponse {

        private int id;

        private String type;

        @JsonProperty("chat_link")
        private String chatLink;

        @JsonProperty("min_rating")
        private int minRating;

        @JsonProperty("output_item_id")
        private int outId;

        @JsonProperty("output_item_count")
        private int outCount;

        private ArrayList<String> disciplines;

        private ArrayList<String> flags;

        private ArrayList<Ingredient> ingredients;

        public int getId() {
            return id;
        }

        public String getType() {
            return type;
        }

        public String getChatLink() {
            return chatLink;
        }

        public int getMinRating() {
            return minRating;
        }

        public int getOutId() {
            return outId;
        }

        public int getOutCount() {
            return outCount;
        }

        public ArrayList<String> getDisciplines() {
            return disciplines;
        }

        public ArrayList<String> getFlags() {
            return flags;
        }

        public ArrayList<Ingredient> getIngredients() {
            return ingredients;
        }

        public static class Ingredient {

            @JsonProperty("item_id")
            private int inId;

            @JsonProperty("count")
            private int inCount;

            public int getInId() {
                return inId;
            }

            public int getInCount() {
                return inCount;
            }
        }
    }
}
