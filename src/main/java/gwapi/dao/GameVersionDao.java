package gwapi.dao;

import org.springframework.stereotype.Component;

@Component
public class GameVersionDao extends JdbcDao {

  public Integer getVersion() {
    return one("SELECT version FROM game_version", (resultSet, i) -> resultSet.getInt("version"));
  }

  public void setVersion(int version) {
    update("UPDATE game_version SET version = ?", version);
  }
}
