package tfg.psygcv.appointment.service;

import java.time.LocalDate;
import java.util.List;
import org.springframework.security.access.prepost.PreAuthorize;
import tfg.psygcv.appointment.command.RescheduleAppointmentCommand;
import tfg.psygcv.appointment.command.ScheduleAppointmentCommand;
import tfg.psygcv.appointment.entity.Appointment;
import tfg.psygcv.appointment.entity.AppointmentStatus;
import tfg.psygcv.clinic.entity.VeterinaryClinic;
import tfg.psygcv.pet.entity.Pet;

public interface AppointmentService {

  Appointment findById(Long appointmentId);

  @PreAuthorize("@authz.canAccessAppointment(authentication, #appointmentId)")
  Appointment findWithDetails(Long appointmentId);

  List<Appointment> findByCustomerId(Long clientId);

  List<Appointment> findByClinicId(Long clinicId);

  List<Pet> findPetsWithAppointmentsInClinics(List<VeterinaryClinic> clinics);

  String findVeterinarianName(Appointment appointment);

  void createClientAppointment(
      LocalDate date, Long petId, Long serviceId, Long clinicId, Long clientId);

  void createReceptionistAppointment(ScheduleAppointmentCommand command, Long receptionistId);

  @PreAuthorize("@authz.canAccessAppointment(authentication, #appointmentId)")
  void updateStatus(Long appointmentId, AppointmentStatus status);

  @PreAuthorize("@authz.canAccessAppointment(authentication, #appointmentId)")
  void reschedule(Long appointmentId, RescheduleAppointmentCommand command);

  @PreAuthorize("@authz.canAccessAppointment(authentication, #appointmentId)")
  void cancel(Long appointmentId);
}
