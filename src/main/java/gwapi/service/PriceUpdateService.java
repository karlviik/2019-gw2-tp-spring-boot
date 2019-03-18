package gwapi.service;

import gwapi.dao.ItemsDao;
import gwapi.entity.Price;
import gwapi.web.apiresponse.PricesPageApiResponse;
import gwapi.web.response.HelloResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;

import static java.time.ZoneOffset.UTC;

@Component
public class PriceUpdateService {

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private ItemsDao itemDao;

    public void updateItems() {
        ResponseEntity<HelloResponse> result = restTemplate.exchange("http://localhost:8080/hello", HttpMethod.GET, null, HelloResponse.class);
        System.out.println(result.getBody().getMessage());
        itemDao.create(1, result.getBody().getMessage());
    }

    public void getPricePoints() {
        LocalDateTime time = LocalDateTime.now(UTC);

        ResponseEntity<PricesPageApiResponse> result = restTemplate.exchange(
                "https://api.guildwars2.com/v2/commerce/prices?page=125&page_size=200",
                HttpMethod.GET,
                null,
                PricesPageApiResponse.class);

        System.out.println(result.getBody());

        result.getBody()
                .stream()
                .map(priceResponse -> map(priceResponse, time))
                .forEach(System.out::println);
    }

    private Price map(PricesPageApiResponse.PriceResponse priceResponse, LocalDateTime time) {
        return new Price(
                priceResponse.getId(),
                time,
                priceResponse.getBuys().getUnitPrice(),
                priceResponse.getBuys().getQuantity(),
                priceResponse.getSells().getUnitPrice(),
                priceResponse.getSells().getQuantity());
    }
}
