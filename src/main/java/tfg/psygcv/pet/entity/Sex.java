package tfg.psygcv.pet.entity;

import lombok.Getter;

@Getter
public enum Sex {
  MALE("Macho"),
  FEMALE("Hembra");

  private final String description;

  Sex(String description) {
    this.description = description;
  }

  @Override
  public String toString() {
    return this.name();
  }
}
