package tfg.psygcv.model.pet;

import lombok.Getter;

@Getter
public enum Temperament {
  CALM("Calmado"),
  AGITATED("Agitado"),
  ANXIOUS("Ansioso"),
  FRIENDLY("Amigable"),
  AGGRESSIVE("Agresivo"),
  UNKNOWN("No determinado");

  private final String description;

  Temperament(String description) {
    this.description = description;
  }

  @Override
  public String toString() {
    return this.name();
  }
}
