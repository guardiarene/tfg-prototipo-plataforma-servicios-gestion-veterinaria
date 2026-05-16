package tfg.psygcv.clinic.mapper;

import tfg.psygcv.clinic.command.CreateMedicalServiceCommand;
import tfg.psygcv.clinic.command.UpdateMedicalServiceCommand;
import tfg.psygcv.clinic.dto.request.CreateMedicalServiceRequest;
import tfg.psygcv.clinic.dto.request.UpdateMedicalServiceRequest;
import tfg.psygcv.clinic.dto.response.MedicalServiceResponse;
import tfg.psygcv.clinic.entity.MedicalService;

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

  public static CreateMedicalServiceCommand toCreateCommand(
      CreateMedicalServiceRequest request, Long clinicId) {
    return CreateMedicalServiceCommand.builder()
        .name(request.getName())
        .description(request.getDescription())
        .clinicId(clinicId)
        .build();
  }

  public static UpdateMedicalServiceCommand toUpdateCommand(UpdateMedicalServiceRequest request) {
    return UpdateMedicalServiceCommand.builder()
        .name(request.getName())
        .description(request.getDescription())
        .build();
  }

  public static UpdateMedicalServiceRequest toUpdateRequest(MedicalService service) {
    UpdateMedicalServiceRequest request = new UpdateMedicalServiceRequest();
    request.setName(service.getName());
    request.setDescription(service.getDescription());
    return request;
  }
}
