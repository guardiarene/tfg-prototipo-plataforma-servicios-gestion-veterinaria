package tfg.psygcv.service.impl;

import jakarta.persistence.EntityNotFoundException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tfg.psygcv.model.appointment.Appointment;
import tfg.psygcv.model.appointment.AppointmentStatus;
import tfg.psygcv.model.clinic.MedicalService;
import tfg.psygcv.model.clinic.VeterinaryClinic;
import tfg.psygcv.model.pet.Pet;
import tfg.psygcv.model.user.User;
import tfg.psygcv.repository.base.AppointmentRepository;
import tfg.psygcv.repository.query.AppointmentQueryRepository;
import tfg.psygcv.service.interfaces.AppointmentServiceInterface;
import tfg.psygcv.service.interfaces.MedicalServiceServiceInterface;
import tfg.psygcv.service.interfaces.PetServiceInterface;
import tfg.psygcv.service.interfaces.VeterinaryClinicServiceInterface;
import tfg.psygcv.service.validator.AppointmentValidator;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class AppointmentServiceImpl implements AppointmentServiceInterface {

  private final AppointmentRepository appointmentRepository;
  private final AppointmentQueryRepository appointmentQueryRepository;
  private final VeterinaryClinicServiceInterface veterinaryClinicService;
  private final MedicalServiceServiceInterface medicalServiceService;
  private final PetServiceInterface petService;
  private final AppointmentValidator appointmentValidator;

  @Override
  public Appointment findById(Long appointmentId) {
    appointmentValidator.validateId(appointmentId);
    return appointmentRepository
        .findById(appointmentId)
        .orElseThrow(
            () -> new EntityNotFoundException("Appointment not found with ID: " + appointmentId));
  }

  @Override
  public Appointment findWithDetails(Long appointmentId) {
    appointmentValidator.validateId(appointmentId);
    return appointmentRepository
        .findByIdWithDetails(appointmentId)
        .orElseThrow(
            () -> new EntityNotFoundException("Appointment not found with ID: " + appointmentId));
  }

  @Override
  public List<Appointment> findByCustomerId(Long customerId) {
    appointmentValidator.validateId(customerId);
    return appointmentRepository.findByCustomerId(customerId);
  }

  @Override
  public List<Appointment> findByClinicId(Long clinicId) {
    appointmentValidator.validateId(clinicId);
    return appointmentRepository.findByClinicId(clinicId);
  }

  @Override
  public List<Pet> findPetsWithAppointmentsInClinics(List<VeterinaryClinic> clinics) {
    appointmentValidator.validateClinics(clinics);
    return appointmentQueryRepository.findPetsWithAppointmentsInClinics(clinics);
  }

  @Override
  public String findVeterinarianName(Appointment appointment) {
    appointmentValidator.validateAppointment(appointment);
    return Optional.ofNullable(appointment.getMedicalService())
        .map(MedicalService::getClinic)
        .map(VeterinaryClinic::getOwner)
        .map(User::getFirstName)
        .orElse("Veterinarian not assigned");
  }

  @Override
  @Transactional
  public void createClientAppointment(
      String dateStr, Long petId, Long serviceId, Long clinicId, User client) {
    appointmentValidator.validateClientAppointmentCreation(
        dateStr, petId, serviceId, clinicId, client);
    LocalDate date = LocalDate.parse(dateStr);
    Pet pet = petService.findById(petId);
    MedicalService service = medicalServiceService.findById(serviceId);
    VeterinaryClinic clinic = veterinaryClinicService.findById(clinicId);
    Appointment appointment = buildClientAppointment(date, pet, service, clinic, client);
    appointmentRepository.save(appointment);
  }

  @Override
  @Transactional
  public void createReceptionistAppointment(
      Appointment appointment, Long serviceId, Long receptionistId) {
    appointmentValidator.validateReceptionistAppointmentCreation(
        appointment, serviceId, receptionistId);
    VeterinaryClinic clinic = veterinaryClinicService.findByReceptionistId(receptionistId);
    MedicalService service = medicalServiceService.findById(serviceId);
    appointment.setClinic(clinic);
    appointment.setMedicalService(service);
    appointment.setAppointmentStatus(AppointmentStatus.CONFIRMED);
    appointmentRepository.save(appointment);
  }

  @Override
  public void updateStatus(Long appointmentId, AppointmentStatus status) {
    appointmentValidator.validateId(appointmentId);
    appointmentValidator.validateStatus(status);
    Appointment appointment = findWithDetails(appointmentId);
    appointment.setAppointmentStatus(status);
    appointmentRepository.save(appointment);
  }

  @Override
  @Transactional
  public void reschedule(Long appointmentId, Appointment updatedAppointment) {
    appointmentValidator.validateReschedule(appointmentId, updatedAppointment);
    Appointment existingAppointment = findWithDetails(appointmentId);
    MedicalService service =
        medicalServiceService.findById(updatedAppointment.getMedicalService().getId());
    existingAppointment.setDate(updatedAppointment.getDate());
    existingAppointment.setTime(updatedAppointment.getTime());
    existingAppointment.setMedicalService(service);
    existingAppointment.setAppointmentStatus(AppointmentStatus.CONFIRMED);
    appointmentRepository.save(existingAppointment);
  }

  @Override
  @Transactional
  public void cancel(Long appointmentId) {
    updateStatus(appointmentId, AppointmentStatus.CANCELLED);
  }

  private Appointment buildClientAppointment(
      LocalDate date, Pet pet, MedicalService service, VeterinaryClinic clinic, User customer) {
    Appointment appointment = new Appointment();
    appointment.setDate(date);
    appointment.setTime(LocalTime.of(0, 0));
    appointment.setClinic(clinic);
    appointment.setMedicalService(service);
    appointment.setCustomer(customer);
    appointment.setPet(pet);
    appointment.setAppointmentStatus(AppointmentStatus.PENDING);
    return appointment;
  }
}
