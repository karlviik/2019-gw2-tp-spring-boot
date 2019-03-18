package gwapi.service;

import gwapi.dao.RecipesDao;
import gwapi.entity.Recipe;
import gwapi.entity.RecipeType;
import gwapi.web.apiresponse.AllIdsApiResponse;
import gwapi.web.apiresponse.RecipesPageApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.ArrayList;

import static java.time.ZoneOffset.UTC;

@Component
public class RecipesUpdateService {

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private RecipesDao recipesDao;

    public void getRecipes() {
        LocalDateTime time = LocalDateTime.now(UTC);

        ResponseEntity<AllIdsApiResponse> idCount = restTemplate.exchange(
                "https://api.guildwars2.com/v2/recipes",
                HttpMethod.GET,
                null,
                AllIdsApiResponse.class);
        int pageAmount = (int) Math.round(idCount.getBody().size() / 200.0);

        for (int i = 0; i < pageAmount; i++) {
            ResponseEntity<RecipesPageApiResponse> result = restTemplate.exchange(
                    "https://api.guildwars2.com/v2/commerce/recipes?page_size=200&page=" + i,
                    HttpMethod.GET,
                    null,
                    RecipesPageApiResponse.class);

            result.getBody()
                    .stream()
                    .map(recipesResponse -> map(recipesResponse))
                    .forEach(recipe -> recipesDao.create(recipe));
        }


    }

    private Recipe map(RecipesPageApiResponse.RecipeResponse recipeResponse) {
        ArrayList<RecipesPageApiResponse.RecipeResponse.Ingredient> ingredients = recipeResponse.getIngredients();

        return new Recipe(
                recipeResponse.getId(),
                null,
                RecipeType.valueOf(recipeResponse.getType().toUpperCase()),
                recipeResponse.getMinRating(),
                recipeResponse.getFlags().contains("LearnedFromItem"),
                recipeResponse.getChatLink(),
                recipeResponse.getOutId(),
                recipeResponse.getOutCount(),
                ingredients.size() >= 1 ? ingredients.get(0).getInId() : null,
                ingredients.size() >= 1 ? ingredients.get(0).getInCount() : null,
                ingredients.size() >= 2 ? ingredients.get(1).getInId() : null,
                ingredients.size() >= 2 ? ingredients.get(1).getInCount() : null,
                ingredients.size() >= 3 ? ingredients.get(2).getInId() : null,
                ingredients.size() >= 3 ? ingredients.get(2).getInCount() : null,
                ingredients.size() >= 4 ? ingredients.get(3).getInId() : null,
                ingredients.size() >= 4 ? ingredients.get(3).getInCount() : null);
    }
}
