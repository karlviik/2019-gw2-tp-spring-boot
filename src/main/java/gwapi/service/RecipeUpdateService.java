package gwapi.service;

import gwapi.dao.RecipeDao;
import gwapi.entity.Recipe;
import gwapi.entity.RecipeComponent;
import gwapi.entity.RecipeDiscipline;
import gwapi.entity.RecipeType;
import gwapi.web.apiresponse.IdListApiResponse;
import gwapi.web.apiresponse.RecipePageApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static java.time.ZoneOffset.UTC;

@Component
public class RecipeUpdateService {

  @Autowired
  private RestTemplate restTemplate;

  @Autowired
  private RecipeDao recipeDao;

  /**
   * @return false if no recipes added, true otherwise
   */
  public void addNewRecipes() {
    ResponseEntity<IdListApiResponse> allApiRecipeIdsResponse = restTemplate.exchange(
        "https://api.guildwars2.com/v2/recipes",
        HttpMethod.GET,
        null,
        IdListApiResponse.class
    );

    List<Integer> apiRecipeIds = allApiRecipeIdsResponse.getBody();
    List<Integer> databaseRecipeIds = recipeDao.getAllRecipeIds();
    for (Integer dbId : databaseRecipeIds) {
      apiRecipeIds.remove(dbId);
    }
    if (apiRecipeIds.size() == 0) {
      return;
    }
    for (int i = 0; i < (int) Math.ceil(apiRecipeIds.size() / 200.0); i++) {
      StringBuilder sb = new StringBuilder();
      sb.append("https://api.guildwars2.com/v2/recipes?ids=");
      int end = Math.min((i + 1) * 200, apiRecipeIds.size());
      for (int j = i * 200; j < end; j++) {
        sb.append(apiRecipeIds.get(j));
        if (j < end - 1) {
          sb.append(",");
        }
      }

      ResponseEntity<RecipePageApiResponse> recipePageApiResponse = restTemplate.exchange(
          sb.toString(),
          HttpMethod.GET,
          null,
          RecipePageApiResponse.class
      );

      recipePageApiResponse.getBody().stream()
          .map(this::mapRecipe)
          .forEach(this::addNewRecipeToDatabase);
    }
    updateRecipeCalculationOrder();
  }

  public void updateAllRecipes() {
    LocalDateTime time = LocalDateTime.now(UTC);

    ResponseEntity<IdListApiResponse> idCount = restTemplate.exchange(
        "https://api.guildwars2.com/v2/recipes",
        HttpMethod.GET,
        null,
        IdListApiResponse.class);
    int pageAmount = (int) Math.ceil(idCount.getBody().size() / 200.0);

    for (int i = 0; i < pageAmount; i++) {
      ResponseEntity<RecipePageApiResponse> result = restTemplate.exchange(
          "https://api.guildwars2.com/v2/recipes?page_size=200&page=" + i,
          HttpMethod.GET,
          null,
          RecipePageApiResponse.class);

      result.getBody()
          .stream()
          .map(this::mapRecipe)
          .forEach(recipe -> recipeDao.createOrUpdate(recipe, time));
    }
    updateRecipeCalculationOrder();
  }

  private void addNewRecipeToDatabase(Recipe recipe) {
    recipeDao.createRecipeIfNew(
        recipe.getRecipeId(),
        Timestamp.valueOf(recipe.getOverwriteTime()),
        recipe.getType().name(),
        recipe.getMinRating(),
        recipe.isLearnedFromItem(),
        recipe.getChatLink(),
        recipe.getOutItemId(),
        recipe.getOutItemCount()
    );
    for (RecipeDiscipline discipline : recipe.getDisciplines()) {
      recipeDao.addDisciplineOrNothing(
          recipe.getRecipeId(),
          discipline.name()
      );
    }
    for (RecipeComponent component : recipe.getComponents()) {
      recipeDao.addComponentIfNew(
          recipe.getRecipeId(),
          component.getId(),
          component.getCount()
      );
    }
  }


  // TODO: refactor this
  private Recipe mapRecipe(RecipePageApiResponse.RecipeResponse recipeResponse) {
    ArrayList<RecipePageApiResponse.RecipeResponse.Ingredient> ingredients = recipeResponse.getIngredients();
    List<RecipeComponent> components = new ArrayList<>();
    for (RecipePageApiResponse.RecipeResponse.Ingredient ingredient : ingredients) {
      components.add(new RecipeComponent(ingredient.getInId(), ingredient.getInCount()));
    }

    boolean learnedFromItem = recipeResponse.getFlags().contains("LearnedFromItem");

    List<RecipeDiscipline> disciplines = recipeResponse.getDisciplines().stream()
        .map(x -> RecipeDiscipline.valueOf(x.toUpperCase()))
        .collect(Collectors.toList());

    return new Recipe(
        recipeResponse.getId(),
        null,
        RecipeType.valueOf(recipeResponse.getType().toUpperCase()),
        recipeResponse.getMinRating(),
        learnedFromItem,
        recipeResponse.getChatLink(),
        recipeResponse.getOutId(),
        recipeResponse.getOutCount(),
        disciplines,
        components);
  }

  public void updateRecipeCalculationOrder() {
    recipeDao.resetCalculationLevel();

    int level = 1;

    while (recipeDao.updateCalculationLevel(level)) {
      level++;
    }
  }
}
