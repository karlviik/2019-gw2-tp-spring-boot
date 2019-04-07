package gwapi.service;

import gwapi.dao.PriceDao;
import gwapi.dao.RecipeDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


@Component
public class PriceCalculatorService {

  @Autowired
  PriceDao priceDao;

  @Autowired
  RecipeDao recipeDao;


}
