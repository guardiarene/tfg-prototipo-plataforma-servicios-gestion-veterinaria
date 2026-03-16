package tfg.psygcv.model.pet;

import lombok.Getter;

@Getter
public enum ReproductiveStatus {
  INTACT("Entero/a (sin esterilizar)"),
  NEUTERED("Esterilizado/a"),
  PREGNANT("Gestante"),
  LACTATING("Lactante"),
  UNKNOWN("No determinado");

  private final String description;

  ReproductiveStatus(String description) {
    this.description = description;
  }

  @Override
  public String toString() {
    return this.name();
  }
}
