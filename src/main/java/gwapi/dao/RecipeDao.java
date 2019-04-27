package gwapi.dao;

import gwapi.entity.Recipe;
import gwapi.entity.RecipeComponent;
import gwapi.entity.RecipeDiscipline;
import gwapi.entity.RecipeType;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static java.util.function.Predicate.isEqual;

@Component
public class RecipeDao extends JdbcDao {

  // used to create new listing for a recipe that most definitely does not exist yet
  public void createNew(Recipe recipe) {
    createRecipeIfNew(
        recipe.getRecipeId(),
        null,
        recipe.getType().name(),
        recipe.getMinRating(),
        recipe.isLearnedFromItem(),
        recipe.getChatLink(),
        recipe.getOutItemId(),
        recipe.getOutItemCount()
    );
    for (RecipeDiscipline discipline : recipe.getDisciplines()) {
      addDisciplineOrNothing(recipe.getRecipeId(), discipline.name());
    }
    for (RecipeComponent component : recipe.getComponents()) {
      addComponentIfNew(recipe.getRecipeId(), component.getId(), component.getCount());
    }
  }

  public void createRecipeIfNew(
      Integer id,
      Timestamp updatedAt,
      String type,
      Integer minRating,
      Boolean learnedFromItem,
      String chatLink,
      Integer outItemId,
      Integer outItemCount
  ) {
    update(
        "INSERT INTO recipe(id, updated_at, type, min_rating, learned_from_item, chat_link, out_item_id, out_item_count, calculation_level) " +
            "VALUES (?, ?, ?, ?, ?, ?, ?, ?, 1) " +
            "ON CONFLICT DO NOTHING",
        id, updatedAt, type, minRating, learnedFromItem, chatLink, outItemId, outItemCount);
  }

  public void addDisciplineOrNothing(Integer id, String discipline) {
    update(
        "INSERT INTO recipe_discipline(recipe_id, discipline) " +
            "VALUES (?, ?) " +
            "ON CONFLICT (recipe_id, discipline) DO NOTHING",
        id, discipline
    );
  }

  public void addComponentIfNew(Integer recipeId, Integer componentId, Integer componentCount) {
    update(
        "INSERT INTO recipe_component(recipe_id, updated_at, component_item_id, component_item_count) " +
            "VALUES (?, ?, ?, ?) " +
            "ON CONFLICT DO NOTHING",
        recipeId, null, componentId, componentCount
    );
  }

  public void setRecipeUpdateTime(Integer recipeId, Timestamp updatedAt) {
    update(
        "UPDATE recipe " +
        "SET updated_at=? " +
        "WHERE id=? AND updated_at IS NULL",
        updatedAt, recipeId
    );
  }

  public void setComponentUpdateTime(Integer recipeId, Timestamp updatedAt) {
    update(
        "UPDATE recipe_component " +
        "SET updated_at=? " +
        "WHERE recipe_id=? AND updated_at IS NULL",
        updatedAt, recipeId
    );
  }

  // used to either create a new recipe if it's not already
  // in the recipes list OR if it is, then it checks if
  // this recipe and the one in the database match up.
  // if matches up, does nothing
  // if doesn't, then updates the current recipe + components in database with overwrite time
  // and also adds the new recipe
  public void createOrUpdate(Recipe recipe, LocalDateTime time) {
    Optional<Recipe> optionalRecipe = searchCurrentByRecipeId(recipe.getRecipeId());
    if (optionalRecipe.filter(isEqual(recipe)).isPresent()) {
      return;
    }
    if (optionalRecipe.isPresent()) {
      setRecipeUpdateTime(recipe.getRecipeId(), Timestamp.valueOf(time));
      setComponentUpdateTime(recipe.getRecipeId(), Timestamp.valueOf(time));
    }
    createNew(recipe);
  }

  public Optional<Recipe> searchCurrentByRecipeId(int recipeId) {
    List<DbRow> components = list(
        "SELECT recipe_id, component_item_id, component_item_count " +
            "FROM recipe_component " +
            "WHERE recipe_id=? AND updated_at IS NULL",
        recipeId);
    return tryOne(
        "SELECT id, updated_at, type, min_rating, learned_from_item, chat_link, out_item_id, out_item_count " +
            "FROM recipe " +
            "WHERE id=? AND updated_at IS NULL",
        (rs, i) -> mapRecipe(rs, components),
        recipeId);
  }

  public List<Integer> getAllRecipeIds() {
    return list("SELECT id FROM recipe").stream()
        .map(result -> result.getInteger("id"))
        .collect(Collectors.toList());
  }

  public List<Recipe> searchCurrentByCalculationLevel(int level) {
    List<DbRow> components = list(
        "SELECT recipe_id, component_item_id, component_item_count " +
            "FROM recipe_component " +
            "WHERE updated_at IS NULL");

    return list(
        "SELECT id, updated_at, type, min_rating, learned_from_item, chat_link, out_item_id, out_item_count " +
            "FROM recipe " +
            "WHERE calculation_level=? AND updated_at IS NULL",
        (rs, i) -> mapRecipe(rs, components),
        level);
  }

  public void resetCalculationLevel() {
    update("UPDATE recipe SET calculation_level = 1");
  }

  public boolean updateCalculationLevel(int level) {
    return update("UPDATE recipe " +
        "SET calculation_level = ? " +
        "WHERE id IN (" +
        "  SELECT rc.recipe_id " +
        "  FROM recipe r " +
        "  JOIN recipe_component rc ON r.out_item_id = rc.component_item_id " +
        "  WHERE r.calculation_level = ?)", level + 1, level) > 0;
  }

  public HashSet<Integer> getAllOutIds() {
    return list("SELECT out_item_id FROM recipe").stream()
        .map(x -> x.getInteger("out_item_id"))
        .collect(Collectors.toCollection(HashSet::new));
  }

  private RecipeComponent mapComponent(DbRow row) {
    return new RecipeComponent(
        row.getInteger("component_item_id"),
        row.getInteger("component_item_count")
    );
  }

  private Recipe mapRecipe(ResultSet rs, List<DbRow> components) throws SQLException {
    int recipeId = rs.getInt("id");
    return new Recipe(
        recipeId,
        rs.getObject("updated_at", LocalDateTime.class),
        RecipeType.valueOf(rs.getString("type")),
        rs.getInt("min_rating"),
        rs.getBoolean("learned_from_item"),
        rs.getString("chat_link"),
        rs.getInt("out_item_id"),
        rs.getInt("out_item_count"),
        null,
        components.stream()
        .filter(row -> row.getInteger("recipe_id").equals(recipeId))
        .map(this::mapComponent)
        .collect(Collectors.toList())
    );
  }
}
