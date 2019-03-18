package gwapi.entity;

import java.time.LocalDateTime;

public class Price {

    private int id;
    private LocalDateTime time;
    private Integer buyPrice;
    private Integer buyQuantity;
    private Integer sellPrice;
    private Integer sellQuantity;
    private Integer buyCraft;
    private Integer sellCraft;
    private Integer buyOpen;
    private Integer sellOpen;
    private Integer buyMysticForge;
    private Integer sellMysticForge;
    private Integer salvage1Id;
    private Integer salvage1Buy;
    private Integer salvage1Sell;
    private Integer salvage2Id;
    private Integer salvage2Buy;
    private Integer salvage2Sell;


    public Price(int id, LocalDateTime time, Integer buyPrice, Integer buyQuantity, Integer sellPrice, Integer sellQuantity) {
        this.id = id;
        this.time = time;
        this.buyPrice = buyPrice;
        this.buyQuantity = buyQuantity;
        this.sellPrice = sellPrice;
        this.sellQuantity = sellQuantity;
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
}
