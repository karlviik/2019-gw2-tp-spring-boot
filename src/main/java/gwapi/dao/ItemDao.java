package gwapi.dao;

import gwapi.entity.*;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Component
public class ItemDao extends JdbcDao {

    public void createOrOverwrite(Item item) {
        update("INSERT INTO item(id, name, chat_link, icon_id, rarity, level, bound, vendor_value, type, sub_type, sub_sub_type) VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?) ON CONFLICT (id) DO UPDATE SET name=?, chat_link=?, icon_id=?, rarity=?, level=?, bound=?, vendor_value=?, type=?, sub_type=?, sub_sub_type=?", item.getId(), item.getName(), item.getChatLink(), item.getIconId(), Objects.toString(item.getRarity()), item.getLevel(), item.getBound(), item.getVendorValue(), Objects.toString(item.getType()), Objects.toString(item.getType2()), Objects.toString(item.getType3()), item.getName(), item.getChatLink(), item.getIconId(), Objects.toString(item.getRarity()), item.getLevel(), item.getBound(), item.getVendorValue(), Objects.toString(item.getType()), Objects.toString(item.getType2()), Objects.toString(item.getType3()));
        if (item.getItemUpgrades() != null) {
            for (Integer upgrade : item.getItemUpgrades()) {
                update("INSERT INTO item_upgrade(item_id, upgrade_item_id) VALUES(?, ?) ON CONFLICT DO NOTHING", item.getId(), upgrade);
            }
        }
        if (item.getItemInfusions() != null) {
            for (Integer infusion : item.getItemInfusions()) {
                update("INSERT INTO item_infusion(item_id, infusion_item_id) VALUES(?, ?) ON CONFLICT DO NOTHING", item.getId(), infusion);
            }
        }
    }

    public List<Integer> getAllItemIds() {
        return list("SELECT id FROM item").stream()
                .map(result -> result.getInteger("id"))
                .collect(Collectors.toList());
    }

}
