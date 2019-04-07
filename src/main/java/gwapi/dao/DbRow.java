package gwapi.dao;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Map;

import org.springframework.jdbc.core.ColumnMapRowMapper;
import org.springframework.util.LinkedCaseInsensitiveMap;

/**
 * A more-nice-to-work-with-substitute for regular {@link Map} returned by {@link ColumnMapRowMapper}.
 */
public class DbRow extends LinkedCaseInsensitiveMap<Object> {

  public DbRow(int initialCapacity) {
    super(initialCapacity);
  }

  public Long getLong(String columnLabel) {
    return getValue(columnLabel);
  }

  public BigDecimal getBigDecimal(String columnLabel) {
    return getValue(columnLabel);
  }

  public Integer getInteger(String columnLabel) {
    return getValue(columnLabel);
  }

  public String getString(String columnLabel) {
    return getValue(columnLabel);
  }

  public Boolean getBoolean(String columnLabel) {
    return getValue(columnLabel);
  }

  public LocalDateTime getLocalDateTime(String columnLabel) {
    return getValue(columnLabel);
  }

  @SuppressWarnings("unchecked")
  public <T> T getValue(String columnLabel) {
    return (T) get(columnLabel);
  }
}
