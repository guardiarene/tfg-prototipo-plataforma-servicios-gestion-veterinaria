package tfg.psygcv.mapper.appointment;

import tfg.psygcv.dto.appointment.response.AppointmentResponse;
import tfg.psygcv.dto.appointment.response.AppointmentSummaryResponse;
import tfg.psygcv.entity.appointment.Appointment;

public class AppointmentMapper {

  private AppointmentMapper() {
    throw new UnsupportedOperationException("Utility class");
  }

  public static AppointmentSummaryResponse toSummary(Appointment appointment) {
    AppointmentSummaryResponse summary = new AppointmentSummaryResponse();
    summary.setId(appointment.getId());
    summary.setDate(appointment.getDate());
    summary.setTime(appointment.getTime());
    summary.setAppointmentStatus(appointment.getAppointmentStatus());
    if (appointment.getClinic() != null) {
      summary.setClinicName(appointment.getClinic().getName());
      summary.setClinicAddress(appointment.getClinic().getAddress());
    }
    if (appointment.getPet() != null) {
      summary.setPetName(appointment.getPet().getName());
      summary.setPetSpeciesDescription(appointment.getPet().getSpecies().getDescription());
    }
    if (appointment.getCustomer() != null) {
      summary.setCustomerFullName(
          appointment.getCustomer().getFirstName() + " " + appointment.getCustomer().getLastName());
    }
    return summary;
  }

  public static AppointmentResponse toResponse(Appointment appointment) {
    AppointmentResponse response = new AppointmentResponse();
    response.setId(appointment.getId());
    response.setDate(appointment.getDate());
    response.setTime(appointment.getTime());
    response.setAppointmentStatus(appointment.getAppointmentStatus());
    if (appointment.getClinic() != null) {
      response.setClinicName(appointment.getClinic().getName());
      response.setClinicAddress(appointment.getClinic().getAddress());
      response.setClinicPhone(appointment.getClinic().getPhone());
      response.setClinicEmail(appointment.getClinic().getEmail());
    }
    if (appointment.getMedicalService() != null) {
      response.setMedicalServiceName(appointment.getMedicalService().getName());
      response.setMedicalServiceId(appointment.getMedicalService().getId());
    }
    if (appointment.getPet() != null) {
      response.setPetName(appointment.getPet().getName());
      response.setPetSpeciesDescription(appointment.getPet().getSpecies().getDescription());
    }
    if (appointment.getCustomer() != null) {
      response.setCustomerFullName(
          appointment.getCustomer().getFirstName() + " " + appointment.getCustomer().getLastName());
    }
    return response;
  }
}
