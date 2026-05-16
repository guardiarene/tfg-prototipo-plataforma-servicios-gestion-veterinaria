package tfg.psygcv.medical.visit.dto.response;

import java.time.LocalDate;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class TreatmentResponse {

  private Long id;
  private String product;
  private String route;
  private String frequency;
  private LocalDate startDate;
  private LocalDate endDate;
}
