package gwapi.jobs;

import gwapi.web.response.HelloResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class JobScheduler {

    @Autowired
    private RestTemplate restTemplate;

    @Scheduled(fixedDelay = 2000)
    public void updatePrices() {
        System.out.println("updating");
    }
}

