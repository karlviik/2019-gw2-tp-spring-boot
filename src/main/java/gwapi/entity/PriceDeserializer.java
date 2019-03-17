package gwapi.entity;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;

import java.io.IOException;

public class PriceDeserializer extends StdDeserializer<Price> {

    public PriceDeserializer() {
        this(null);
    }

    public PriceDeserializer(Class<?> vc) {
        super(vc);
    }

    @Override
    public Price deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException {

        JsonNode node = jp.getCodec().readTree(jp);

        Integer id = node.get("id").asInt();
        Integer buyPrice = node.get("buys").get("unit_price").asInt();
        if (buyPrice == 0) {
            buyPrice = null;
        }
        Integer buyQuantity = node.get("buys").get("quantity").asInt();
        Integer sellPrice = node.get("sells").get("unit_price").asInt();
        if (sellPrice == 0) {
            sellPrice = null;
        }
        Integer sellQuantity = node.get("sells").get("quantity").asInt();

        return new Price(id, buyPrice, buyQuantity, sellPrice, sellQuantity);
    }
}
