package gwapi.web;

import gwapi.dao.ItemDao;
import gwapi.dao.PriceDao;
import gwapi.entity.Item;
import gwapi.entity.Price;
import gwapi.service.ItemUpdateService;
import gwapi.service.PriceUpdateService;
import gwapi.service.RecipeUpdateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;


@Controller
public class ApiPullController {

  @Autowired
  private RestTemplate restTemplate;

  @Autowired
  private PriceUpdateService priceUpdateService;

  @Autowired
  private RecipeUpdateService recipeUpdateService;

  @Autowired
  private ItemUpdateService itemUpdateService;

  @Autowired
  private ItemDao itemDao;

  @Autowired
  private PriceDao priceDao;

  @GetMapping("/item")
  public String item(Model model, @RequestParam(value="id", required=false, defaultValue="84") String idString) {
    Integer id;
    try {
      id = Integer.parseInt(idString);
    } catch (NumberFormatException e) {
      return "404";
    }
    Optional<Item> optionalItem = itemDao.getItemNoComponents(id);
    if (optionalItem.isEmpty()) {
      return "404";
    }
    Item item = optionalItem.get();
    // add variables into html
    model.addAttribute("name", item.getName());
    model.addAttribute("id", id);
    model.addAttribute("level", item.getLevel().toString());
    model.addAttribute("rarity", item.getRarity().name());
    model.addAttribute("type", item.getType().name());
    model.addAttribute("chat_link", item.getChatLink());
    model.addAttribute("bound", item.getBound().toString());
    model.addAttribute("vendor", item.getVendorValue().toString());
    model.addAttribute("icon", item.getIconLink());
    // get all price points for id
    List<Price> prices = priceDao.getPrices(id);

    System.out.println(prices.size());

    ArrayList<ArrayList> allPrices = new ArrayList<>();
    for (Price price : prices) {
//      long time = Timestamp.valueOf(price.getTime()).getTime();
      LocalDateTime time = price.getTime();
//      Timestamp time = Timestamp.valueOf(price.getTime());

      allPrices.add(new ArrayList<Object>(Arrays.asList(
          time,
          price.getBuyPrice(),
          price.getBuyQuantity(),
          price.getSellPrice(),
          price.getSellQuantity(),
          price.getCraftBuyPrice(),
          price.getCraftSellPrice()
      )));
    }
    System.out.println(allPrices);
    model.addAttribute("inData", allPrices);
    return "item";
  }

//  @RequestMapping("/update")
//  public void update() {
//    itemUpdateService.updateAllItems();
//    System.out.println("updated all items");
//    recipeUpdateService.updateAllRecipes();
//    System.out.println("updated all recipes");
//  }

}
