package tfg.psygcv.service.appointment;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.List;
import org.springframework.stereotype.Component;
import tfg.psygcv.entity.appointment.Appointment;
import tfg.psygcv.entity.appointment.AppointmentStatus;
import tfg.psygcv.entity.clinic.VeterinaryClinic;
import tfg.psygcv.entity.user.User;
import tfg.psygcv.service.validation.BaseValidator;

@Component
public class AppointmentValidator extends BaseValidator {

  public void validateClientAppointmentCreation(
      String dateStr, Long petId, Long serviceId, Long clinicId, User client) {
    validateDateString(dateStr);
    validateId(petId);
    validateId(serviceId);
    validateId(clinicId);
    validateClient(client);
  }

  public void validateReceptionistAppointmentCreation(
      Appointment appointment, Long serviceId, Long receptionistId) {
    validateAppointment(appointment);
    validateId(serviceId);
    validateId(receptionistId);
    validateAppointmentBasicFields(appointment);
  }

  public void validateReschedule(Long appointmentId, Appointment updatedAppointment) {
    validateId(appointmentId);
    validateAppointment(updatedAppointment);
    validateAppointmentScheduleFields(updatedAppointment);
    if (updatedAppointment.getMedicalService() == null
        || updatedAppointment.getMedicalService().getId() == null) {
      throw new IllegalArgumentException("Medical service is required for rescheduling");
    }
  }

  public void validateAppointment(Appointment appointment) {
    validateNotNull(appointment, "Appointment cannot be null");
  }

  public void validateStatus(AppointmentStatus status) {
    validateNotNull(status, "Appointment status cannot be null");
  }

  public void validateClinics(List<VeterinaryClinic> clinics) {
    if (clinics == null || clinics.isEmpty()) {
      throw new IllegalArgumentException("Clinics list cannot be null or empty");
    }
  }

  private void validateDateString(String dateStr) {
    validateStringNotBlank(dateStr, "Date string");
    try {
      LocalDate date = LocalDate.parse(dateStr);
      if (date.isBefore(LocalDate.now())) {
        throw new IllegalArgumentException("Appointment date cannot be in the past");
      }
    } catch (DateTimeParseException e) {
      throw new IllegalArgumentException("Invalid date format. Expected format: YYYY-MM-DD");
    }
  }

  private void validateClient(User client) {
    validateNotNull(client, "Client cannot be null");
    validateNotNull(client.getId(), "Client must have a valid ID");
  }

  private void validateAppointmentBasicFields(Appointment appointment) {
    validateNotNull(appointment.getDate(), "Appointment date is required");
    validateNotNull(appointment.getTime(), "Appointment time is required");
    validateNotNull(appointment.getPet(), "Pet is required for appointment");
    validateNotNull(appointment.getCustomer(), "Client is required for appointment");
  }

  private void validateAppointmentScheduleFields(Appointment appointment) {
    validateNotNull(appointment.getDate(), "New appointment date is required");
    validateNotNull(appointment.getTime(), "New appointment time is required");
    if (appointment.getDate().isBefore(LocalDate.now())) {
      throw new IllegalArgumentException("New appointment date cannot be in the past");
    }
  }
}
