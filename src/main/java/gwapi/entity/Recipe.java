package gwapi.entity;


import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.sql.Time;

@Entity
@Table(name = "recipes")
@JsonDeserialize(using = RecipeDeserializer.class)
public class Recipe {

    @Id
    private Integer recipeId;
    private Time overwriteTime;
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
    private boolean boolNew;
    private boolean boolArtificer;
    private boolean boolArmorsmith;
    private boolean boolChef;
    private boolean boolHuntsman;
    private boolean boolJeweler;
    private boolean boolLeatherworker;
    private boolean boolTailor;
    private boolean boolWeaponsmith;
    private boolean boolScribe;

    protected Recipe() {

    }

    public Recipe(Integer recipeId,
                  Time overwriteTime,
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
                  Integer inCountFour,
                  boolean boolNew,
                  boolean boolArtificer,
                  boolean boolArmorsmith,
                  boolean boolChef,
                  boolean boolHuntsman,
                  boolean boolJeweler,
                  boolean boolLeatherworker,
                  boolean boolTailor,
                  boolean boolWeaponsmith,
                  boolean boolScribe) {
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
        this.boolNew = boolNew;
        this.boolArtificer = boolArtificer;
        this.boolArmorsmith = boolArmorsmith;
        this.boolChef = boolChef;
        this.boolHuntsman = boolHuntsman;
        this.boolJeweler = boolJeweler;
        this.boolLeatherworker = boolLeatherworker;
        this.boolTailor = boolTailor;
        this.boolWeaponsmith = boolWeaponsmith;
        this.boolScribe = boolScribe;
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
