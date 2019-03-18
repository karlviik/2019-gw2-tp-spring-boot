package gwapi.web;

import gwapi.entity.*;

import gwapi.service.PriceUpdateService;
import gwapi.web.response.HelloResponse;
import gwapi.web.apiresponse.ItemsPageApiResponse;
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

    @GetMapping("/hello")
    public Object hello() {
        return new HelloResponse("hello");
    }

    @GetMapping("/hello2")
    public void fetchItems() {
        priceUpdateService.getPricePoints();
//        ResponseEntity<ItemsPageApiResponse> response = restTemplate.exchange("https://api.guildwars2.com/v2/items?page_size=200&page=150", HttpMethod.GET, null, ItemsPageApiResponse.class);
//
//        for (ItemsPageApiResponse.ItemResponse item : response.getBody()) {
//            System.out.println(map(item));
//        }
//
//        ResponseEntity<RecipesPageApiResponse> response2 = restTemplate.exchange("https://api.guildwars2.com/v2/recipes?page_size=200&page=20", HttpMethod.GET, null, RecipesPageApiResponse.class);
//
//        for (RecipesPageApiResponse.RecipeResponse ef : response2.getBody()) {
//            System.out.println(ef);
////            dbd.createTable();
//        }

    }
    private Item map(ItemsPageApiResponse.ItemResponse response) {
        String[] regexSplit = response.getIcon().split("/");
        Integer iconId = Integer.parseInt(regexSplit[regexSplit.length - 1].split("\\.")[0]);

        boolean isBound = false;
        for (String flag : response.getFlags()) {
            if (flag.equals("AccountBound") || flag.equals("SoulBindOnAcquire")) {
                isBound = true;
                break;
            }
        }
        return new Item(
                response.getId(),
                response.getName(),
                response.getChatLink(),
                iconId,
                ItemRarity.valueOf(response.getRarity().toUpperCase()),
                response.getLevel(),
                isBound,
                response.getVendorValue(),
                ItemType.valueOf(response.getType().toUpperCase()),
                response.getDetail() != null && response.getDetail().getType() != null ? ItemSubType.valueOf(response.getDetail().getType().toUpperCase()) : null,
                response.getDetail() != null && response.getDetail().getWeightClass() != null ? ItemSubSubType.valueOf(response.getDetail().getWeightClass().toUpperCase()) : null,
                response.getDetail() != null && response.getDetail().getSubItem() != null ? response.getDetail().getSubItem() : null,
                response.getDetail() != null && response.getDetail().getSubItem2() != null ? response.getDetail().getSubItem2() : null,
                response.getDetail() != null && response.getDetail().getInfusions() != null && response.getDetail().getInfusions().size() > 0 && response.getDetail().getInfusions().get(0).getId() != null ? response.getDetail().getInfusions().get(0).getId() : null,
                response.getDetail() != null && response.getDetail().getInfusions() != null && response.getDetail().getInfusions().size() > 1 && response.getDetail().getInfusions().get(1).getId() != null ? response.getDetail().getInfusions().get(1).getId() : null,
                response.getDetail() != null && response.getDetail().getInfusions() != null && response.getDetail().getInfusions().size() > 2 && response.getDetail().getInfusions().get(2).getId() != null ? response.getDetail().getInfusions().get(2).getId() : null);
    }

}
