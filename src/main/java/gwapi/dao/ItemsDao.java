package gwapi.dao;

import gwapi.entity.ItemRarity;
import gwapi.entity.ItemSubSubType;
import gwapi.entity.ItemSubType;
import gwapi.entity.ItemType;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class ItemsDao extends JdbcDao {

    public void create(Integer id, String name, String chatLink, Integer iconId, ItemRarity rarity, Integer level,
                       Boolean bound, Integer vendorValue, ItemType type, ItemSubType type2, ItemSubSubType type3,
                       Integer subItem, Integer subItem2, Integer subInfusion, Integer subInfusion2, Integer subInfusion3) {
        update("INSERT INTO items(id, name, chatLink, iconId, rarity, level, bound, vendorValue, type, type2, type3, subItem, subItem2, subInfusion, subInfusion2, subInfusion3) values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)", id, name, chatLink, iconId, rarity, level, bound, vendorValue, type, type2, type3, subItem, subItem2, subInfusion, subInfusion2, subInfusion3);
    }

    public List<String> search() {
        return list("SELECT name FROM items").stream()
                .map(rs -> rs.getString("name"))
                .collect(Collectors.toList());
    }
}
