package tfg.psygcv.clinic.mapper;

import tfg.psygcv.clinic.command.RegisterClinicWithVeterinarianCommand;
import tfg.psygcv.clinic.command.UpdateClinicCommand;
import tfg.psygcv.clinic.dto.request.RegisterClinicRequest;
import tfg.psygcv.clinic.dto.request.UpdateClinicRequest;
import tfg.psygcv.clinic.dto.response.VeterinaryClinicResponse;
import tfg.psygcv.clinic.dto.response.VeterinaryClinicSummaryResponse;
import tfg.psygcv.clinic.entity.VeterinaryClinic;

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

  public static RegisterClinicWithVeterinarianCommand toRegisterCommand(
      RegisterClinicRequest request) {
    return RegisterClinicWithVeterinarianCommand.builder()
        .userFirstName(request.getUserFirstName())
        .userLastName(request.getUserLastName())
        .userEmail(request.getUserEmail())
        .userPassword(request.getUserPassword())
        .userPhone(request.getUserPhone())
        .clinicName(request.getClinicName())
        .clinicAddress(request.getClinicAddress())
        .clinicPhone(request.getClinicPhone())
        .clinicEmail(request.getClinicEmail())
        .build();
  }

  public static UpdateClinicCommand toUpdateClinicCommand(UpdateClinicRequest request) {
    return UpdateClinicCommand.builder()
        .name(request.getName())
        .address(request.getAddress())
        .phone(request.getPhone())
        .email(request.getEmail())
        .build();
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
