package gwapi.web;

import gwapi.dao.ItemDao;
import gwapi.dao.JdbcDao;
import gwapi.entity.Item;
import gwapi.repository.ItemRepository;
import gwapi.web.response.HelloResponse;
import gwapi.web.apiresponse.ItemsApiResponse;
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

    @GetMapping("/hello")
    public Object hello() {
        return new HelloResponse("hello");
    }

    @GetMapping("/hello2")
    public void fetchItems() {
        ResponseEntity<ItemsApiResponse> response = restTemplate.exchange("https://api.guildwars2.com/v2/items?page_size=200&page=150", HttpMethod.GET, null, ItemsApiResponse.class);

        for (Item item : response.getBody()) {
            System.out.println(item);
            itemRepository.save(item);
        }
    }

}
