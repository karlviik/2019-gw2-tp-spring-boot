package gwapi.service;

import gwapi.dao.ItemDao;
import gwapi.web.response.HelloResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class PriceUpdateService {

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private ItemDao itemDao;

    public void updateItems() {
        ResponseEntity<HelloResponse> result = restTemplate.exchange("http://localhost:8080/hello", HttpMethod.GET, null, HelloResponse.class);
        System.out.println(result.getBody().getMessage());
        itemDao.create(1, result.getBody().getMessage());
    }
}
