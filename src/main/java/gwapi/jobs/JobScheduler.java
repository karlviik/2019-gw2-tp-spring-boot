package gwapi.jobs;

import gwapi.dao.GameVersionDao;
import gwapi.service.ItemUpdateService;
import gwapi.service.PriceUpdateService;
import gwapi.service.RecipeUpdateService;
import gwapi.web.apiresponse.BuildApiResponse;
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

  @Autowired
  private GameVersionDao versionDao;

  @Autowired
  private ItemUpdateService itemUpdateService;

  @Autowired
  private RecipeUpdateService recipeUpdateService;

  @Autowired
  private PriceUpdateService priceUpdateService;

  @Scheduled(fixedDelay = 1800000)
  public void versionCheckAndTrigger() {
    System.out.println("Started updating items.");
    ResponseEntity<BuildApiResponse> build = restTemplate.exchange(
        "https://api.guildwars2.com/v2/build",
        HttpMethod.GET,
        null,
        BuildApiResponse.class);
    Integer currentVersion = build.getBody().getId();
    if (!versionDao.getVersion().equals(currentVersion)) {
      System.out.println("New version, started updating everything");
      versionDao.setVersion(currentVersion);
      itemUpdateService.updateAllItems();
      recipeUpdateService.updateAllRecipes();
    }
    else {
      System.out.println("Same version, checking for new items");
      itemUpdateService.addNewItems();
      recipeUpdateService.addNewRecipes();
    }
    System.out.println("Finished updating items");
  }

  @Scheduled(cron = "0 0,15,30,45 * * * ?")
  public void priceUpdate() {
    System.out.println("Started price updating.");
    priceUpdateService.updatePrices();
    System.out.println("Finished price updating.");
  }
}

