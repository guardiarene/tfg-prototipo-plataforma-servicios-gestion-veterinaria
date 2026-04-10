package tfg.psygcv.medical.visit.dto.response;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import tfg.psygcv.pet.entity.Temperament;

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
