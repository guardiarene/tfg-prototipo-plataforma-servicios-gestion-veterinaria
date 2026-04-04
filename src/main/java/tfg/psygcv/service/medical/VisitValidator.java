package tfg.psygcv.service.medical;

import java.time.LocalDate;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import tfg.psygcv.entity.clinic.VeterinaryClinic;
import tfg.psygcv.entity.medical.VisitType;
import tfg.psygcv.entity.pet.Pet;
import tfg.psygcv.entity.user.User;
import tfg.psygcv.repository.appointment.AppointmentQueryRepository;
import tfg.psygcv.service.validation.BaseValidator;

@Component
@RequiredArgsConstructor
public class VisitValidator extends BaseValidator {

  private final AppointmentQueryRepository appointmentQueryRepository;

  public void validateForCreation(CreateVisitCommand command, User veterinarian, Pet pet) {
    validateNotNull(command, "Visit command cannot be null");
    validateNotNull(veterinarian, "Veterinarian cannot be null");
    validateNotNull(pet, "Pet cannot be null");
    validateVisitFields(command.getDate(), command.getReasonForVisit(), command.getVisitType());
    validateVeterinarianClinicAccess(veterinarian, pet);
    validateAppointmentRequirement(command.getVisitType(), veterinarian, pet);
  }

  public void validateForUpdate(Long id, UpdateVisitCommand command, User veterinarian) {
    validateId(id);
    validateNotNull(command, "Visit command cannot be null");
    validateNotNull(veterinarian, "Veterinarian cannot be null");
    validateVisitFields(command.getDate(), command.getReasonForVisit(), command.getVisitType());
  }

  @Override
  public void validateId(Long id) {
    if (id == null || id <= 0) {
      throw new IllegalArgumentException("Visit ID must be a positive number");
    }
  }

  private void validateVisitFields(LocalDate date, String reasonForVisit, VisitType visitType) {
    validateNotNull(date, "Visit date cannot be null");
    validateStringNotBlank(reasonForVisit, "Reason for visit");
    validateNotNull(visitType, "Visit type cannot be null");
  }

  private void validateVeterinarianClinicAccess(User veterinarian, Pet pet) {
    if (hasVeterinarianAccessToPet(veterinarian, pet)) {
      throw new IllegalStateException("Pet is not registered in any of the veterinarian's clinics");
    }
  }

  private void validateAppointmentRequirement(VisitType visitType, User veterinarian, Pet pet) {
    if (visitType == VisitType.EMERGENCY) {
      return;
    }
    if (hasVeterinarianAccessToPet(veterinarian, pet)) {
      throw new IllegalStateException(
          "Pet has no registered appointments in veterinarian's clinics."
              + " Emergency visits can be created without appointments.");
    }
  }

  private boolean hasVeterinarianAccessToPet(User veterinarian, Pet pet) {
    Set<VeterinaryClinic> veterinarianClinics = veterinarian.getClinicsOwned();
    VeterinaryClinic workClinic = veterinarian.getWorkClinic();
    if ((veterinarianClinics == null || veterinarianClinics.isEmpty()) && workClinic == null) {
      throw new IllegalStateException("Veterinarian has no registered clinics or work clinic");
    }
    if (veterinarianClinics != null && !veterinarianClinics.isEmpty()) {
      if (appointmentQueryRepository.existsAppointmentByPetAndClinicsAndCustomer(
          pet, veterinarianClinics, pet.getOwner())) {
        return false;
      }
    }
    if (workClinic != null) {
      return !appointmentQueryRepository.existsAppointmentByPetAndClinicAndCustomer(
          pet, workClinic, pet.getOwner());
    }
    return true;
  }
}
