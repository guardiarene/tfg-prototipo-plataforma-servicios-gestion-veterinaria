package tfg.psygcv.repository.query;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import tfg.psygcv.model.appointment.Appointment;
import tfg.psygcv.model.clinic.VeterinaryClinic;
import tfg.psygcv.model.pet.Pet;
import tfg.psygcv.model.user.User;

@Repository
public interface AppointmentQueryRepository extends JpaRepository<Appointment, Long> {

  @Query(
      "SELECT DISTINCT a.pet FROM Appointment a WHERE a.clinic IN :clinics AND a.pet.active = true")
  List<Pet> findPetsWithAppointmentsInClinics(@Param("clinics") List<VeterinaryClinic> clinics);

  @Query(
      "SELECT COUNT(a) > 0 FROM Appointment a WHERE a.pet = :pet AND a.clinic IN :clinics AND a.customer = :customer")
  boolean existsAppointmentByPetAndClinicsAndCustomer(
      @Param("pet") Pet pet,
      @Param("clinics") List<VeterinaryClinic> clinics,
      @Param("customer") User customer);
}
