package gwapi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.client.RestTemplate;

/**
 * Main class where application starts.
 */
@EnableScheduling
@SpringBootApplication
public class ApiApplication {


  public static void main(String[] args) {
    SpringApplication.run(ApiApplication.class, args);
  }


  @Bean
  public RestTemplate restTemplate() {
    return new RestTemplate();
  }
}
