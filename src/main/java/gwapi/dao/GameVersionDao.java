package gwapi.dao;

import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
public class GameVersionDao extends JdbcDao {

  public Integer getVersion() {
    return list("SELECT version FROM game_version").stream()
        .map(result -> result.getInteger("version"))
        .collect(Collectors.toList())
        .get(0);
  }

  public void updateVersion(int newVersion) {
    update("UPDATE game_version SET version=?", newVersion);
  }
}
