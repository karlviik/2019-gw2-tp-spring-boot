package gwapi.service;

import gwapi.entity.Price;
import gwapi.web.apiresponse.AllIdsApiResponse;
import gwapi.web.apiresponse.PricesPageApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import java.lang.Math;

import java.time.LocalDateTime;

import static java.time.ZoneOffset.UTC;

@Component
public class PriceUpdateService {

    @Autowired
    private RestTemplate restTemplate;

    public void getPricePoints() {
        LocalDateTime time = LocalDateTime.now(UTC);

        ResponseEntity<AllIdsApiResponse> idCount = restTemplate.exchange(
                "https://api.guildwars2.com/v2/commerce/prices",
                HttpMethod.GET,
                null,
                AllIdsApiResponse.class);
        int pageAmount = (int) Math.round(idCount.getBody().size() / 200.0);

        for (int i = 0; i < pageAmount; i++) {
            ResponseEntity<PricesPageApiResponse> result = restTemplate.exchange(
                    "https://api.guildwars2.com/v2/commerce/prices?page_size=200&page=" + i,
                    HttpMethod.GET,
                    null,
                    PricesPageApiResponse.class);

            result.getBody()
                    .stream()
                    .map(priceResponse -> map(priceResponse, time))
                    .forEach(System.out::println);
        }


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
