package tfg.psygcv.mapper.clinic;

import tfg.psygcv.dto.clinic.request.UpdateClinicRequest;
import tfg.psygcv.dto.clinic.response.VeterinaryClinicResponse;
import tfg.psygcv.dto.clinic.response.VeterinaryClinicSummaryResponse;
import tfg.psygcv.entity.clinic.VeterinaryClinic;

public class VeterinaryClinicMapper {

  private VeterinaryClinicMapper() {
    throw new UnsupportedOperationException("Utility class");
  }

  public static VeterinaryClinicResponse toResponse(VeterinaryClinic clinic) {
    VeterinaryClinicResponse response = new VeterinaryClinicResponse();
    response.setId(clinic.getId());
    response.setName(clinic.getName());
    response.setAddress(clinic.getAddress());
    response.setPhone(clinic.getPhone());
    response.setEmail(clinic.getEmail());
    response.setOwnerFullName(
        clinic.getOwner() != null
            ? clinic.getOwner().getFirstName() + " " + clinic.getOwner().getLastName()
            : null);
    response.setServices(
        clinic.getServices().stream().map(MedicalServiceMapper::toResponse).toList());
    return response;
  }

  public static VeterinaryClinicSummaryResponse toSummary(VeterinaryClinic clinic) {
    VeterinaryClinicSummaryResponse summary = new VeterinaryClinicSummaryResponse();
    summary.setId(clinic.getId());
    summary.setName(clinic.getName());
    summary.setAddress(clinic.getAddress());
    summary.setServices(
        clinic.getServices().stream().map(MedicalServiceMapper::toResponse).toList());
    return summary;
  }

  public static UpdateClinicRequest toUpdateRequest(VeterinaryClinic clinic) {
    UpdateClinicRequest request = new UpdateClinicRequest();
    request.setName(clinic.getName());
    request.setAddress(clinic.getAddress());
    request.setPhone(clinic.getPhone());
    request.setEmail(clinic.getEmail());
    return request;
  }
}
