package tfg.psygcv.repository.base;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tfg.psygcv.model.appointment.Appointment;

@Repository
public interface AppointmentRepository extends JpaRepository<Appointment, Long> {

  List<Appointment> findByCustomerId(Long customerId);

  List<Appointment> findByClinicId(Long clinicId);
}
