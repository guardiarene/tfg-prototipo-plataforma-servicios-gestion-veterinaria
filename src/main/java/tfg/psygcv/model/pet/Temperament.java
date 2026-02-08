package tfg.psygcv.model.pet;

import lombok.Getter;

@Getter
public enum Temperament {
  CALM("Calm and relaxed"),
  AGITATED("Restless or irritable"),
  ANXIOUS("Nervous or uneasy"),
  FRIENDLY("Sociable and affectionate"),
  AGGRESSIVE("Hostile or confrontational"),
  UNKNOWN("Temperament not determined");

  private final String description;

  Temperament(String description) {
    this.description = description;
  }

  @Override
  public String toString() {
    return this.name();
  }
}
