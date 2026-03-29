package tfg.psygcv.service.interfaces;

import java.util.List;
import tfg.psygcv.model.appointment.Appointment;
import tfg.psygcv.model.appointment.AppointmentStatus;
import tfg.psygcv.model.clinic.VeterinaryClinic;
import tfg.psygcv.model.pet.Pet;
import tfg.psygcv.model.user.User;

public interface AppointmentServiceInterface {

  Appointment findById(Long appointmentId);

  Appointment findWithDetails(Long appointmentId);

  List<Appointment> findByCustomerId(Long clientId);

  List<Appointment> findByClinicId(Long clinicId);

  List<Pet> findPetsWithAppointmentsInClinics(List<VeterinaryClinic> clinics);

  String findVeterinarianName(Appointment appointment);

  void createClientAppointment(
      String dateStr, Long petId, Long serviceId, Long clinicId, User client);

  void createReceptionistAppointment(Appointment appointment, Long serviceId, Long receptionistId);

  void updateStatus(Long appointmentId, AppointmentStatus status);

  void reschedule(Long appointmentId, Appointment updatedAppointment);

  void cancel(Long appointmentId);
}
