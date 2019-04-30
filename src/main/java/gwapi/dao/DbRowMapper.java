package gwapi.dao;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;

import org.springframework.jdbc.core.ColumnMapRowMapper;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.JdbcUtils;

/**
 * Mostly copy of {@link ColumnMapRowMapper}.
 * Returns {@link LocalDateTime} instead of {@link Timestamp}.
 * Returns {@link LocalDate} instead of {@link Date}.
 *
 * @author Henri Viik
 */
public class DbRowMapper implements RowMapper<DbRow> {

  @Override
  public DbRow mapRow(ResultSet rs, int rowNum) throws SQLException {
    ResultSetMetaData rsmd = rs.getMetaData();
    int columnCount = rsmd.getColumnCount();
    DbRow mapOfColValues = new DbRow(columnCount);

    for (int i = 1; i <= columnCount; i++) {
      String key = JdbcUtils.lookupColumnName(rsmd, i);
      Object obj = getColumnValue(rs, i);
      mapOfColValues.put(key, obj);
    }

    return mapOfColValues;
  }

  private Object getColumnValue(ResultSet rs, int index) throws SQLException {
    return JdbcUtils.getResultSetValue(rs, index);
  }
}
