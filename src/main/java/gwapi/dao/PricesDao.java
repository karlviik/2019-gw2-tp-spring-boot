package gwapi.dao;

import gwapi.entity.Price;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class PricesDao extends JdbcDao {

    public void create(Price price) {
        update("INSERT INTO prices (id, time, buyPrice, buyQuantity, sellPrice, sellQuantity) VALUES (?, ?, ?, ?, ?, ?)", price.getId(), java.sql.Timestamp.valueOf(price.getTime()), price.getBuyPrice(), price.getBuyQuantity(), price.getSellPrice(), price.getSellQuantity());
    }

    public List<String> searchId(int id) {
        return list("SELECT id, buyPrice, sellPrice FROM prices WHERE id=?", id).stream()
                .map(rs -> rs.getString("buyPrice"))
                .collect(Collectors.toList());
    }
}
