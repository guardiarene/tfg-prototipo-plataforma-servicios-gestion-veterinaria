package tfg.psygcv.appointment.service;

import java.time.LocalDate;
import java.util.List;
import org.springframework.stereotype.Component;
import tfg.psygcv.appointment.command.RescheduleAppointmentCommand;
import tfg.psygcv.appointment.command.ScheduleAppointmentCommand;
import tfg.psygcv.appointment.entity.Appointment;
import tfg.psygcv.appointment.entity.AppointmentStatus;
import tfg.psygcv.clinic.entity.VeterinaryClinic;
import tfg.psygcv.exception.domain.BusinessRuleException;
import tfg.psygcv.shared.validation.BaseValidator;

@Component
public class AppointmentValidator extends BaseValidator {

  public void validateClientAppointmentCreation(
      LocalDate date, Long petId, Long serviceId, Long clinicId, Long clientId) {
    validateNotNull(date, "La fecha del turno es obligatoria");
    if (date.isBefore(LocalDate.now())) {
      throw new BusinessRuleException("La fecha del turno no puede ser en el pasado");
    }
    validateId(petId);
    validateId(serviceId);
    validateId(clinicId);
    validateId(clientId);
  }

  public void validateReceptionistAppointmentCreation(
      ScheduleAppointmentCommand command, Long receptionistId) {
    validateNotNull(command, "El comando de agendamiento no puede ser nulo");
    validateNotNull(command.getDate(), "La fecha del turno es obligatoria");
    validateNotNull(command.getTime(), "La hora del turno es obligatoria");
    validateNotNull(command.getPetId(), "La mascota es obligatoria para el turno");
    validateId(command.getServiceId());
    validateId(receptionistId);
  }

  public void validateReschedule(Long appointmentId, RescheduleAppointmentCommand command) {
    validateId(appointmentId);
    validateNotNull(command, "El comando de reprogramación no puede ser nulo");
    validateNotNull(command.getDate(), "La nueva fecha del turno es obligatoria");
    validateNotNull(command.getTime(), "La nueva hora del turno es obligatoria");
    if (command.getDate().isBefore(LocalDate.now())) {
      throw new BusinessRuleException("La nueva fecha del turno no puede ser en el pasado");
    }
    validateNotNull(
        command.getMedicalServiceId(), "El servicio médico es obligatorio para reprogramar");
  }

  public void validateAppointment(Appointment appointment) {
    validateNotNull(appointment, "El turno no puede ser nulo");
  }

  public void validateStatus(AppointmentStatus status) {
    validateNotNull(status, "El estado del turno no puede ser nulo");
  }

  public void validateClinics(List<VeterinaryClinic> clinics) {
    if (clinics == null || clinics.isEmpty()) {
      throw new BusinessRuleException("La lista de clínicas no puede estar vacía");
    }
  }
}
