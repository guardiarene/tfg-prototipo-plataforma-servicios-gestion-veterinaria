package tfg.psygcv.appointment.service;

import jakarta.persistence.EntityNotFoundException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tfg.psygcv.appointment.command.RescheduleAppointmentCommand;
import tfg.psygcv.appointment.command.ScheduleAppointmentCommand;
import tfg.psygcv.appointment.entity.Appointment;
import tfg.psygcv.appointment.entity.AppointmentStatus;
import tfg.psygcv.appointment.repository.AppointmentQueryRepository;
import tfg.psygcv.appointment.repository.AppointmentRepository;
import tfg.psygcv.clinic.entity.MedicalService;
import tfg.psygcv.clinic.entity.VeterinaryClinic;
import tfg.psygcv.clinic.service.MedicalServiceService;
import tfg.psygcv.clinic.service.VeterinaryClinicService;
import tfg.psygcv.pet.entity.Pet;
import tfg.psygcv.pet.service.PetService;
import tfg.psygcv.user.entity.User;
import tfg.psygcv.user.service.UserService;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class AppointmentServiceImpl implements AppointmentService {

  private final AppointmentRepository appointmentRepository;
  private final AppointmentQueryRepository appointmentQueryRepository;
  private final VeterinaryClinicService veterinaryClinicService;
  private final MedicalServiceService medicalServiceService;
  private final PetService petService;
  private final UserService userService;
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
      LocalDate date, Long petId, Long serviceId, Long clinicId, Long clientId) {
    appointmentValidator.validateClientAppointmentCreation(
        date, petId, serviceId, clinicId, clientId);
    User customer = userService.findById(clientId);
    Pet pet = petService.findById(petId);
    MedicalService service = medicalServiceService.findById(serviceId);
    VeterinaryClinic clinic = veterinaryClinicService.findById(clinicId);
    Appointment appointment = buildClientAppointment(date, pet, service, clinic, customer);
    appointmentRepository.save(appointment);
  }

  @Override
  @Transactional
  public void createReceptionistAppointment(
      ScheduleAppointmentCommand command, Long receptionistId) {
    appointmentValidator.validateReceptionistAppointmentCreation(command, receptionistId);
    User customer = userService.findById(command.getCustomerId());
    Pet pet = petService.findById(command.getPetId());
    VeterinaryClinic clinic = veterinaryClinicService.findByReceptionistId(receptionistId);
    MedicalService service = medicalServiceService.findById(command.getServiceId());
    Appointment appointment = new Appointment();
    appointment.setDate(command.getDate());
    appointment.setTime(command.getTime());
    appointment.setCustomer(customer);
    appointment.setPet(pet);
    appointment.setClinic(clinic);
    appointment.setMedicalService(service);
    appointment.setAppointmentStatus(AppointmentStatus.CONFIRMED);
    appointmentRepository.save(appointment);
  }

  @Override
  @Transactional
  public void updateStatus(Long appointmentId, AppointmentStatus status) {
    appointmentValidator.validateId(appointmentId);
    appointmentValidator.validateStatus(status);
    Appointment appointment = findWithDetails(appointmentId);
    appointment.setAppointmentStatus(status);
    appointmentRepository.save(appointment);
  }

  @Override
  @Transactional
  public void reschedule(Long appointmentId, RescheduleAppointmentCommand command) {
    appointmentValidator.validateReschedule(appointmentId, command);
    Appointment existingAppointment = findWithDetails(appointmentId);
    MedicalService service = medicalServiceService.findById(command.getMedicalServiceId());
    existingAppointment.setDate(command.getDate());
    existingAppointment.setTime(command.getTime());
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
