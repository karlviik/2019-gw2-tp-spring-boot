package gwapi.entity;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

public class Recipe {

    private Integer recipeId;
    private LocalDateTime overwriteTime;
    private RecipeType type;
    private Integer minRating;
    private boolean learnedFromItem;
    private String chatLink;
    private Integer outItemId;
    private Integer outItemCount;
    private List<RecipeDiscipline> disciplines;
    private List<RecipeComponent> components;


    public Recipe(Integer recipeId, LocalDateTime overwriteTime, RecipeType type, Integer minRating, boolean learnedFromItem, String chatLink, Integer outItemId, Integer outItemCount, List<RecipeDiscipline> disciplines, List<RecipeComponent> components) {
        this.recipeId = recipeId;
        this.overwriteTime = overwriteTime;
        this.type = type;
        this.minRating = minRating;
        this.learnedFromItem = learnedFromItem;
        this.chatLink = chatLink;
        this.outItemId = outItemId;
        this.outItemCount = outItemCount;
        this.disciplines = disciplines;
        this.components = components;
    }

    public Recipe(Integer outItemId, Integer outItemCount, List<RecipeComponent> components) {
        this.outItemId = outItemId;
        this.outItemCount = outItemCount;
        this.components = components;
    }

    @Override
    public String toString() {
        return String.join(" ",
                recipeId.toString(),
                String.valueOf(overwriteTime),
                type.toString(),
                minRating.toString(),
                String.valueOf(learnedFromItem),
                String.valueOf(chatLink));
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

    public Integer getOutItemId() {
        return outItemId;
    }

    public Integer getOutItemCount() {
        return outItemCount;
    }

    public List<RecipeDiscipline> getDisciplines() {
        return disciplines;
    }

    public List<RecipeComponent> getComponents() {
        return components;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Recipe)) return false;
        Recipe recipe = (Recipe) o;
        for (RecipeComponent component : components) {
            boolean hasFoundPair = false;
            for (RecipeComponent compareComponent : recipe.components) {
                if (component.getId() == compareComponent.getId() && component.getCount() == compareComponent.getCount()) {
                    hasFoundPair = true;
                    break;
                }
            }
            if (!hasFoundPair) {
                return false;
            }
        }
        return learnedFromItem == recipe.learnedFromItem &&
                recipeId.equals(recipe.recipeId) &&
                type == recipe.type &&
                minRating.equals(recipe.minRating) &&
                chatLink.equals(recipe.chatLink) &&
                outItemId.equals(recipe.outItemId) &&
                outItemCount.equals(recipe.outItemCount);
    }

    @Override
    public int hashCode() {
        return Objects.hash(recipeId, type, minRating, learnedFromItem, chatLink, outItemId, outItemCount, components);
    }
}
