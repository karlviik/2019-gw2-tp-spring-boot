package gwapi.web;

import gwapi.service.ItemUpdateService;
import gwapi.service.PriceUpdateService;
import gwapi.service.RecipeUpdateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
public class ApiPullController {

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private PriceUpdateService priceUpdateService;

    @Autowired
    private RecipeUpdateService recipeUpdateService;

    @Autowired
    private ItemUpdateService itemUpdateService;

    @GetMapping("/hello2")
    public void fetchItems() {
        priceUpdateService.updatePrices();
    }

}
