package gwapi.service;

import gwapi.dao.PriceDao;
import gwapi.dao.RecipeDao;
import gwapi.entity.Price;
import gwapi.entity.Recipe;
import gwapi.entity.RecipeComponent;
import gwapi.web.apiresponse.IdListApiResponse;
import gwapi.web.apiresponse.PricePageApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import java.lang.Math;

import java.time.LocalDateTime;
import java.util.*;

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
            if (recipeOutPrice != null && (recipeOutPrice.getCraftBuyPrice() != null || recipeOutPrice.getCraftSellPrice() != null)) {
                continue;
            }
            calculateCraftingCost(recipe.getOutItemId(), time);

        }
    }

    private List<Integer> calculateCraftingCost(Integer id, LocalDateTime time) {
        // input should be item ID
        // if id has no recipes where it is the output, give back
        // null
        Price itemPrice = priceDao.getTradePostAndCraftPrice(id, time);
        if (itemPrice != null && (itemPrice.getCraftBuyPrice() != null || itemPrice.getCraftSellPrice() != null)) {
            LinkedList<Integer> returnList = new LinkedList<>();
            returnList.add(itemPrice.getCraftBuyPrice());
            returnList.add(itemPrice.getCraftSellPrice());
            return returnList;
        }
        List<Recipe> associatedRecipes = recipeDao.searchCurrentByOutId(id);
        if (associatedRecipes == null || associatedRecipes.size() == 0) {
            return null;
        }
        ArrayList<Integer> recipeBuyCosts = new ArrayList<>();
        ArrayList<Integer> recipeSellCosts = new ArrayList<>();

        for (Recipe recipe : associatedRecipes) {
            Integer costToBuyCraft = 0;
            Integer costToSellCraft = 0;
            for (RecipeComponent component : recipe.getComponents()) {
                Price componentPrice = priceDao.getTradePostAndCraftPrice(component.getId(), time);
                if (componentPrice == null) {
                    List<Integer> componentCraftCost = calculateCraftingCost(component.getId(), time);
                    if (componentCraftCost == null) {
                        continue;
                    } else {
                        costToBuyCraft += componentCraftCost.get(0) != null ? componentCraftCost.get(0) : 0;
                        costToSellCraft += componentCraftCost.get(1) != null ? componentCraftCost.get(1) : 0;
                        continue;
                    }
                } else {
                    List<Integer> componentCraftCost = calculateCraftingCost(component.getId(), time);
                    if (componentPrice.getCraftBuyPrice() != null && componentPrice.getBuyPrice() != null) {
                        costToBuyCraft += Math.min(componentPrice.getCraftBuyPrice(), componentPrice.getBuyPrice()) * component.getCount();
                    } else if (componentPrice.getCraftBuyPrice() != null) {
                        costToBuyCraft += componentPrice.getCraftBuyPrice() * component.getCount();
                    } else {
                        if (componentPrice.getBuyPrice() == null) {
                            costToBuyCraft += (componentCraftCost != null ? componentCraftCost.get(0) != null ? componentCraftCost.get(0) : 0 : 0) * component.getCount();
                        } else {
                            if (componentCraftCost == null || componentCraftCost.get(0) == null) {
                                costToBuyCraft += componentPrice.getBuyPrice() * component.getCount();
                            } else {
                                costToBuyCraft += Math.min(componentPrice.getBuyPrice(), componentCraftCost.get(0)) * component.getCount();
                            }
                        }
                    }
                    if (componentPrice.getCraftSellPrice() != null && componentPrice.getSellPrice() != null) {
                        costToSellCraft += Math.min(componentPrice.getCraftSellPrice(), componentPrice.getSellPrice()) * component.getCount();
                    } else if (componentPrice.getCraftSellPrice() != null) {
                        costToSellCraft += componentPrice.getCraftSellPrice() * component.getCount();
                    } else {
                        if (componentPrice.getSellPrice() == null) {
                            costToSellCraft += (componentCraftCost != null ? componentCraftCost.get(1) != null ? componentCraftCost.get(1) : 0 : 0) * component.getCount();
                        } else {
                            if (componentCraftCost == null || componentCraftCost.get(1) == null) {
                                costToSellCraft += componentPrice.getSellPrice() * component.getCount();
                            } else {
                                costToSellCraft += Math.min(componentPrice.getSellPrice(), componentCraftCost.get(1)) * component.getCount();
                            }
                        }
                    }
                }
            }
            if (costToBuyCraft != 0) {
                recipeBuyCosts.add(costToBuyCraft / recipe.getOutItemCount());
            }
            if (costToSellCraft != 0) {
                recipeSellCosts.add(costToSellCraft / recipe.getOutItemCount());
            }
        }
        LinkedList<Integer> returnList = new LinkedList<>();
        Integer buyCraft = recipeBuyCosts.size() != 0 ? Collections.min(recipeBuyCosts) : null;
        Integer sellCraft = recipeSellCosts.size() != 0 ? Collections.min(recipeSellCosts) : null;
        returnList.add(buyCraft);
        returnList.add(sellCraft);
        priceDao.addCraftData(id, time, buyCraft, sellCraft);
        System.out.println(id + " " + time + " " + buyCraft + " " + sellCraft);
        return returnList;

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
