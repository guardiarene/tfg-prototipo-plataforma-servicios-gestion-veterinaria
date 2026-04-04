package tfg.psygcv.dto.medical.response;

import java.time.LocalDate;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class VaccineResponse {

  private Long id;
  private LocalDate applicationDate;
  private String brand;
  private String dose;
  private String batch;
}
