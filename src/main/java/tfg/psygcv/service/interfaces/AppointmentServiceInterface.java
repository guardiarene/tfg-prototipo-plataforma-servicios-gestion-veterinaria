package tfg.psygcv.service.interfaces;

import tfg.psygcv.model.appointment.Appointment;
import tfg.psygcv.model.appointment.AppointmentStatus;
import tfg.psygcv.model.clinic.VeterinaryClinic;
import tfg.psygcv.model.pet.Pet;
import tfg.psygcv.model.user.User;

import java.util.List;

public interface AppointmentServiceInterface {

    Appointment findById(Long appointmentId);

    Appointment findWithDetails(Long appointmentId);

    List<Appointment> findByCustomerId(Long clientId);

    List<Appointment> findByClinicId(Long clinicId);

    List<Pet> findPetsWithAppointmentsInClinics(List<VeterinaryClinic> clinics);

    String findVeterinarianName(Appointment appointment);

    Appointment createClientAppointment(String dateStr, Long petId, Long serviceId, Long clinicId, User client);

    Appointment createReceptionistAppointment(Appointment appointment, Long serviceId, Long receptionistId);

    Appointment updateStatus(Long appointmentId, AppointmentStatus status);

    Appointment reschedule(Long appointmentId, Appointment updatedAppointment);

    Appointment cancel(Long appointmentId);

}
