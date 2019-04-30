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

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.*;

import static java.time.ZoneOffset.UTC;

// TODO: fix it all
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
    System.out.println("Updated all TP prices");
    getCraftPricePoints(time);
    System.out.println("Updated all craft prices");
  }

  private void getCraftPricePoints(LocalDateTime time) {
    int level = 1;
    while (true) {
      List<Recipe> recipes = recipeDao.searchCurrentByCalculationLevel(level);
      if (recipes == null || recipes.size() == 0) {
        break;
      }
      for (Recipe recipe : recipes) {
        Integer craftBuy = 0;
        Integer craftSell = 0;
        for (RecipeComponent component : recipe.getComponents()) {
          Optional<Price> componentPriceOptional = priceDao.getTradePostAndCraftPrice(component.getId(), time);
          if (componentPriceOptional.isPresent()) {
            Price componentPrice = componentPriceOptional.get();
            craftBuy += customMinPrice(componentPrice.getBuyPrice(), componentPrice.getCraftBuyPrice()) * component.getCount();
            craftSell += customMinPrice(componentPrice.getSellPrice(), componentPrice.getCraftSellPrice()) * component.getCount();
          }
        }
        if (craftBuy == 0) {
          craftBuy = null;
        } else {
          craftBuy /= recipe.getOutItemCount();
        }
        if (craftSell == 0) {
          craftSell = null;
        } else {
          craftSell /= recipe.getOutItemCount();
        }
        priceDao.addCraftData(recipe.getOutItemId(), Timestamp.valueOf(time), craftBuy, craftSell);
      }
      level++;
    }
  }

  private int customMinPrice(Integer a, Integer b) {
    if (a == null && b == null) {
      return 0;
    }
    else if (a == null) {
      return b;
    }
    else if (b == null) {
      return a;
    }
    return Math.min(a, b);
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
        .forEach(this::addCurrentTradePostPriceToDatabase);
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

  private void addCurrentTradePostPriceToDatabase(Price price) {
    priceDao.addTradePostData(
      price.getId(),
      Timestamp.valueOf(price.getTime()),
      price.getBuyPrice(),
      price.getBuyQuantity(),
      price.getSellPrice(),
      price.getSellQuantity());
  }
}
