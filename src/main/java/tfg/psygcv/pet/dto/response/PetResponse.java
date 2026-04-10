package tfg.psygcv.pet.dto.response;

import java.time.LocalDate;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import tfg.psygcv.pet.entity.Breed;
import tfg.psygcv.pet.entity.Sex;
import tfg.psygcv.pet.entity.Species;

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
