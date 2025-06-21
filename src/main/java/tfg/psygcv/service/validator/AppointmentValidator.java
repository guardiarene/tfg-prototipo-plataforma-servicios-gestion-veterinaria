package tfg.psygcv.service.validator;

import org.springframework.stereotype.Component;
import tfg.psygcv.model.appointment.Appointment;
import tfg.psygcv.model.appointment.AppointmentStatus;
import tfg.psygcv.model.clinic.VeterinaryClinic;
import tfg.psygcv.model.user.User;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.List;

@Component
public class AppointmentValidator {

    public void validateId(Long id) {
        if (id == null || id <= 0) {
            throw new IllegalArgumentException("Invalid ID: must be positive and not null");
        }
    }

    public void validateClientAppointmentCreation(String dateStr, Long petId, Long serviceId, Long clinicId, User client) {
        validateDateString(dateStr);
        validateId(petId);
        validateId(serviceId);
        validateId(clinicId);
        validateClient(client);
    }

    public void validateReceptionistAppointmentCreation(Appointment appointment, Long serviceId, Long receptionistId) {
        validateAppointment(appointment);
        validateId(serviceId);
        validateId(receptionistId);
        validateAppointmentBasicFields(appointment);
    }

    public void validateReschedule(Long appointmentId, Appointment updatedAppointment) {
        validateId(appointmentId);
        validateAppointment(updatedAppointment);
        validateAppointmentScheduleFields(updatedAppointment);
        if (updatedAppointment.getMedicalService() == null || updatedAppointment.getMedicalService().getId() == null) {
            throw new IllegalArgumentException("Medical service is required for rescheduling");
        }
    }

    public void validateAppointment(Appointment appointment) {
        if (appointment == null) {
            throw new IllegalArgumentException("Appointment cannot be null");
        }
    }

    public void validateStatus(AppointmentStatus status) {
        if (status == null) {
            throw new IllegalArgumentException("Appointment status cannot be null");
        }
    }

    public void validateClinics(List<VeterinaryClinic> clinics) {
        if (clinics == null || clinics.isEmpty()) {
            throw new IllegalArgumentException("Clinics list cannot be null or empty");
        }
    }

    private void validateDateString(String dateStr) {
        if (dateStr == null || dateStr.trim().isEmpty()) {
            throw new IllegalArgumentException("Date string cannot be null or empty");
        }
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
        if (client == null) {
            throw new IllegalArgumentException("Client cannot be null");
        }
        if (client.getId() == null) {
            throw new IllegalArgumentException("Client must have a valid ID");
        }
    }

    private void validateAppointmentBasicFields(Appointment appointment) {
        if (appointment.getDate() == null) {
            throw new IllegalArgumentException("Appointment date is required");
        }
        if (appointment.getTime() == null) {
            throw new IllegalArgumentException("Appointment time is required");
        }
        if (appointment.getPet() == null) {
            throw new IllegalArgumentException("Pet is required for appointment");
        }
        if (appointment.getCustomer() == null) {
            throw new IllegalArgumentException("Client is required for appointment");
        }
    }

    private void validateAppointmentScheduleFields(Appointment appointment) {
        if (appointment.getDate() == null) {
            throw new IllegalArgumentException("New appointment date is required");
        }
        if (appointment.getTime() == null) {
            throw new IllegalArgumentException("New appointment time is required");
        }
        if (appointment.getDate().isBefore(LocalDate.now())) {
            throw new IllegalArgumentException("New appointment date cannot be in the past");
        }
    }

}
