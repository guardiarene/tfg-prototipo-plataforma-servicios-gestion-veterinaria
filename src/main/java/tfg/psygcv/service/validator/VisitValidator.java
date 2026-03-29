package tfg.psygcv.service.validator;

import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import tfg.psygcv.model.clinic.VeterinaryClinic;
import tfg.psygcv.model.medical.Visit;
import tfg.psygcv.model.medical.VisitType;
import tfg.psygcv.model.pet.Pet;
import tfg.psygcv.model.user.User;
import tfg.psygcv.repository.query.AppointmentQueryRepository;

@Component
@RequiredArgsConstructor
public class VisitValidator extends BaseValidator {

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

  @Override
  public void validateId(Long id) {
    if (id == null || id <= 0) {
      throw new IllegalArgumentException("Visit ID must be a positive number");
    }
  }

  private void validateVisitFields(Visit visit) {
    validateNotNull(visit.getDate(), "Visit date cannot be null");
    validateStringNotBlank(visit.getReasonForVisit(), "Reason for visit");
    validateNotNull(visit.getVisitType(), "Visit type cannot be null");
  }

  private void validateVeterinarianClinicAccess(User veterinarian, Pet pet) {
    if (hasVeterinarianAccessToPet(veterinarian, pet)) {
      throw new IllegalStateException("Pet is not registered in any of the veterinarian's clinics");
    }
  }

  private void validateAppointmentRequirement(Visit visit, User veterinarian, Pet pet) {
    if (visit.getVisitType() == VisitType.EMERGENCY) {
      return;
    }
    if (hasVeterinarianAccessToPet(veterinarian, pet)) {
      throw new IllegalStateException(
          "Pet has no registered appointments in veterinarian's clinics. Emergency visits can be created without appointments.");
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
