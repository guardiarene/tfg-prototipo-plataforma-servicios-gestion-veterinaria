package tfg.psygcv.dto.pet.response;

import java.time.LocalDate;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import tfg.psygcv.entity.pet.Breed;
import tfg.psygcv.entity.pet.Sex;
import tfg.psygcv.entity.pet.Species;

@Getter
@Setter
@NoArgsConstructor
public class PetResponse {

  private Long id;
  private String name;
  private Sex sex;
  private Breed breed;
  private Species species;
  private LocalDate birthDate;
  private Float weight;
  private Long medicalRecordId;
}
