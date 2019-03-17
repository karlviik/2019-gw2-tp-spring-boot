package gwapi.web;

import gwapi.dao.JdbcDao;
import gwapi.entity.Item;
import gwapi.entity.Recipe;
import gwapi.repository.ItemRepository;
import gwapi.web.apiresponse.RecipesPageApiResponse;
import gwapi.web.response.HelloResponse;
import gwapi.web.apiresponse.ItemsPageApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
public class ApiPullController {

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private ItemRepository itemRepository;

    @Autowired
    private JdbcDao dbd;

    @GetMapping("/hello")
    public Object hello() {
        return new HelloResponse("hello");
    }

    @GetMapping("/hello2")
    public void fetchItems() {
//        ResponseEntity<ItemsPageApiResponse> response = restTemplate.exchange("https://api.guildwars2.com/v2/items?page_size=200&page=150", HttpMethod.GET, null, ItemsPageApiResponse.class);

//        for (Item item : response.getBody()) {
//            System.out.println(item);
//            itemRepository.save(item);
//            dbd.createTable();
//        }

        ResponseEntity<RecipesPageApiResponse> response = restTemplate.exchange("https://api.guildwars2.com/v2/recipes?page_size=200&page=20", HttpMethod.GET, null, RecipesPageApiResponse.class);

        for (Recipe recipe : response.getBody()) {
            System.out.println(recipe);
//            dbd.createTable();
        }

    }

}
