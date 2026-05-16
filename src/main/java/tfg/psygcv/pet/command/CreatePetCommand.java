package tfg.psygcv.pet.command;

import java.time.LocalDate;
import lombok.Builder;
import lombok.Getter;
import tfg.psygcv.pet.entity.Breed;
import tfg.psygcv.pet.entity.Sex;
import tfg.psygcv.pet.entity.Species;

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
