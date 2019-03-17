package gwapi.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.support.JdbcDaoSupport;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;

@Component
@Primary
public class JdbcDao extends JdbcDaoSupport {
    // should be Java Database Connectivity Data Access Object

    @Autowired
    DataSource dataSource;

    JdbcTemplate dbt;

    @PostConstruct
    private void initialize(){
        setDataSource(dataSource);
        dbt = getJdbcTemplate();
    }

    public void createTable() {
        System.out.println(getJdbcTemplate() == null);
        getJdbcTemplate().update("CREATE TABLE iaaaatem (id int, name varchar);");
    }

    public void create(String sql, Object... args) {
        dbt.update(sql, args);
    }

    public int save(String sql, Object... args) {
        return 0;
    }

    public void delete(String sql, Object... args) {

    }

    public void update(String sql, Object... args) {

    }
}
