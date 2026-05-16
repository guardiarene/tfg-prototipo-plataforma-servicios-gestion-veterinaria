package tfg.psygcv.medical.visit.dto.response;

import java.time.LocalDate;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import tfg.psygcv.pet.entity.ReproductiveStatus;

@Getter
@Setter
@NoArgsConstructor
public class AnamnesisResponse {

  private String allergies;
  private String previousDiseases;
  private String surgeries;
  private String currentMedications;
  private String diet;
  private ReproductiveStatus reproductiveStatus;
  private LocalDate lastDewormingDate;
  private LocalDate lastHeatDate;
  private LocalDate lastBirthDate;
}
