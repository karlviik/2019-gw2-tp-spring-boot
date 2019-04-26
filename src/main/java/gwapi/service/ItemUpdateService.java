package gwapi.service;

import gwapi.dao.ItemDao;
import gwapi.entity.Item;
import gwapi.entity.ItemRarity;
import gwapi.entity.ItemSubSubType;
import gwapi.entity.ItemSubType;
import gwapi.entity.ItemType;
import gwapi.web.apiresponse.IdListApiResponse;
import gwapi.web.apiresponse.ItemPageApiResponse;
import gwapi.web.apiresponse.ItemPageApiResponse.ItemResponse.Detail;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Stream;

import static java.util.function.Predicate.isEqual;
import static java.util.function.Predicate.not;
import static java.util.stream.Collectors.toList;

/**
 * Should have 2 methods:
 * a) get all items and update them (like when build changes)
 * This kinda means that when sub tables have rows removed or edited, then first got to
 * delete current contents related to that item ID and then add the new ones.
 * Could also check if actually got to remove, but that's kinda unnecessary
 * as there are not many listings in those cases.
 * Happens rarely.
 * b) get new items and add them (like when no build change)
 */
@Component
public class ItemUpdateService {

  @Autowired
  private RestTemplate restTemplate;

  @Autowired
  private ItemDao itemDao;

  public void addNewItems() {
    ResponseEntity<IdListApiResponse> allApiItemIdsResponse = restTemplate.exchange(
        "https://api.guildwars2.com/v2/items",
        HttpMethod.GET,
        null,
        IdListApiResponse.class);
    List<Integer> allApiItemIds = allApiItemIdsResponse.getBody();
    List<Integer> allDatabaseItemIds = itemDao.getItemIds();

    HashSet<Integer> uncommonElements = new HashSet<>(allApiItemIds);
    for (Integer b : allDatabaseItemIds) {
      if (!uncommonElements.add(b)) {
        uncommonElements.remove(b);
      }
    }
    // if there are no new items in api list
    if (uncommonElements.size() == 0) {
      return;
    }

    for (int i = 0; i < (int) Math.ceil(allApiItemIds.size() / 200.0); i++) {
      StringBuilder sb = new StringBuilder();
      sb.append("https://api.guildwars2.com/v2/items?ids=");
      int end = Math.min((i + 1) * 200, allApiItemIds.size());
      for (int j = i * 200; j < end; j++) {
        sb.append(allApiItemIds.get(j));
        if (j < end - 1) {
          sb.append(",");
        }
      }
      ResponseEntity<ItemPageApiResponse> itemPageApiResponse = restTemplate.exchange(
          sb.toString(),
          HttpMethod.GET,
          null,
          ItemPageApiResponse.class);
      itemPageApiResponse.getBody().stream()
          .map(this::mapItem)
          .forEach(this::addItemToDatabase);
    }
  }

  public void updateAllItems() {

    ResponseEntity<IdListApiResponse> idCount = restTemplate.exchange(
        "https://api.guildwars2.com/v2/items",
        HttpMethod.GET,
        null,
        IdListApiResponse.class);
    int pageAmount = (int) Math.round(idCount.getBody().size() / 200.0);

    for (int i = 0; i < pageAmount; i++) {
      ResponseEntity<ItemPageApiResponse> result = restTemplate.exchange(
          "https://api.guildwars2.com/v2/items?page_size=200&page=" + i,
          HttpMethod.GET,
          null,
          ItemPageApiResponse.class);

      result.getBody()
          .stream()
          .map(this::mapItem)
          .forEach(this::addItemToDatabase);
    }
  }

  private Item mapItem(ItemPageApiResponse.ItemResponse itemResponse) {
    boolean bound = itemResponse.getFlags().stream().anyMatch(x -> x.equals("AccountBound") || x.equals("SoulbindOnAcquire"));

    boolean nullVendor = itemResponse.getFlags().stream().anyMatch(x -> x.equals("NoSell"));

    String iconLink = null;
    if (itemResponse.getIcon() != null) {
      iconLink = itemResponse.getIcon();
    }

    ItemSubType subType = Optional.ofNullable(itemResponse.getDetail())
        .map(Detail::getType)
        .map(String::toUpperCase)
        .map(ItemSubType::valueOf)
        .orElse(null);

    ItemSubSubType subSubType = Optional.ofNullable(itemResponse.getDetail())
        .map(Detail::getWeightClass)
        .map(String::toUpperCase)
        .map(ItemSubSubType::valueOf)
        .orElse(null);

    List<Integer> infusions = Optional.ofNullable(itemResponse.getDetail())
        .map(Detail::getInfusions)
        .stream()
        .flatMap(Collection::stream)
        .map(Detail.InfusionSlot::getId)
        .collect(toList());

    List<Integer> upgrades = Stream.of(
        Optional.ofNullable(itemResponse.getDetail())
          .map(Detail::getSubItem)
          .stream(),
        Optional.ofNullable(itemResponse.getDetail())
          .map(Detail::getSubItem2)
          .filter(not(isEqual(0)))
          .stream()
    )
        .flatMap(Function.identity())
        .collect(toList());

    return new Item(
        itemResponse.getId(),
        itemResponse.getName(),
        itemResponse.getChatLink(),
        iconLink,
        ItemRarity.valueOf(itemResponse.getRarity().toUpperCase()),
        itemResponse.getLevel(),
        bound,
        nullVendor ? null : itemResponse.getVendorValue(),
        ItemType.valueOf(itemResponse.getType().toUpperCase()),
        subType,
        subSubType,
        upgrades,
        infusions);
  }

  private void addItemToDatabase(Item item) {
    itemDao.createOrOverwriteItem(
        item.getId(),
        item.getName(),
        item.getChatLink(),
        item.getIconLink(),
        item.getRarity().name(),
        item.getLevel(),
        item.getBound(),
        item.getVendorValue(),
        Optional.ofNullable(item.getType()).map(ItemType::name).orElse(null),
        Optional.ofNullable(item.getType2()).map(ItemSubType::name).orElse(null),
        Optional.ofNullable(item.getType3()).map(ItemSubSubType::name).orElse(null));

    Optional.ofNullable(item.getItemUpgrades())
        .stream()
        .flatMap(Collection::stream)
        .filter(x -> x != null)
        .forEach(upgrade -> {
          itemDao.createOrNothingUpgrade(item.getId(), upgrade);
        });

    Optional.ofNullable(item.getItemInfusions())
        .stream()
        .flatMap(Collection::stream)
        .filter(x -> x != null)
        .forEach(infusion -> {
          itemDao.createOrNothingInfusion(item.getId(), infusion);
        });
  }
}
