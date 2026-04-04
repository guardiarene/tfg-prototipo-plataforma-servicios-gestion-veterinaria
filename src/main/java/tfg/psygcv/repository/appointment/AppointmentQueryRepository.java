package tfg.psygcv.repository.appointment;

import java.util.Collection;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import tfg.psygcv.entity.appointment.Appointment;
import tfg.psygcv.entity.clinic.VeterinaryClinic;
import tfg.psygcv.entity.pet.Pet;
import tfg.psygcv.entity.user.User;

@Repository
public interface AppointmentQueryRepository extends JpaRepository<Appointment, Long> {

  @Query(
      "SELECT DISTINCT a.pet FROM Appointment a JOIN FETCH a.pet.owner WHERE a.clinic IN :clinics AND a.pet.active = true AND a.pet.medicalRecord IS NULL")
  List<Pet> findPetsWithAppointmentsInClinics(
      @Param("clinics") Collection<VeterinaryClinic> clinics);

  @Query(
      "SELECT COUNT(a) > 0 FROM Appointment a WHERE a.pet = :pet AND a.clinic IN :clinics AND a.customer = :customer")
  boolean existsAppointmentByPetAndClinicsAndCustomer(
      @Param("pet") Pet pet,
      @Param("clinics") Collection<VeterinaryClinic> clinics,
      @Param("customer") User customer);

  @Query(
      "SELECT COUNT(a) > 0 FROM Appointment a WHERE a.pet = :pet AND a.clinic = :clinic AND a.customer = :customer")
  boolean existsAppointmentByPetAndClinicAndCustomer(
      @Param("pet") Pet pet,
      @Param("clinic") VeterinaryClinic clinic,
      @Param("customer") User customer);
}
