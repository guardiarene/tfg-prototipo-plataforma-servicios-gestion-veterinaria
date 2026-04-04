package tfg.psygcv.service.pet;

import java.time.LocalDate;
import lombok.Builder;
import lombok.Getter;
import tfg.psygcv.entity.pet.Breed;
import tfg.psygcv.entity.pet.Sex;
import tfg.psygcv.entity.pet.Species;

@Builder
@Getter
public class CreatePetCommand {

  private final String name;
  private final Sex sex;
  private final Breed breed;
  private final Species species;
  private final LocalDate birthDate;
  private final Float weight;
  private final Long ownerId;
}
