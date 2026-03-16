package tfg.psygcv.model.pet;

import lombok.Getter;

@Getter
public enum Breed {
  LABRADOR_RETRIEVER(Species.DOG, "Labrador Retriever"),
  GERMAN_SHEPHERD(Species.DOG, "Pastor Alemán"),
  GOLDEN_RETRIEVER(Species.DOG, "Golden Retriever"),
  FRENCH_BULLDOG(Species.DOG, "Bulldog Francés"),
  ENGLISH_BULLDOG(Species.DOG, "Bulldog Inglés"),
  POODLE(Species.DOG, "Caniche"),
  BEAGLE(Species.DOG, "Beagle"),
  YORKSHIRE_TERRIER(Species.DOG, "Yorkshire Terrier"),
  CHIHUAHUA(Species.DOG, "Chihuahua"),
  ROTTWEILER(Species.DOG, "Rottweiler"),
  BORDER_COLLIE(Species.DOG, "Border Collie"),
  SIBERIAN_HUSKY(Species.DOG, "Husky Siberiano"),
  MALTESE(Species.DOG, "Maltés"),
  DACHSHUND(Species.DOG, "Teckel"),
  SHIH_TZU(Species.DOG, "Shih Tzu"),
  MIXED_DOG(Species.DOG, "Sin raza definida"),

  PERSIAN(Species.CAT, "Persa"),
  SIAMESE(Species.CAT, "Siamés"),
  MAINE_COON(Species.CAT, "Maine Coon"),
  BRITISH_SHORTHAIR(Species.CAT, "British Shorthair"),
  BENGAL(Species.CAT, "Bengalí"),
  RAGDOLL(Species.CAT, "Ragdoll"),
  SCOTTISH_FOLD(Species.CAT, "Scottish Fold"),
  RUSSIAN_BLUE(Species.CAT, "Azul Ruso"),
  ABYSSINIAN(Species.CAT, "Abisinio"),
  MIXED_CAT(Species.CAT, "Sin raza definida"),

  DUTCH(Species.RABBIT, "Holandés"),
  MINI_LOP(Species.RABBIT, "Mini Lop"),
  HOLLAND_LOP(Species.RABBIT, "Holland Lop"),
  ANGORA_RABBIT(Species.RABBIT, "Angora"),
  REX_RABBIT(Species.RABBIT, "Rex"),
  LIONHEAD(Species.RABBIT, "Cabeza de León"),
  MIXED_RABBIT(Species.RABBIT, "Sin raza definida"),

  CANARY(Species.BIRD, "Canario"),
  BUDGERIGAR(Species.BIRD, "Periquito"),
  COCKATIEL(Species.BIRD, "Ninfa"),
  PARROT(Species.BIRD, "Loro"),
  LOVEBIRD(Species.BIRD, "Agapornis"),
  COCKATOO(Species.BIRD, "Cacatúa"),
  MIXED_BIRD(Species.BIRD, "Sin raza definida"),

  SYRIAN_HAMSTER(Species.HAMSTER, "Hámster Sirio"),
  DWARF_HAMSTER(Species.HAMSTER, "Hámster Enano Ruso"),
  CHINESE_HAMSTER(Species.HAMSTER, "Hámster Chino"),
  ROBOROVSKI(Species.HAMSTER, "Hámster Roborovski"),
  MIXED_HAMSTER(Species.HAMSTER, "Sin raza definida"),

  AMERICAN_GUINEA_PIG(Species.GUINEA_PIG, "Cobaya Americana"),
  PERUVIAN_GUINEA_PIG(Species.GUINEA_PIG, "Cobaya Peruana"),
  ABYSSINIAN_GUINEA_PIG(Species.GUINEA_PIG, "Cobaya Abisinia"),
  TEDDY_GUINEA_PIG(Species.GUINEA_PIG, "Cobaya Teddy"),
  MIXED_GUINEA_PIG(Species.GUINEA_PIG, "Sin raza definida"),

  BEARDED_DRAGON(Species.REPTILE, "Dragón Barbudo"),
  LEOPARD_GECKO(Species.REPTILE, "Gecko Leopardo"),
  CORN_SNAKE(Species.REPTILE, "Serpiente del Maíz"),
  RED_EARED_SLIDER(Species.REPTILE, "Tortuga de Orejas Rojas"),
  BALL_PYTHON(Species.REPTILE, "Pitón Bola"),
  MIXED_REPTILE(Species.REPTILE, "Sin raza definida"),

  GOLDFISH(Species.FISH, "Pez Dorado"),
  BETTA(Species.FISH, "Pez Betta"),
  GUPPY(Species.FISH, "Guppy"),
  NEON_TETRA(Species.FISH, "Tetra Neón"),
  ANGELFISH(Species.FISH, "Pez Ángel"),
  MIXED_FISH(Species.FISH, "Sin raza definida"),

  OTHER_BREED(Species.OTHER, "Sin raza definida");

  private final Species species;
  private final String description;

  Breed(Species species, String description) {
    this.species = species;
    this.description = description;
  }

  @Override
  public String toString() {
    return this.name();
  }
}
