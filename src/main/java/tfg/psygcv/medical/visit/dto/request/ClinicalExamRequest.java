package tfg.psygcv.medical.visit.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import tfg.psygcv.pet.entity.Temperament;

@Getter
@Setter
@NoArgsConstructor
public class ClinicalExamRequest {

  @NotNull(message = "La temperatura es obligatoria")
  @PositiveOrZero
  private Float temperature;

  @NotNull(message = "La frecuencia cardíaca es obligatoria")
  @PositiveOrZero
  private Integer heartRate;

  @NotNull(message = "La frecuencia respiratoria es obligatoria")
  @PositiveOrZero
  private Integer respiratoryRate;

  @NotNull(message = "El peso es obligatorio")
  @PositiveOrZero
  private Float weight;

  @NotNull(message = "El pulso es obligatorio")
  @PositiveOrZero
  private Integer pulse;

  @NotBlank(message = "Las mucosas son obligatorias")
  private String mucosalMembranes;

  @NotNull(message = "El temperamento es obligatorio")
  private Temperament temperament;

  private String description;
}
