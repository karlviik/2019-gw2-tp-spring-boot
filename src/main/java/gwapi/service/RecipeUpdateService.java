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

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

import static java.time.ZoneOffset.UTC;

/**
 * should have a method to:
 * a) update absolutely all entries while paying attention to if recipe has changed, in those cases properly add
 * overwrite time to recipe and components
 * b) add new entries (like when no build change)
 */
@Component
public class RecipeUpdateService {

  @Autowired
  private RestTemplate restTemplate;

  @Autowired
  private RecipeDao recipeDao;

  public void addNewRecipes() {
    ResponseEntity<IdListApiResponse> allApiRecipeIdsResponse = restTemplate.exchange(
        "https://api.guildwars2.com/v2/recipes",
        HttpMethod.GET,
        null,
        IdListApiResponse.class);
    LinkedList<Integer> allApiRecipeIds = new LinkedList<>(allApiRecipeIdsResponse.getBody());
    LinkedList<Integer> allDatabaseRecipeIds = new LinkedList<>(recipeDao.getAllRecipeIds());
    for (Integer dbId : allDatabaseRecipeIds) {
      allApiRecipeIds.remove(dbId);
    }
    if (allApiRecipeIds.size() == 0) {
      return;
    }
    for (int i = 0; i < (int) Math.ceil(allApiRecipeIds.size() / 200.0); i++) {
      StringBuilder sb = new StringBuilder();
      sb.append("https://api.guildwars2.com/v2/recipes?ids=");
      int end = Math.min((i + 1) * 200, allApiRecipeIds.size());
      for (int j = i * 200; j < end; j++) {
        sb.append(allApiRecipeIds.get(j));
        if (j < end - 1) {
          sb.append(",");
        }
      }
      ResponseEntity<RecipePageApiResponse> recipePageApiResponse = restTemplate.exchange(
          sb.toString(),
          HttpMethod.GET,
          null,
          RecipePageApiResponse.class);
      recipePageApiResponse.getBody().stream()
          .map(this::mapRecipe)
          .forEach(recipe -> recipeDao.createNew(recipe));
    }
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
  }

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
}
