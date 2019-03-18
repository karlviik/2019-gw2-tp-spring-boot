package gwapi.dao;

import gwapi.entity.Recipe;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class RecipesDao extends JdbcDao {

    public void create(Recipe recipe) {
        update("INSERT INTO recipes (id, overwrite_time, type, min_rating, learned_from_item, chat_link, out_id, out_count, in_id_1, in_count_1, in_id_2, in_count_2, in_id_3, in_count_3, in_id_4, in_count_4) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)", recipe.getRecipeId(), recipe.getOverwriteTime(), recipe.getType(), recipe.getMinRating(), recipe.isLearnedFromItem(), recipe.getChatLink(), recipe.getOutId(), recipe.getOutCount(), recipe.getInId1(), recipe.getInCount1(), recipe.getInId2(), recipe.getInCount2(), recipe.getInId3(), recipe.getInCount3(), recipe.getInId4(), recipe.getInCount4());
    }

    public List<Recipe> searchOutIdCurrentOnlyComponents(int outId) {
        return list("SELECT out_id, out_count, in_id_1, in_count_1, in_id_2, in_count_2, in_id_3, in_count_3, in_id_4, in_count_4 FROM prices WHERE out_id=? AND overwrite_time IS NULL", outId).stream()
                .map(rs -> map(rs))
                .collect(Collectors.toList());
    }

    private Recipe map(DbRow row) {
        return new Recipe(
                row.getInteger("out_id"),
                row.getInteger("out_count"),
                row.getInteger("in_id_1"),
                row.getInteger("in_count_1"),
                row.getInteger("in_id_2"),
                row.getInteger("in_count_2"),
                row.getInteger("in_id_3"),
                row.getInteger("in_count_3"),
                row.getInteger("in_id_4"),
                row.getInteger("in_count_4")
        );
    }
}
