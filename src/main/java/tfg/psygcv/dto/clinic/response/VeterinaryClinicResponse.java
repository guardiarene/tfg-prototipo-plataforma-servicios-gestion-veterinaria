package tfg.psygcv.dto.clinic.response;

import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class VeterinaryClinicResponse {

  private Long id;
  private String name;
  private String address;
  private String phone;
  private String email;
  private String ownerFullName;
  private List<MedicalServiceResponse> services;
}
