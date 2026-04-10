package tfg.psygcv.medical.visit.dto.request;

import java.time.LocalDate;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;
import tfg.psygcv.pet.entity.ReproductiveStatus;

@Getter
@Setter
@NoArgsConstructor
public class AnamnesisRequest {

  private String allergies;
  private String previousDiseases;
  private String surgeries;
  private String currentMedications;
  private String diet;
  private ReproductiveStatus reproductiveStatus;

  @DateTimeFormat(iso = ISO.DATE)
  private LocalDate lastDewormingDate;

  @DateTimeFormat(iso = ISO.DATE)
  private LocalDate lastHeatDate;

  @DateTimeFormat(iso = ISO.DATE)
  private LocalDate lastBirthDate;
}
