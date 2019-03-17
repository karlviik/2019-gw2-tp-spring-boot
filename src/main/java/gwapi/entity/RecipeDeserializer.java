package gwapi.entity;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;

import java.io.IOException;
import java.util.LinkedList;

public class RecipeDeserializer extends StdDeserializer<Recipe> {

    public RecipeDeserializer() {
        this(null);
    }

    public RecipeDeserializer(Class<?> vc) {
        super(vc);
    }

    @Override
    public Recipe deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException {

        JsonNode node = jp.getCodec().readTree(jp);

        Integer recipeId = node.get("id").asInt();
        String chatLink = node.get("chat_link").asText();
        Integer minRating = node.get("min_rating").asInt();
        Integer outId = node.get("output_item_id").asInt();
        Integer outCount = node.get("output_item_count").asInt();

        boolean learnedFromItem = node.get("flags").toString().contains("LearnedFromItem");

        RecipeType type;
        try {
            String typeAsString = node.get("type").asText().toUpperCase();
            type = typeAsString.length() == 0 ? RecipeType.NONE : RecipeType.valueOf(typeAsString);
        } catch (IllegalArgumentException e) {
            type = RecipeType.NEW;
        }

        // initialise the boolean array
        Boolean[] disciplines = new Boolean[10];
        for (int i = 0; i < 10; i++) {
            disciplines[i] = false;
        }

        for (JsonNode discipline : node.withArray("disciplines")) {
            try {
                switch (RecipeDiscipline.valueOf(discipline.asText().toUpperCase())) {
                    case ARTIFICER:
                        disciplines[1] = true;
                        break;
                    case ARMORSMITH:
                        disciplines[2] = true;
                        break;
                    case CHEF:
                        disciplines[3] = true;
                        break;
                    case HUNTSMAN:
                        disciplines[4] = true;
                        break;
                    case JEWELER:
                        disciplines[5] = true;
                        break;
                    case LEATHERWORKER:
                        disciplines[6] = true;
                        break;
                    case TAILOR:
                        disciplines[7] = true;
                        break;
                    case WEAPONSMITH:
                        disciplines[8] = true;
                        break;
                    case SCRIBE:
                        disciplines[9] = true;
                        break;
                }
            } catch (IllegalArgumentException e) {
                disciplines[0] = true;
            }
        }


        LinkedList<Integer> componentIds = new LinkedList<>();
        LinkedList<Integer> componentCounts = new LinkedList<>();
        int i = 0;

        for (JsonNode subnode : node.get("ingredients")) {
            componentIds.add(i, subnode.get("item_id").asInt());
            componentCounts.add(i, subnode.get("count").asInt());
            i++;
        }

        while (i < 4) {
            componentIds.add(null);
            componentCounts.add(null);
            i++;
        }

        return new Recipe(recipeId, null, type, minRating, learnedFromItem, chatLink, outId, outCount, componentIds.get(0), componentCounts.get(0), componentIds.get(1), componentCounts.get(1), componentIds.get(2), componentCounts.get(2), componentIds.get(3), componentCounts.get(3), disciplines[0], disciplines[1], disciplines[2], disciplines[3], disciplines[4], disciplines[5], disciplines[6], disciplines[7], disciplines[8], disciplines[9]);
    }
}
