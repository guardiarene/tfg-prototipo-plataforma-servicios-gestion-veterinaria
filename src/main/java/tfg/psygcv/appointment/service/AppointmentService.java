package tfg.psygcv.appointment.service;

import java.time.LocalDate;
import java.util.List;
import tfg.psygcv.appointment.command.RescheduleAppointmentCommand;
import tfg.psygcv.appointment.command.ScheduleAppointmentCommand;
import tfg.psygcv.appointment.entity.Appointment;
import tfg.psygcv.appointment.entity.AppointmentStatus;
import tfg.psygcv.clinic.entity.VeterinaryClinic;
import tfg.psygcv.pet.entity.Pet;

public interface AppointmentService {

  Appointment findById(Long appointmentId);

  Appointment findWithDetails(Long appointmentId);

  List<Appointment> findByCustomerId(Long clientId);

  List<Appointment> findByClinicId(Long clinicId);

  List<Pet> findPetsWithAppointmentsInClinics(List<VeterinaryClinic> clinics);

  String findVeterinarianName(Appointment appointment);

  void createClientAppointment(
      LocalDate date, Long petId, Long serviceId, Long clinicId, Long clientId);

  void createReceptionistAppointment(ScheduleAppointmentCommand command, Long receptionistId);

  void updateStatus(Long appointmentId, AppointmentStatus status);

  void reschedule(Long appointmentId, RescheduleAppointmentCommand command);

  void cancel(Long appointmentId);
}
