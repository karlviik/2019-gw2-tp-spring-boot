package gwapi.entity;

import java.time.LocalDateTime;

public class Price {

  private int id;
  private LocalDateTime time;
  private Integer buyPrice;
  private Integer buyQuantity;
  private Integer sellPrice;
  private Integer sellQuantity;
  private Integer craftBuyPrice;
  private Integer craftSellPrice;
  private Integer openBuyPrice;
  private Integer openSellPrice;
  private Integer mysticForgeBuyPrice;
  private Integer mysticForgeSellPrice;


  public Price(int id, LocalDateTime time, Integer buyPrice, Integer buyQuantity, Integer sellPrice, Integer sellQuantity) {
    this.id = id;
    this.time = time;
    this.buyPrice = buyPrice;
    this.buyQuantity = buyQuantity;
    this.sellPrice = sellPrice;
    this.sellQuantity = sellQuantity;
  }

  public Price(int id, Integer buyPrice, Integer sellPrice, Integer craftBuyPrice, Integer craftSellPrice) {
    this.id = id;
    this.buyPrice = buyPrice;
    this.sellPrice = sellPrice;
    this.craftBuyPrice = craftBuyPrice;
    this.craftSellPrice = craftSellPrice;
  }

  @Override
  public String toString() {
    return String.join(" ",
        String.valueOf(id),
        String.valueOf(buyPrice),
        String.valueOf(buyQuantity),
        String.valueOf(sellPrice),
        String.valueOf(sellQuantity));
  }

  public int getId() {
    return id;
  }

  public LocalDateTime getTime() {
    return time;
  }

  public Integer getBuyPrice() {
    return buyPrice;
  }

  public Integer getBuyQuantity() {
    return buyQuantity;
  }

  public Integer getSellPrice() {
    return sellPrice;
  }

  public Integer getSellQuantity() {
    return sellQuantity;
  }

  public Integer getCraftBuyPrice() {
    return craftBuyPrice;
  }

  public Integer getCraftSellPrice() {
    return craftSellPrice;
  }
}
