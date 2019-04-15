package gwapi.entity;

import java.util.Objects;

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

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (!(o instanceof RecipeComponent)) {
      return false;
    }
    RecipeComponent that = (RecipeComponent) o;
    return id == that.id &&
        count == that.count;
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, count);
  }
}
