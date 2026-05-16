package tfg.psygcv.clinic.dto.response;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class MedicalServiceResponse {

  private Long id;
  private String name;
  private String description;
}
