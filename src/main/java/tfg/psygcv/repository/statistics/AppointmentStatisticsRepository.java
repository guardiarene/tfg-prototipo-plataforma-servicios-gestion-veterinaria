package tfg.psygcv.repository.statistics;

import java.time.LocalDate;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import tfg.psygcv.model.appointment.Appointment;

@Repository
public interface AppointmentStatisticsRepository extends JpaRepository<Appointment, Long> {

  @Query(
      "SELECT a.date, COUNT(a) FROM Appointment a WHERE a.clinic.id = :clinicId AND a.date BETWEEN :startDate AND :endDate GROUP BY a.date ORDER BY a.date ASC")
  List<Object[]> countAppointmentsByDate(
      @Param("clinicId") Long clinicId,
      @Param("startDate") LocalDate startDate,
      @Param("endDate") LocalDate endDate);

  @Query(
      "SELECT s.name, COUNT(a) FROM Appointment a JOIN a.medicalService s WHERE a.clinic.id = :clinicId AND a.date BETWEEN :startDate AND :endDate GROUP BY s.name ORDER BY COUNT(a) DESC")
  List<Object[]> countRequestedServices(
      @Param("clinicId") Long clinicId,
      @Param("startDate") LocalDate startDate,
      @Param("endDate") LocalDate endDate);
}
