package gwapi.dao;

import gwapi.entity.Price;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class PriceDao extends JdbcDao {

  public void addTradePostData(
      Integer itemId,
      Timestamp createdAt,
      Integer buyPrice,
      Integer buyQuantity,
      Integer sellPrice,
      Integer sellQuantity
  ) {
    update("INSERT INTO price(item_id, created_at, buy_price, buy_quantity, sell_price, sell_quantity) VALUES(?, ?, ?, ?, ?, ?)", itemId, createdAt, buyPrice, buyQuantity, sellPrice, sellQuantity);
  }

  public void addCraftData(Integer itemId, Timestamp time, Integer craftBuy, Integer craftSell) {
    update("INSERT INTO price(item_id, created_at, craft_buy_price, craft_sell_price) VALUES(?, ?, ?, ?) ON CONFLICT (item_id, created_at) DO UPDATE SET craft_buy_price=?, craft_sell_price=?", itemId, time, craftBuy, craftSell, craftBuy, craftSell);
  }

  public LinkedList<Integer> getCraftData(Integer itemId, LocalDateTime time) {
    List<LinkedList<Integer>> list = list("SELECT craft_buy_price, craft_sell_price FROM price WHERE item_id=? AND created_at=?", itemId, Timestamp.valueOf(time)).stream()
        .map(x -> mapCraftPrices(x))
        .collect(Collectors.toList());
    return list.size() == 0 ? null : list.get(0);
  }

  public Price getTradePostAndCraftPrice(Integer itemId, LocalDateTime time) {
    List<Price> list = list("SELECT buy_price, sell_price, craft_buy_price, craft_sell_price FROM price WHERE item_id=? AND created_at=?", itemId, Timestamp.valueOf(time)).stream()
        .map(x -> mapTradePostAndCraftPrice(itemId, x))
        .collect(Collectors.toList());
    return list.size() == 0 ? null : list.get(0);
  }

  private LinkedList<Integer> mapCraftPrices(DbRow row) {
    LinkedList<Integer> list = new LinkedList<>();
    list.add(row.getInteger("craft_buy_price"));
    list.add(row.getInteger("craft_sell_price"));
    return list;
  }

  private Price mapTradePostAndCraftPrice(Integer itemId, DbRow row) {
    return new Price(
        itemId,
        row.getInteger("buy_price"),
        row.getInteger("sell_price"),
        row.getInteger("craft_buy_price"),
        row.getInteger("craft_sell_price")
    );
  }
}
