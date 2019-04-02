package gwapi.service;

import gwapi.dao.PriceDao;
import gwapi.dao.RecipeDao;
import gwapi.entity.Price;
import gwapi.entity.Recipe;
import gwapi.web.apiresponse.IdListApiResponse;
import gwapi.web.apiresponse.PricePageApiResponse;
import jdk.vm.ci.meta.Local;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import java.lang.Math;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static java.time.ZoneOffset.UTC;

/**
 * Should just have one method that gets the prices and inserts them into the database, maybe make scheduler get the
 * timestamp and feed it to the updater and then feed the same thing to recipe cprice calculator service
 */
@Component
public class PriceUpdateService {

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private PriceDao priceDao;

    @Autowired
    private RecipeDao recipeDao;

    public void updatePrices() {
        LocalDateTime time = LocalDateTime.now(UTC);
        getTradePostPricePoints(time);
        getCraftPricePoints(time);
    }

    private void getCraftPricePoints(LocalDateTime time) {
        ArrayList<Integer> allRecipeIds = new ArrayList<>(recipeDao.getAllRecipeIds());
        for (Integer id : allRecipeIds) {
            Recipe recipe = recipeDao.searchCurrentByRecipeId(id).get(0);
            Price recipeOutPrice = priceDao.getTradePostAndCraftPrice(recipe.getOutItemId(), time);
            if (recipeOutPrice.getCraftBuyPrice() != null || recipeOutPrice.getCraftSellPrice() != null) {
                continue;
            }

        }
    }

    private List<Integer> calculateCraftingCost(Integer id, LocalDateTime time) {
        // input should be item ID
        // if id has no recipes where it is the output, give back
        // null
        List<Recipe> associatedRecipes = recipeDao.searchCurrentByOutId(id);

        // otherwise calculate costs of all the recipes related to the item
        // and for each component in the thing, ask for minimal of
        // craft and trade post, but the function gives back only craft

        // result of calculation should be minimal cost of crafting the item
        // one for the buy price, other sell price
        // before returning those values, the thing should also add them
        // to the database
    }

    private void getTradePostPricePoints(LocalDateTime time) {

        ResponseEntity<IdListApiResponse> idCount = restTemplate.exchange(
                "https://api.guildwars2.com/v2/commerce/prices",
                HttpMethod.GET,
                null,
                IdListApiResponse.class);
        int pageAmount = (int) Math.ceil(idCount.getBody().size() / 200.0);

        for (int i = 0; i < pageAmount; i++) {
            ResponseEntity<PricePageApiResponse> result = restTemplate.exchange(
                    "https://api.guildwars2.com/v2/commerce/prices?page_size=200&page=" + i,
                    HttpMethod.GET,
                    null,
                    PricePageApiResponse.class);

            result.getBody()
                    .stream()
                    .map(priceResponse -> map(priceResponse, time))
                    .forEach(price -> priceDao.addTradePostData(price));
        }


    }

    private Price map(PricePageApiResponse.PriceResponse priceResponse, LocalDateTime time) {
        return new Price(
                priceResponse.getId(),
                time,
                priceResponse.getBuys().getUnitPrice(),
                priceResponse.getBuys().getQuantity(),
                priceResponse.getSells().getUnitPrice(),
                priceResponse.getSells().getQuantity());
    }
}
