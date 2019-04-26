package gwapi.web;

import gwapi.dao.ItemDao;
import gwapi.entity.Item;
import gwapi.service.ItemUpdateService;
import gwapi.service.PriceUpdateService;
import gwapi.service.RecipeUpdateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.ModelAndView;

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
    model.addAttribute("name", item.getName());
    model.addAttribute("id", id);
    model.addAttribute("level", item.getLevel().toString());
    model.addAttribute("rarity", item.getRarity().name());
    model.addAttribute("type", item.getType().name());
    model.addAttribute("chat_link", item.getChatLink());
    model.addAttribute("bound", item.getBound().toString());
    model.addAttribute("vendor", item.getVendorValue().toString());
    model.addAttribute("icon", item.getIconLink());

    return "item";
  }

  @GetMapping("/itemm")
  public void fetchItems() {
//    recipeUpdateService.addNewRecipes();
//    priceUpdateService.updatePrices();
  }

}
