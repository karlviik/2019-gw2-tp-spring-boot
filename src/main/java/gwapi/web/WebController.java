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

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;


@Controller
public class WebController {

  @Autowired
  private ItemDao itemDao;

  @Autowired
  private PriceDao priceDao;

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
}
