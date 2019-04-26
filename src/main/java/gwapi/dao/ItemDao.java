package gwapi.dao;

import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ItemDao extends JdbcDao {

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
        id, name, chatLink, iconLink, rarity, level, bound, vendorValue, type, subType, subSubType, name, chatLink, iconLink, rarity, level, bound, vendorValue, type, subType, subSubType);
  }

  public void createOrNothingUpgrade(Integer itemId, Integer upgradeItemId) {
    update(
        "INSERT INTO item_upgrade(item_id, upgrade_item_id) " +
            "VALUES(?, ?) " +
            "ON CONFLICT DO NOTHING",
        itemId, upgradeItemId);
  }

  public void createOrNothingInfusion(Integer itemId, Integer infusionItemId) {
    update(
        "INSERT INTO item_infusion(item_id, infusion_item_id) " +
            "VALUES(?, ?) " +
            "ON CONFLICT DO NOTHING",
        itemId, infusionItemId);
  }

  public List<Integer> getItemIds() {
    return list("SELECT id FROM item", (resultset, i) -> resultset.getInt("id"));
  }

}
