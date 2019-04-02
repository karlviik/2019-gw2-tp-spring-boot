package gwapi.jobs;

import gwapi.dao.GameVersionDao;
import gwapi.service.ItemUpdateService;
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

    public void versionCheckAndTrigger() {
        ResponseEntity<BuildApiResponse> build = restTemplate.exchange(
                "https://api.guildwars2.com/v2/build",
                HttpMethod.GET,
                null,
                BuildApiResponse.class);
        Integer currentVersion = build.getBody().getId();
        if (versionDao.getVersion() != currentVersion) {
            versionDao.updateVersion(currentVersion);
            itemUpdateService.updateAllItems();
            recipeUpdateService.updateAllRecipes();
            // this means new build has happened, need to trigger
            // a database wide check for recipe and item changes
            // that also includes adding new items
        } else {
            // here just checking for new items should happen
            itemUpdateService.addNewItems();
            recipeUpdateService.addNewRecipes();
        }
    }

    @Scheduled(fixedDelay = 60000)
    public void versionCheck() {
        System.out.println("updating");
    }
}

