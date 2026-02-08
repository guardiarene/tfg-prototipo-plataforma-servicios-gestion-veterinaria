package tfg.psygcv.model.pet;

import lombok.Getter;

@Getter
public enum Sex {
  MALE("Male"),
  FEMALE("Female");

  private final String description;

  Sex(String description) {
    this.description = description;
  }

  @Override
  public String toString() {
    return this.name();
  }
}
