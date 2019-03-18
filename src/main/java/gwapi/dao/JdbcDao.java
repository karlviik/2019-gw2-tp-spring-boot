package gwapi.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.support.JdbcDaoSupport;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public class JdbcDao extends JdbcDaoSupport {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    protected int update(String sql, Object... arguments) {
        return jdbcTemplate.update(sql, arguments);
    }

    protected int update(String sql, Map<String, ?> parameters) {
        return namedParameterJdbcTemplate.update(sql, new MapSqlParameterSource(parameters));
    }

    protected List<DbRow> list(String sql, Object... arguments) {
        return jdbcTemplate.query(sql, new DbRowMapper(), arguments);
    }

    protected List<DbRow> list(String sql, Map<String, ?> parameters) {
        return namedParameterJdbcTemplate.query(sql, parameters, new DbRowMapper());
    }

    protected <T> List<T> list(String sql, RowMapper<T> mapper, Map<String, ?> parameters) {
        return namedParameterJdbcTemplate.query(sql, parameters, mapper);
    }

    protected <T> List<T> list(String sql, RowMapper<T> mapper, Object... arguments) {
        return jdbcTemplate.query(sql, arguments, mapper);
    }

    protected DbRow one(String sql, Map<String, ?> parameters) {
        return namedParameterJdbcTemplate.queryForObject(sql, parameters, new DbRowMapper());
    }

    protected <T> T one(String sql, RowMapper<T> mapper, Object... arguments) {
        return jdbcTemplate.queryForObject(sql, mapper, arguments);
    }

    protected <T> Optional<T> tryOne(String sql, RowMapper<T> mapper, Object... arguments) {
        try {
            return Optional.ofNullable(one(sql, mapper, arguments));
        }
        catch (IncorrectResultSizeDataAccessException ignore) {
            return Optional.empty();
        }
    }

    protected <T> Optional<T> trySingleResult(List<T> list) {
        if (list.size() == 1) {
            return Optional.of(list.get(0));
        }

        return Optional.empty();
    }
}
