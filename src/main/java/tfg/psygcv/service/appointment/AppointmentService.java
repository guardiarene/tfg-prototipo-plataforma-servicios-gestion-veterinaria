package tfg.psygcv.service.appointment;

import java.util.List;
import tfg.psygcv.entity.appointment.Appointment;
import tfg.psygcv.entity.appointment.AppointmentStatus;
import tfg.psygcv.entity.clinic.VeterinaryClinic;
import tfg.psygcv.entity.pet.Pet;
import tfg.psygcv.entity.user.User;

public interface AppointmentService {

  Appointment findById(Long appointmentId);

  Appointment findWithDetails(Long appointmentId);

  List<Appointment> findByCustomerId(Long clientId);

  List<Appointment> findByClinicId(Long clinicId);

  List<Pet> findPetsWithAppointmentsInClinics(List<VeterinaryClinic> clinics);

  String findVeterinarianName(Appointment appointment);

  void createClientAppointment(
      String dateStr, Long petId, Long serviceId, Long clinicId, User client);

  void createReceptionistAppointment(
      Appointment appointment, Long customerId, Long serviceId, Long receptionistId);

  void updateStatus(Long appointmentId, AppointmentStatus status);

  void reschedule(Long appointmentId, Appointment updatedAppointment);

  void cancel(Long appointmentId);
}
