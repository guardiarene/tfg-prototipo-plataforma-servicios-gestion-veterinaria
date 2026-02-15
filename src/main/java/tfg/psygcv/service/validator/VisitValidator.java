package tfg.psygcv.service.validator;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import tfg.psygcv.model.appointment.Appointment;
import tfg.psygcv.model.clinic.VeterinaryClinic;
import tfg.psygcv.model.medical.Visit;
import tfg.psygcv.model.medical.VisitType;
import tfg.psygcv.model.pet.Pet;
import tfg.psygcv.model.user.User;
import tfg.psygcv.repository.query.AppointmentQueryRepository;

@Component
@RequiredArgsConstructor
public class VisitValidator {

  private final AppointmentQueryRepository appointmentQueryRepository;

  public void validateForCreation(Visit visit, User veterinarian, Pet pet) {
    validateNotNull(visit, "Visit cannot be null");
    validateNotNull(veterinarian, "Veterinarian cannot be null");
    validateNotNull(pet, "Pet cannot be null");

    validateVisitFields(visit);
    validateVeterinarianClinicAccess(veterinarian, pet);
    validateAppointmentRequirement(visit, veterinarian, pet);
  }

  public void validateForUpdate(Long id, Visit visit, User veterinarian) {
    validateId(id);
    validateNotNull(visit, "Visit cannot be null");
    validateNotNull(veterinarian, "Veterinarian cannot be null");
    validateVisitFields(visit);
  }

  public void validateId(Long id) {
    if (id == null || id <= 0) {
      throw new IllegalArgumentException("Visit ID must be a positive number");
    }
  }

  private void validateVisitFields(Visit visit) {
    if (visit.getDate() == null) {
      throw new IllegalArgumentException("Visit date cannot be null");
    }

    if (visit.getReasonForVisit() == null || visit.getReasonForVisit().isBlank()) {
      throw new IllegalArgumentException("Reason for visit cannot be empty");
    }

    if (visit.getVisitType() == null) {
      throw new IllegalArgumentException("Visit type cannot be null");
    }
  }

  private void validateVeterinarianClinicAccess(User veterinarian, Pet pet) {
    List<VeterinaryClinic> veterinarianClinics = veterinarian.getClinicsOwned();

    if (veterinarianClinics == null || veterinarianClinics.isEmpty()) {
      throw new IllegalStateException("Veterinarian has no registered clinics");
    }

    boolean hasPetAccess =
        appointmentQueryRepository.existsAppointmentByPetAndClinicsAndCustomer(
            pet, veterinarianClinics, pet.getOwner());

    if (!hasPetAccess) {
      throw new IllegalStateException(
          "Pet is not registered in any of the veterinarian's clinics");
    }
  }

  private void validateAppointmentRequirement(Visit visit, User veterinarian, Pet pet) {
    // Visitas de emergencia no requieren cita previa
    if (visit.getVisitType() == VisitType.EMERGENCY) {
      return;
    }

    // Para otros tipos de visita, verificar que existe cita
    List<VeterinaryClinic> veterinarianClinics = veterinarian.getClinicsOwned();
    boolean hasAppointments =
        appointmentQueryRepository.existsAppointmentByPetAndClinicsAndCustomer(
            pet, veterinarianClinics, pet.getOwner());

    if (!hasAppointments) {
      throw new IllegalStateException(
          "Pet has no registered appointments in veterinarian's clinics. Emergency visits can be created without appointments.");
    }
  }

  private void validateNotNull(Object object, String message) {
    if (object == null) {
      throw new IllegalArgumentException(message);
    }
  }

  public void validateAppointmentCompatibility(Visit visit, Appointment appointment) {
    if (appointment == null) {
      return; // Visitas sin cita son válidas (ej: emergencias)
    }

    if (visit.getVisitType() == VisitType.EMERGENCY) {
      throw new IllegalStateException("Emergency visits should not be linked to appointments");
    }

    // Validar que la cita corresponde a la mascota correcta
    if (visit.getMedicalRecord() != null
        && visit.getMedicalRecord().getPet() != null
        && !visit.getMedicalRecord().getPet().getId().equals(appointment.getPet().getId())) {
      throw new IllegalStateException("Appointment does not belong to the same pet");
    }
  }
}
