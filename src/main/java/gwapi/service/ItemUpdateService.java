package gwapi.service;

import gwapi.dao.ItemDao;
import gwapi.entity.*;
import gwapi.web.apiresponse.IdListApiResponse;
import gwapi.web.apiresponse.ItemPageApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import java.lang.Math;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

/**
 * Should have 2 methods:
 * a) get all items and update them (like when build changes)
 *      This kinda means that when sub tables have rows removed or edited, then first got to
 *      delete current contents related to that item ID and then add the new ones.
 *      Could also check if actually got to remove, but that's kinda unnecessary
 *      as there are not many listings in those cases.
 *      Happens rarely.
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
        LinkedList<Integer> allApiItemIds = new LinkedList<>(allApiItemIdsResponse.getBody());
        LinkedList<Integer> allDatabaseItemIds = new LinkedList<>(itemDao.getAllItemIds());
        for (Integer dbId : allDatabaseItemIds) {
            allApiItemIds.remove(dbId);
        }
        if (allApiItemIds.size() == 0) {
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
                    .forEach(item -> itemDao.createOrOverwrite(item));
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
                    .forEach(item -> itemDao.createOrOverwrite(item));
        }
    }

    private Item mapItem(ItemPageApiResponse.ItemResponse itemResponse) {
        boolean bound = Arrays.stream(itemResponse.getFlags()).anyMatch(x -> x.equals("AccountBound") || x.equals("SoulbindOnAcquire"));

        boolean nullVendor = Arrays.stream(itemResponse.getFlags()).anyMatch(x -> x.equals("NoSell"));
        Integer vendor = nullVendor ? null : itemResponse.getVendorValue();

        String[] iconSplit = itemResponse.getIcon().split("/");
        Integer iconId = Integer.parseInt(iconSplit[iconSplit.length - 1].split("\\.")[0]);

        ItemSubType subType;
        ItemSubSubType subSubType;
        List<Integer> upgrades;
        List<Integer> infusions;
        if (itemResponse.getDetail() != null) {
            ItemPageApiResponse.ItemResponse.Detail detail = itemResponse.getDetail();
            if (detail.getType() != null) {
                subType = ItemSubType.valueOf(detail.getType().toUpperCase());
                if (detail.getWeightClass() != null) {
                    subSubType = ItemSubSubType.valueOf(detail.getWeightClass().toUpperCase());
                } else {
                    subSubType = null;
                }
            } else {
                subType = null;
                subSubType = null;
            }
            if (detail.getInfusions() != null && detail.getInfusions().size() > 0) {
                infusions = new LinkedList<>();
                for (ItemPageApiResponse.ItemResponse.Detail.InfusionSlot infusionSlot : detail.getInfusions()) {
                    infusions.add(infusionSlot.getId());
                }
            } else {
                infusions = null;
            }
            if (detail.getSubItem() != null) {
                upgrades = new LinkedList<>();
                upgrades.add(detail.getSubItem());
                if (detail.getSubItem2() != null && detail.getSubItem2() != 0) {
                    upgrades.add(detail.getSubItem2());
                }
            } else {
                upgrades = null;
            }
        } else {
            subType = null;
            subSubType = null;
            upgrades = null;
            infusions = null;
        }
        return new Item(
                itemResponse.getId(),
                itemResponse.getName(),
                itemResponse.getChatLink(),
                iconId,
                ItemRarity.valueOf(itemResponse.getRarity().toUpperCase()),
                itemResponse.getLevel(),
                bound,
                vendor,
                ItemType.valueOf(itemResponse.getType().toUpperCase()),
                subType,
                subSubType,
                upgrades,
                infusions);
    }
}
