package gwapi.dao;

import gwapi.entity.Recipe;
import gwapi.entity.RecipeComponent;
import gwapi.entity.RecipeDiscipline;
import gwapi.entity.RecipeType;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Component
public class RecipeDao extends JdbcDao {

  // used to create new listing for a recipe that most definitely does not exist yet
  public void createNew(Recipe recipe) {
    update("INSERT INTO recipe(id, updated_at, type, min_rating, learned_from_item, chat_link, out_item_id, out_item_count) VALUES (?, ?, ?, ?, ?, ?, ?, ?)", recipe.getRecipeId(), recipe.getOverwriteTime(), Objects.toString(recipe.getType()), recipe.getMinRating(), recipe.isLearnedFromItem(), recipe.getChatLink(), recipe.getOutItemId(), recipe.getOutItemCount());
    for (RecipeDiscipline discipline : recipe.getDisciplines()) {
      update("INSERT INTO recipe_discipline(recipe_id, discipline) VALUES (?, ?) ON CONFLICT DO NOTHING", recipe.getRecipeId(), Objects.toString(discipline));
    }
    for (RecipeComponent component : recipe.getComponents()) {
      update("INSERT INTO recipe_component(recipe_id, updated_at, component_item_id, component_item_count) VALUES (?, ?, ?, ?)", recipe.getRecipeId(), null, component.getId(), component.getCount());
    }
  }

  public void createNewRecipe(
      Integer id,
      Timestamp updatedAt,
      String type,
      Integer minRating,
      Boolean learnedFromItem,
      String chatLink,
      Integer outItemId,
      Integer outItemCount
  ) {
    update("INSERT INTO recipe(id, updated_at, type, min_rating, learned_from_item, chat_link, out_item_id, out_item_count) VALUES (?, ?, ?, ?, ?, ?, ?, ?)", id, updatedAt, type, minRating, learnedFromItem, chatLink, outItemId, outItemCount);
  }

  public void addDisciplineOrNothing(Integer id, String discipline) {
    update("INSERT INTO recipe_discipline(recipe_id, discipline) VALUES (?, ?) ON CONFLICT (recipe_id, discipline) DO NOTHING", id, discipline);
  }

  public void addNewComponent(Integer recipeId, Integer componentId, Integer componentCount) {
    update("INSERT INTO recipe_component(recipe_id, updated_at, component_item_id, component_item_count) VALUES (?, ?, ?, ?)", recipeId, null, componentId, componentCount);
  }

  public void setRecipeIdUpdateTime() {

  }

  // used to either create a new recipe if it's not already
  // in the recipes list OR if it is, then it checks if
  // this recipe and the one in the database match up.
  // if matches up, does nothing
  // if doesn't, then updates the current recipe + components in database with overwrite time
  // and also adds the new recipe
  public void createOrUpdate(Recipe recipe, LocalDateTime time) {
    List<Recipe> currentRecipes = searchCurrentByRecipeId(recipe.getRecipeId());
    if (currentRecipes.size() == 0) {
      createNew(recipe);
    }
    else {
      Recipe currentRecipe = currentRecipes.get(0);
      if (!currentRecipe.equals(recipe)) {
        update("UPDATE recipe SET updated_at=? WHERE id=? AND updated_at IS NULL", Timestamp.valueOf(time), recipe.getRecipeId());
        update("UPDATE recipe_component SET updated_at=? WHERE recipe_id=? AND updated_at IS NULL", Timestamp.valueOf(time), recipe.getRecipeId());
        createNew(recipe);
      }
    }
  }

  public List<Recipe> searchCurrentByRecipeId(int recipeId) {
    List<RecipeComponent> components = list("SELECT component_item_id, component_item_count FROM recipe_component WHERE recipe_id=? AND updated_at IS NULL", recipeId).stream()
        .map(result -> mapComponent(result))
        .collect(Collectors.toList());
    return list("SELECT id, updated_at, type, min_rating, learned_from_item, chat_link, out_item_id, out_item_count FROM recipe WHERE id=? AND updated_at IS NULL", recipeId).stream()
        .map(result -> mapRecipeWithComponents(result, components))
        .collect(Collectors.toList());
  }

  public List<Integer> getAllRecipeIds() {
    return list("SELECT id FROM recipe").stream()
        .map(result -> result.getInteger("id"))
        .collect(Collectors.toList());
  }

  public List<Recipe> searchCurrentByOutId(int outId) {
    List<DbRow> listOfResults = list("SELECT id, updated_at, type, min_rating, learned_from_item, chat_link, out_item_id, out_item_count FROM recipe WHERE out_item_id=? AND updated_at IS NULL", outId);
    ArrayList<Recipe> recipes = new ArrayList<>();
    for (DbRow row : listOfResults) {
      Integer recipeId = row.getInteger("id");
      List<RecipeComponent> components = list("SELECT component_item_id, component_item_count FROM recipe_component WHERE recipe_id=? AND updated_at IS NULL", recipeId).stream()
          .map(result -> mapComponent(result))
          .collect(Collectors.toList());
      recipes.add(mapRecipeWithComponents(row, components));
    }
    return recipes;
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

  private Recipe mapRecipeWithComponents(DbRow row, List<RecipeComponent> components) {
    return new Recipe(
        row.getInteger("id"),
        row.getLocalDateTime("updated_at"),
        RecipeType.valueOf(row.getString("type")),
        row.getInteger("min_rating"),
        row.getBoolean("learned_from_item"),
        row.getString("chat_link"),
        row.getInteger("out_item_id"),
        row.getInteger("out_item_count"),
        null,
        components
    );
  }
}
