package tfg.psygcv.model.pet;

import lombok.Getter;

@Getter
public enum Species {
  DOG("Perro"),
  CAT("Gato"),
  RABBIT("Conejo"),
  BIRD("Ave"),
  HAMSTER("Hámster"),
  GUINEA_PIG("Cobaya"),
  REPTILE("Reptil"),
  FISH("Pez"),
  OTHER("Otro");

  private final String description;

  Species(String description) {
    this.description = description;
  }

  @Override
  public String toString() {
    return this.name();
  }
}
