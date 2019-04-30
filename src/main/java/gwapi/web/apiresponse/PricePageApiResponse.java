package gwapi.web.apiresponse;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;

import gwapi.web.apiresponse.PricePageApiResponse.PriceResponse;

/**
 * API Json structure for /prices?page_size=x endpoint
 *
 * example: https://api.guildwars2.com/v2/prices?page_size=200&page=125
 */
public class PricePageApiResponse extends ArrayList<PriceResponse> {

  public static class PriceResponse {

    private int id;

    private Transaction buys;

    private Transaction sells;

    public int getId() {
      return id;
    }

    public Transaction getBuys() {
      return buys;
    }

    public Transaction getSells() {
      return sells;
    }

    public static class Transaction {

      private int quantity;

      @JsonProperty("unit_price")
      private int unitPrice;

      public int getQuantity() {
        return quantity;
      }

      public int getUnitPrice() {
        return unitPrice;
      }
    }
  }
}
