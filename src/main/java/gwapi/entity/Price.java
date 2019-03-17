package gwapi.entity;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

@Entity
@Table(name = "prices")
@JsonDeserialize(using = PriceDeserializer.class)
public class Price {

    @Id // temporary
    private int id;
    private Date time;
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


    protected Price() {

    }


    public Price(int id, Integer buyPrice, Integer buyQuantity, Integer sellPrice, Integer sellQuantity) {
        this.id = id;
        this.buyPrice = buyPrice;
        this.buyQuantity = buyQuantity;
        this.sellPrice = sellPrice;
        this.sellQuantity = sellQuantity;
    }

    public Price(int id, Date time, Integer buyPrice, Integer buyQuantity, Integer sellPrice, Integer sellQuantity, Integer buyCraft, Integer sellCraft, Integer buyOpen, Integer sellOpen, Integer buyMysticForge, Integer sellMysticForge, Integer salvage1Id, Integer salvage1Buy, Integer salvage1Sell, Integer salvage2Id, Integer salvage2Buy, Integer salvage2Sell) {
        this.id = id;
        this.time = time;
        this.buyPrice = buyPrice;
        this.buyQuantity = buyQuantity;
        this.sellPrice = sellPrice;
        this.sellQuantity = sellQuantity;
        this.buyCraft = buyCraft;
        this.sellCraft = sellCraft;
        this.buyOpen = buyOpen;
        this.sellOpen = sellOpen;
        this.buyMysticForge = buyMysticForge;
        this.sellMysticForge = sellMysticForge;
        this.salvage1Id = salvage1Id;
        this.salvage1Buy = salvage1Buy;
        this.salvage1Sell = salvage1Sell;
        this.salvage2Id = salvage2Id;
        this.salvage2Buy = salvage2Buy;
        this.salvage2Sell = salvage2Sell;
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

    public void setId(int id) {
        this.id = id;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    public Integer getBuyPrice() {
        return buyPrice;
    }

    public void setBuyPrice(Integer buyPrice) {
        this.buyPrice = buyPrice;
    }

    public Integer getBuyQuantity() {
        return buyQuantity;
    }

    public void setBuyQuantity(Integer buyQuantity) {
        this.buyQuantity = buyQuantity;
    }

    public Integer getSellPrice() {
        return sellPrice;
    }

    public void setSellPrice(Integer sellPrice) {
        this.sellPrice = sellPrice;
    }

    public Integer getSellQuantity() {
        return sellQuantity;
    }

    public void setSellQuantity(Integer sellQuantity) {
        this.sellQuantity = sellQuantity;
    }

    public Integer getBuyCraft() {
        return buyCraft;
    }

    public void setBuyCraft(Integer buyCraft) {
        this.buyCraft = buyCraft;
    }

    public Integer getSellCraft() {
        return sellCraft;
    }

    public void setSellCraft(Integer sellCraft) {
        this.sellCraft = sellCraft;
    }

    public Integer getBuyOpen() {
        return buyOpen;
    }

    public void setBuyOpen(Integer buyOpen) {
        this.buyOpen = buyOpen;
    }

    public Integer getSellOpen() {
        return sellOpen;
    }

    public void setSellOpen(Integer sellOpen) {
        this.sellOpen = sellOpen;
    }

    public Integer getBuyMysticForge() {
        return buyMysticForge;
    }

    public void setBuyMysticForge(Integer buyMysticForge) {
        this.buyMysticForge = buyMysticForge;
    }

    public Integer getSellMysticForge() {
        return sellMysticForge;
    }

    public void setSellMysticForge(Integer sellMysticForge) {
        this.sellMysticForge = sellMysticForge;
    }

    public Integer getSalvage1Id() {
        return salvage1Id;
    }

    public void setSalvage1Id(Integer salvage1Id) {
        this.salvage1Id = salvage1Id;
    }

    public Integer getSalvage1Buy() {
        return salvage1Buy;
    }

    public void setSalvage1Buy(Integer salvage1Buy) {
        this.salvage1Buy = salvage1Buy;
    }

    public Integer getSalvage1Sell() {
        return salvage1Sell;
    }

    public void setSalvage1Sell(Integer salvage1Sell) {
        this.salvage1Sell = salvage1Sell;
    }

    public Integer getSalvage2Id() {
        return salvage2Id;
    }

    public void setSalvage2Id(Integer salvage2Id) {
        this.salvage2Id = salvage2Id;
    }

    public Integer getSalvage2Buy() {
        return salvage2Buy;
    }

    public void setSalvage2Buy(Integer salvage2Buy) {
        this.salvage2Buy = salvage2Buy;
    }

    public Integer getSalvage2Sell() {
        return salvage2Sell;
    }

    public void setSalvage2Sell(Integer salvage2Sell) {
        this.salvage2Sell = salvage2Sell;
    }
}
