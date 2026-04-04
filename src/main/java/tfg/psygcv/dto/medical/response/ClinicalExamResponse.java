package tfg.psygcv.dto.medical.response;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import tfg.psygcv.entity.pet.Temperament;

@Getter
@Setter
@NoArgsConstructor
public class ClinicalExamResponse {

  private Float temperature;
  private Integer heartRate;
  private Integer respiratoryRate;
  private Float weight;
  private Integer pulse;
  private String mucosalMembranes;
  private Temperament temperament;
  private String description;
}
