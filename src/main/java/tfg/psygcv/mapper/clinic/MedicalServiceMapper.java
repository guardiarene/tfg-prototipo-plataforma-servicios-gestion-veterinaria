package tfg.psygcv.mapper.clinic;

import tfg.psygcv.dto.clinic.request.UpdateMedicalServiceRequest;
import tfg.psygcv.dto.clinic.response.MedicalServiceResponse;
import tfg.psygcv.entity.clinic.MedicalService;

public class MedicalServiceMapper {

  private MedicalServiceMapper() {
    throw new UnsupportedOperationException("Utility class");
  }

  public static MedicalServiceResponse toResponse(MedicalService service) {
    MedicalServiceResponse response = new MedicalServiceResponse();
    response.setId(service.getId());
    response.setName(service.getName());
    response.setDescription(service.getDescription());
    return response;
  }

  public static UpdateMedicalServiceRequest toUpdateRequest(MedicalService service) {
    UpdateMedicalServiceRequest request = new UpdateMedicalServiceRequest();
    request.setName(service.getName());
    request.setDescription(service.getDescription());
    return request;
  }
}
