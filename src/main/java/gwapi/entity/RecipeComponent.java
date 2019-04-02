package gwapi.entity;

public class RecipeComponent {
    private final int id;
    private final int count;

    public RecipeComponent(int id, int count) {
        this.id = id;
        this.count = count;
    }

    public int getId() {
        return id;
    }

    public int getCount() {
        return count;
    }
}
