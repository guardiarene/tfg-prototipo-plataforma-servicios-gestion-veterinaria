package tfg.psygcv.repository.base;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import tfg.psygcv.model.appointment.Appointment;

@Repository
public interface AppointmentRepository extends JpaRepository<Appointment, Long> {

  @Query(
      "SELECT a FROM Appointment a "
          + "JOIN FETCH a.clinic "
          + "JOIN FETCH a.pet "
          + "LEFT JOIN FETCH a.medicalService "
          + "WHERE a.customer.id = :customerId")
  List<Appointment> findByCustomerId(@Param("customerId") Long customerId);

  @Query(
      "SELECT a FROM Appointment a "
          + "JOIN FETCH a.clinic "
          + "JOIN FETCH a.pet "
          + "LEFT JOIN FETCH a.medicalService "
          + "WHERE a.clinic.id = :clinicId")
  List<Appointment> findByClinicId(@Param("clinicId") Long clinicId);

  @Query(
      "SELECT a FROM Appointment a "
          + "JOIN FETCH a.clinic "
          + "JOIN FETCH a.pet "
          + "LEFT JOIN FETCH a.medicalService "
          + "WHERE a.id = :id")
  Optional<Appointment> findByIdWithDetails(@Param("id") Long id);
}
