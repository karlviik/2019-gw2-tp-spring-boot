package gwapi.dao;

import gwapi.entity.Item;
import gwapi.entity.ItemRarity;
import gwapi.entity.ItemType;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

/**
 * DAO for interacting with table "item" and other related tables ("item_upgrade", "item_infusion")
 */
@Component
public class ItemDao extends JdbcDao {

  /**
   * Inserts new item with given parameters into item table if there is no id conflict.
   * On conflict it sets all parameters of already existing item to given parameters.
   *
   * @param id item id
   * @param name item name
   * @param chatLink item chat link code
   * @param iconLink icon link url
   * @param rarity item rarity as string
   * @param level item level
   * @param bound if item is bound
   * @param vendorValue item vendor value
   * @param type item type as string
   * @param subType item subtype as string
   * @param subSubType item subsubtype as string
   */
  public void createOrOverwriteItem(
      Integer id,
      String name,
      String chatLink,
      String iconLink,
      String rarity,
      Integer level,
      Boolean bound,
      Integer vendorValue,
      String type,
      String subType,
      String subSubType
  ) {
    update(
        "INSERT INTO item(id, name, chat_link, icon_link, rarity, level, bound, vendor_value, type, sub_type, sub_sub_type) " +
            "VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?) " +
            "ON CONFLICT (id) DO UPDATE " +
            "SET name=?, chat_link=?, icon_link=?, rarity=?, level=?, bound=?, vendor_value=?, type=?, sub_type=?, sub_sub_type=?",
        id, name, chatLink, iconLink, rarity, level, bound, vendorValue, type, subType, subSubType,
        name, chatLink, iconLink, rarity, level, bound, vendorValue, type, subType, subSubType);
  }

  /**
   * Insert item upgrade into item upgrades table if it's not there already.
   *
   * @param itemId id of the item upgrade is in
   * @param upgradeItemId id of the upgrade
   */
  public void createOrNothingUpgrade(Integer itemId, Integer upgradeItemId) {
    update(
        "INSERT INTO item_upgrade(item_id, upgrade_item_id) " +
            "VALUES(?, ?) " +
            "ON CONFLICT DO NOTHING",
        itemId, upgradeItemId);
  }

  /**
   * Insert item infusion into infusion table if it's not there already.
   *
   * @param itemId id of the item the infusion is in
   * @param infusionItemId id of the infusion
   */
  public void createOrNothingInfusion(Integer itemId, Integer infusionItemId) {
    update(
        "INSERT INTO item_infusion(item_id, infusion_item_id) " +
            "VALUES(?, ?) " +
            "ON CONFLICT DO NOTHING",
        itemId, infusionItemId);
  }

  /**
   * Get all item id's in the database.
   *
   * @return list of all id's
   */
  public List<Integer> getItemIds() {
    return list("SELECT id FROM item", (resultSet, i) -> resultSet.getInt("id"));
  }

  /**
   * Get item without mapping its components
   *
   * @param id id of item requested
   * @return optional of mapped item with that id
   */
  public Optional<Item> getItemNoComponents(int id) {
    return tryOne(
        "SELECT * " +
            "FROM item " +
            "WHERE id=?",
        this::mapItemNoComponents,
        id
    );

  }

  /**
   * Map resultset into an Item object without upgrades or infusions
   *
   * @param rs resultset of database select
   * @param i row, unused
   * @return Item object with no upgrades or infusions
   * @throws SQLException in case something does not seem right
   */
  private Item mapItemNoComponents(ResultSet rs, int i) throws SQLException {
    return new Item(
        rs.getInt("id"),
        rs.getString("name"),
        rs.getString("chat_link"),
        rs.getString("icon_link"),
        ItemRarity.valueOf(rs.getString("rarity")),
        rs.getInt("level"),
        rs.getBoolean("bound"),
        rs.getInt("vendor_value"),
        ItemType.valueOf(rs.getString("type"))
    );
  }

}
