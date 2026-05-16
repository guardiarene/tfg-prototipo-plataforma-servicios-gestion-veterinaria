package tfg.psygcv.medical.record.dto.response;

import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import tfg.psygcv.medical.visit.dto.response.AnamnesisResponse;
import tfg.psygcv.medical.visit.dto.response.VaccineResponse;
import tfg.psygcv.medical.visit.dto.response.VisitResponse;

@Getter
@Setter
@NoArgsConstructor
public class MedicalRecordResponse {

  private Long id;
  private String petName;
  private String petSpeciesDescription;
  private String petBreedDescription;
  private String ownerFullName;
  private String generalObservations;
  private AnamnesisResponse currentAnamnesis;
  private List<VaccineResponse> vaccines;
  private List<VisitResponse> visits;
}
