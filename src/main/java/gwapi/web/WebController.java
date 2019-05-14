package gwapi.web;

import gwapi.dao.ItemDao;
import gwapi.dao.PriceDao;
import gwapi.entity.Item;
import gwapi.entity.Price;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Controller for responding to web requests
 */
@Controller
public class WebController {

  @Autowired
  private ItemDao itemDao;

  @Autowired
  private PriceDao priceDao;

  /**
   * Item page
   *
   * @param model page template
   * @param idString id of item
   * @return item page with data
   */
  @RequestMapping("/item/{idString}")
  public String item(Model model, @PathVariable String idString) {
    System.out.println("Requested item: " + idString);
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

    List<Price> prices = priceDao.getPrices(id);

    ArrayList<ArrayList> allPrices = new ArrayList<>();
    for (Price price : prices) {
      LocalDateTime time = price.getTime();

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
    model.addAttribute("inData", allPrices);
    return "item";
  }

  /**
   * Page for searching items.
   *
   * @param model page template
   * @param searchString search string
   * @return search page with up to 100 results
   */
  @RequestMapping("/item")
  public String itemSearch(Model model, @RequestParam(value = "search", defaultValue = "potato") String searchString) {
    System.out.println("Searched string: " + searchString);
    List<Item> searchResults = itemDao.getItemContainingString(searchString);
    List<List> inList = new ArrayList<>();
    for (Item item : searchResults) {
      inList.add(new ArrayList<Object>(Arrays.asList(
          item.getId(),
          item.getName(),
          item.getRarity().toString(),
          item.getLevel(),
          item.getChatLink()
      )));
    }
    inList = inList.stream().sorted(Comparator.comparing(x -> (String) x.get(1))).collect(Collectors.toList());
    model.addAttribute("items", inList);
    return "itemSearch";
  }

  /**
   * @return index page on root
   */
  @RequestMapping("/")
  public String landing() {
    return "index";
  }

  /**
   * @return 404 on unknown url
   */
  @RequestMapping("*")
  public String fallbackMethod(){
    return "404";
  }
}
