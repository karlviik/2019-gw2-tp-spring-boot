package gwapi.entity;

import java.time.LocalDateTime;

public class Recipe {

    private Integer recipeId;
    private LocalDateTime overwriteTime;
    private RecipeType type;
    private Integer minRating;
    private boolean learnedFromItem;
    private String chatLink;
    private Integer outId;
    private Integer outCount;
    private Integer inId1;
    private Integer inCount1;
    private Integer inId2;
    private Integer inCount2;
    private Integer inId3;
    private Integer inCount3;
    private Integer inId4;
    private Integer inCount4;


    public Recipe(Integer recipeId,
                  LocalDateTime overwriteTime,
                  RecipeType type,
                  Integer minRating,
                  boolean learnedFromItem,
                  String chatLink,
                  Integer outId,
                  Integer outCount,
                  Integer inId1,
                  Integer inCount1,
                  Integer inId2,
                  Integer inCount2,
                  Integer inId3,
                  Integer inCount3,
                  Integer inId4,
                  Integer inCount4) {
        this.recipeId = recipeId;
        this.overwriteTime = overwriteTime;
        this.type = type;
        this.minRating = minRating;
        this.learnedFromItem = learnedFromItem;
        this.chatLink = chatLink;
        this.outId = outId;
        this.outCount = outCount;
        this.inId1 = inId1;
        this.inCount1 = inCount1;
        this.inId2 = inId2;
        this.inCount2 = inCount2;
        this.inId3 = inId3;
        this.inCount3 = inCount3;
        this.inId4 = inId4;
        this.inCount4 = inCount4;
    }

    public Recipe(Integer outId, Integer outCount, Integer inId1, Integer inCount1, Integer inId2, Integer inCount2, Integer inId3, Integer inCount3, Integer inId4, Integer inCount4) {
        this.outId = outId;
        this.outCount = outCount;
        this.inId1 = inId1;
        this.inCount1 = inCount1;
        this.inId2 = inId2;
        this.inCount2 = inCount2;
        this.inId3 = inId3;
        this.inCount3 = inCount3;
        this.inId4 = inId4;
        this.inCount4 = inCount4;
    }

    @Override
    public String toString() {
        return String.join(" ",
                recipeId.toString(),
                String.valueOf(overwriteTime),
                type.toString(),
                minRating.toString(),
                String.valueOf(learnedFromItem),
                String.valueOf(chatLink),
                String.valueOf(outId),
                String.valueOf(outCount),
                String.valueOf(inId1),
                String.valueOf(inCount1),
                String.valueOf(inId2),
                String.valueOf(inCount2),
                String.valueOf(inId3),
                String.valueOf(inCount3),
                String.valueOf(inId4),
                String.valueOf(inCount4));
    }

    public Integer getRecipeId() {
        return recipeId;
    }

    public LocalDateTime getOverwriteTime() {
        return overwriteTime;
    }

    public RecipeType getType() {
        return type;
    }

    public Integer getMinRating() {
        return minRating;
    }

    public boolean isLearnedFromItem() {
        return learnedFromItem;
    }

    public String getChatLink() {
        return chatLink;
    }

    public Integer getOutId() {
        return outId;
    }

    public Integer getOutCount() {
        return outCount;
    }

    public Integer getInId1() {
        return inId1;
    }

    public Integer getInCount1() {
        return inCount1;
    }

    public Integer getInId2() {
        return inId2;
    }

    public Integer getInCount2() {
        return inCount2;
    }

    public Integer getInId3() {
        return inId3;
    }

    public Integer getInCount3() {
        return inCount3;
    }

    public Integer getInId4() {
        return inId4;
    }

    public Integer getInCount4() {
        return inCount4;
    }
}
