package tfg.psygcv.dto.clinic.response;

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
