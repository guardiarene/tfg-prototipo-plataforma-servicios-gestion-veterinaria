package tfg.psygcv.dto.pet.response;

import java.time.LocalDate;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import tfg.psygcv.entity.pet.Breed;
import tfg.psygcv.entity.pet.Species;

@Getter
@Setter
@NoArgsConstructor
public class PetSummaryResponse {

  private Long id;
  private String name;
  private Species species;
  private Breed breed;
  private LocalDate birthDate;
  private Long medicalRecordId;
  private String ownerFullName;
}
