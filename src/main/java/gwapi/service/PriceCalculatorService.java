package gwapi.service;

import gwapi.dao.PriceDao;
import gwapi.dao.RecipeDao;
import gwapi.entity.Price;
import gwapi.entity.Recipe;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Component
public class PriceCalculatorService {

    @Autowired
    PriceDao priceDao;

    @Autowired
    RecipeDao recipeDao;

//    public Integer[] calculateCost(Recipe recipe, LocalDateTime time) {
//
//        System.out.println("calculating");
//
//        Price itemPrice = priceDao.searchIdAndTime(recipe.getOutId(), time);
//
//        if (itemPrice != null && itemPrice.getCraftBuyPrice() != null && itemPrice.getCraftSellPrice() != null) {
//            return new Integer[] {itemPrice.getCraftBuyPrice(), itemPrice.getCraftSellPrice()};
//        }
//
//        int buyCost = 0;
//        int sellCost = 0;
//
//        ArrayList<Integer[]> components = recipe.getComponents();
//        for (Integer[] component : components) {
//            Price componentPrice = priceDao.searchIdAndTime(component[0], time);
//            List<Recipe> componentRecipes = recipeDao.searchOutIdCurrentOnlyComponents(component[0]);
//            if (componentPrice.getBuy)
//        }
//
//
//    }


}
