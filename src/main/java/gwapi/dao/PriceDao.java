package gwapi.dao;

import gwapi.entity.Price;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * DAO for table "price"
 */
@Component
public class PriceDao extends JdbcDao {

  /**
   * Insert new price point of item to database
   *
   * @param itemId item id of price point
   * @param createdAt time of creation
   * @param buyPrice buy price in coppers
   * @param buyQuantity buy quantity aka demand
   * @param sellPrice sell price in coppers
   * @param sellQuantity sell quantity aka supply
   */
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

  /**
   * Insert craft price data point into database, on conflict add those values to existing price point
   *
   * @param itemId item id of price point
   * @param time time of creation
   * @param craftBuy buy order cost in coppers
   * @param craftSell sell order cost in coppers
   */
  public void addCraftData(Integer itemId, Timestamp time, Integer craftBuy, Integer craftSell) {
    update(
        "INSERT INTO price(item_id, created_at, craft_buy_price, craft_sell_price) " +
        "VALUES(?, ?, ?, ?) " +
        "ON CONFLICT (item_id, created_at) DO UPDATE " +
        "SET craft_buy_price=?, craft_sell_price=?",
        itemId, time, craftBuy, craftSell,
        craftBuy, craftSell);
  }

  /**
   * Get Price object with just trade post and craft costs
   *
   * @param itemId id of item
   * @param time time of price point
   * @return Price object with requested fields
   */
  public Optional<Price> getTradePostAndCraftPrice(Integer itemId, LocalDateTime time) {
    return tryOne(
        "SELECT item_id, buy_price, sell_price, craft_buy_price, craft_sell_price " +
            "FROM price " +
            "WHERE item_id=? AND created_at=?",
        this::mapTradePostAndCraftPrice,
        itemId, Timestamp.valueOf(time));
  }

  /**
   * Get all prices for a given item in a list.
   *
   * @param itemId id of item requested
   * @return list of all prices as Price objects.
   */
  public List<Price> getPrices(Integer itemId) {
    return list(
        "SELECT * " +
            "FROM price " +
            "WHERE item_id=?",
        this::mapPrice,
        itemId);
  }

  /**
   * Map resultset to partial Price object
   *
   * @param rs resultset from database
   * @param i row
   * @return Price object with TP and craft prices
   * @throws SQLException if bad
   */
  private Price mapTradePostAndCraftPrice(ResultSet rs, int i) throws SQLException {
    return new Price(
        rs.getInt("item_id"),
        rs.getObject("buy_price", Integer.class),
        rs.getObject("sell_price", Integer.class),
        rs.getObject("craft_buy_price", Integer.class),
        rs.getObject("craft_sell_price", Integer.class)
    );
  }

  /**
   * Map resultset to full Price object.
   *
   * @param rs resultset from database
   * @param i row
   * @return Price object with all class variables
   * @throws SQLException if bad
   */
  private Price mapPrice(ResultSet rs, int i) throws SQLException {
    return new Price(
        rs.getInt("item_id"),
        rs.getObject("created_at", LocalDateTime.class),
        rs.getObject("buy_price", Integer.class),
        rs.getObject("buy_quantity", Integer.class),
        rs.getObject("sell_price", Integer.class),
        rs.getObject("sell_quantity", Integer.class),
        rs.getObject("craft_buy_price", Integer.class),
        rs.getObject("craft_sell_price", Integer.class)
    );
  }
}
