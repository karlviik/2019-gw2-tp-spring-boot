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
    private Integer inIdOne;
    private Integer inCountOne;
    private Integer inIdTwo;
    private Integer inCountTwo;
    private Integer inIdThree;
    private Integer inCountThree;
    private Integer inIdFour;
    private Integer inCountFour;


    public Recipe(Integer recipeId,
                  LocalDateTime overwriteTime,
                  RecipeType type,
                  Integer minRating,
                  boolean learnedFromItem,
                  String chatLink,
                  Integer outId,
                  Integer outCount,
                  Integer inIdOne,
                  Integer inCountOne,
                  Integer inIdTwo,
                  Integer inCountTwo,
                  Integer inIdThree,
                  Integer inCountThree,
                  Integer inIdFour,
                  Integer inCountFour) {
        this.recipeId = recipeId;
        this.overwriteTime = overwriteTime;
        this.type = type;
        this.minRating = minRating;
        this.learnedFromItem = learnedFromItem;
        this.chatLink = chatLink;
        this.outId = outId;
        this.outCount = outCount;
        this.inIdOne = inIdOne;
        this.inCountOne = inCountOne;
        this.inIdTwo = inIdTwo;
        this.inCountTwo = inCountTwo;
        this.inIdThree = inIdThree;
        this.inCountThree = inCountThree;
        this.inIdFour = inIdFour;
        this.inCountFour = inCountFour;

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
                String.valueOf(inIdOne),
                String.valueOf(inCountOne),
                String.valueOf(inIdTwo),
                String.valueOf(inCountTwo),
                String.valueOf(inIdThree),
                String.valueOf(inCountThree),
                String.valueOf(inIdFour),
                String.valueOf(inCountFour));
    }
}
