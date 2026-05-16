package tfg.psygcv.appointment.service;

import java.time.LocalDate;
import java.util.List;
import org.springframework.stereotype.Component;
import tfg.psygcv.appointment.command.RescheduleAppointmentCommand;
import tfg.psygcv.appointment.command.ScheduleAppointmentCommand;
import tfg.psygcv.appointment.entity.Appointment;
import tfg.psygcv.appointment.entity.AppointmentStatus;
import tfg.psygcv.clinic.entity.VeterinaryClinic;
import tfg.psygcv.shared.validation.BaseValidator;

@Component
public class AppointmentValidator extends BaseValidator {

  public void validateClientAppointmentCreation(
      LocalDate date, Long petId, Long serviceId, Long clinicId, Long clientId) {
    validateNotNull(date, "Appointment date cannot be null");
    if (date.isBefore(LocalDate.now())) {
      throw new IllegalArgumentException("Appointment date cannot be in the past");
    }
    validateId(petId);
    validateId(serviceId);
    validateId(clinicId);
    validateId(clientId);
  }

  public void validateReceptionistAppointmentCreation(
      ScheduleAppointmentCommand command, Long receptionistId) {
    validateNotNull(command, "Schedule command cannot be null");
    validateNotNull(command.getDate(), "Appointment date is required");
    validateNotNull(command.getTime(), "Appointment time is required");
    validateNotNull(command.getPetId(), "Pet is required for appointment");
    validateId(command.getServiceId());
    validateId(receptionistId);
  }

  public void validateReschedule(Long appointmentId, RescheduleAppointmentCommand command) {
    validateId(appointmentId);
    validateNotNull(command, "Reschedule command cannot be null");
    validateNotNull(command.getDate(), "New appointment date is required");
    validateNotNull(command.getTime(), "New appointment time is required");
    if (command.getDate().isBefore(LocalDate.now())) {
      throw new IllegalArgumentException("New appointment date cannot be in the past");
    }
    validateNotNull(command.getMedicalServiceId(), "Medical service is required for rescheduling");
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
}
