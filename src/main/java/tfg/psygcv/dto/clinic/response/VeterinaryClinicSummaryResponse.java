package tfg.psygcv.dto.clinic.response;

import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class VeterinaryClinicSummaryResponse {

  private Long id;
  private String name;
  private String address;
  private List<MedicalServiceResponse> services;
}
