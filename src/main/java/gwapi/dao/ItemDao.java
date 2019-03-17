package gwapi.dao;

import gwapi.entity.Item;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.util.List;
import java.util.stream.Collectors;
import javax.sql.DataSource;

@Component
public class ItemDao extends JdbcDao {



    public void create(int id, String name) {
        create("INSERT INTO item(id, name) values (?, ?)", id, name);
    }

//    public List<Item> search() {
//        ResultSet rs = null;
//        List<ResultSet> rows = null;
//        return rows.stream()
//                .map(rs -> new Item(rs.getString("name")))
//                .collect(Collectors.toList());
//    }
}
