package gwapi.dao;

import org.springframework.stereotype.Component;

/**
 * Database Access Object for interacting with table "game_version"
 */
@Component
public class GameVersionDao extends JdbcDao {

  /**
   * Get current version of game.
   *
   * @return version
   */
  public Integer getVersion() {
    return one("SELECT version FROM game_version", (resultSet, i) -> resultSet.getInt("version"));
  }

  /**
   * Updates current version.
   *
   * @param version new version
   */
  public void setVersion(int version) {
    update("UPDATE game_version SET version = ?", version);
  }
}
