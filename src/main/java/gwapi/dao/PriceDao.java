package gwapi.dao;

import gwapi.entity.Price;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

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
    update(
        "INSERT INTO price(item_id, created_at, buy_price, buy_quantity, sell_price, sell_quantity) " +
        "VALUES(?, ?, ?, ?, ?, ?)",
        itemId, createdAt, buyPrice, buyQuantity, sellPrice, sellQuantity);
  }

  public void addCraftData(Integer itemId, Timestamp time, Integer craftBuy, Integer craftSell) {
    update(
        "INSERT INTO price(item_id, created_at, craft_buy_price, craft_sell_price) " +
        "VALUES(?, ?, ?, ?) " +
        "ON CONFLICT (item_id, created_at) DO UPDATE " +
        "SET craft_buy_price=?, craft_sell_price=?",
        itemId, time, craftBuy, craftSell,
        craftBuy, craftSell);
  }

  public Optional<Price> getTradePostAndCraftPrice(Integer itemId, LocalDateTime time) {
    return tryOne(
        "SELECT item_id, buy_price, sell_price, craft_buy_price, craft_sell_price " +
            "FROM price " +
            "WHERE item_id=? AND created_at=?",
        this::mapTradePostAndCraftPrice,
        itemId, Timestamp.valueOf(time));
  }

  public List<Price> getPrices(Integer itemId) {
    return list(
        "SELECT * " +
            "FROM price " +
            "WHERE item_id=?",
        this::mapPrice,
        itemId);
  }

  private Price mapTradePostAndCraftPrice(ResultSet row, int i) throws SQLException {
    return new Price(
        row.getInt("item_id"),
        row.getObject("buy_price", Integer.class),
        row.getObject("sell_price", Integer.class),
        row.getObject("craft_buy_price", Integer.class),
        row.getObject("craft_sell_price", Integer.class)
    );
  }

  private Price mapPrice(ResultSet row, int i) throws SQLException {
    return new Price(
        row.getInt("item_id"),
        row.getObject("created_at", LocalDateTime.class),
        row.getObject("buy_price", Integer.class),
        row.getObject("buy_quantity", Integer.class),
        row.getObject("sell_price", Integer.class),
        row.getObject("sell_quantity", Integer.class),
        row.getObject("craft_buy_price", Integer.class),
        row.getObject("craft_sell_price", Integer.class)
    );
  }
}
