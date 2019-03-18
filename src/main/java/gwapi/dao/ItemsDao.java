package gwapi.dao;

import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class ItemsDao extends JdbcDao {

    public void create(int id, String name) {
        update("INSERT INTO items(id, name) values (?, ?)", id, name);
    }

    public List<String> search() {
        return list("SELECT name FROM items").stream()
                .map(rs -> rs.getString("name"))
                .collect(Collectors.toList());
    }
}
