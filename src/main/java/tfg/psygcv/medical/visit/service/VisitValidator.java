package tfg.psygcv.medical.visit.service;

import java.time.LocalDate;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import tfg.psygcv.appointment.repository.AppointmentQueryRepository;
import tfg.psygcv.clinic.entity.VeterinaryClinic;
import tfg.psygcv.exception.domain.BusinessRuleException;
import tfg.psygcv.medical.visit.command.CreateVisitCommand;
import tfg.psygcv.medical.visit.command.UpdateVisitCommand;
import tfg.psygcv.medical.visit.entity.VisitType;
import tfg.psygcv.pet.entity.Pet;
import tfg.psygcv.shared.validation.BaseValidator;
import tfg.psygcv.user.entity.User;

@Component
@RequiredArgsConstructor
public class VisitValidator extends BaseValidator {

  private final AppointmentQueryRepository appointmentQueryRepository;

  public void validateForCreation(CreateVisitCommand command, User veterinarian, Pet pet) {
    validateNotNull(command, "El comando de la visita no puede ser nulo");
    validateNotNull(veterinarian, "El veterinario no puede ser nulo");
    validateNotNull(pet, "La mascota no puede ser nula");
    validateVisitFields(command.getDate(), command.getReasonForVisit(), command.getVisitType());
    validateVeterinarianClinicAccess(veterinarian, pet);
    validateAppointmentRequirement(command.getVisitType(), veterinarian, pet);
  }

  public void validateForUpdate(Long id, UpdateVisitCommand command, User veterinarian) {
    validateId(id);
    validateNotNull(command, "El comando de la visita no puede ser nulo");
    validateNotNull(veterinarian, "El veterinario no puede ser nulo");
    validateVisitFields(command.getDate(), command.getReasonForVisit(), command.getVisitType());
  }

  @Override
  public void validateId(Long id) {
    if (id == null || id <= 0) {
      throw new BusinessRuleException("El identificador de la visita no es válido");
    }
  }

  private void validateVisitFields(LocalDate date, String reasonForVisit, VisitType visitType) {
    validateNotNull(date, "La fecha de la visita es obligatoria");
    validateStringNotBlank(reasonForVisit, "El motivo de la visita");
    validateNotNull(visitType, "El tipo de visita es obligatorio");
  }

  private void validateVeterinarianClinicAccess(User veterinarian, Pet pet) {
    if (hasVeterinarianAccessToPet(veterinarian, pet)) {
      throw new BusinessRuleException(
          "La mascota no tiene turnos registrados en ninguna clínica del veterinario");
    }
  }

  private void validateAppointmentRequirement(VisitType visitType, User veterinarian, Pet pet) {
    if (visitType == VisitType.EMERGENCY) {
      return;
    }
    if (hasVeterinarianAccessToPet(veterinarian, pet)) {
      throw new BusinessRuleException(
          "La mascota no tiene turnos en las clínicas del veterinario."
              + " Las visitas de emergencia pueden crearse sin turno previo.");
    }
  }

  private boolean hasVeterinarianAccessToPet(User veterinarian, Pet pet) {
    Set<VeterinaryClinic> veterinarianClinics = veterinarian.getClinicsOwned();
    VeterinaryClinic workClinic = veterinarian.getWorkClinic();
    if ((veterinarianClinics == null || veterinarianClinics.isEmpty()) && workClinic == null) {
      throw new BusinessRuleException(
          "El veterinario no tiene clínicas registradas ni clínica de trabajo asignada");
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
