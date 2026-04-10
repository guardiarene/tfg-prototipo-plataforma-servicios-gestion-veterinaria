package tfg.psygcv.appointment.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import java.time.LocalDate;
import java.time.LocalTime;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import tfg.psygcv.clinic.entity.MedicalService;
import tfg.psygcv.clinic.entity.VeterinaryClinic;
import tfg.psygcv.medical.visit.entity.Visit;
import tfg.psygcv.pet.entity.Pet;
import tfg.psygcv.shared.entity.AuditableEntity;
import tfg.psygcv.user.entity.User;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "APPOINTMENT")
public class Appointment extends AuditableEntity {

  @Column(name = "DATE", nullable = false)
  private LocalDate date;

  @Column(name = "TIME", nullable = false)
  private LocalTime time;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "VETERINARY_CLINIC_ID", nullable = false)
  private VeterinaryClinic clinic;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "MEDICAL_SERVICE_ID", nullable = false)
  private MedicalService medicalService;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "CUSTOMER_ID", nullable = false)
  private User customer;

  @Enumerated(EnumType.STRING)
  @Column(name = "APPOINTMENT_STATUS", nullable = false, length = 15)
  private AppointmentStatus appointmentStatus;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "PET_ID", nullable = false)
  private Pet pet;

  @OneToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "VISIT_ID", unique = true)
  private Visit visit;

  public void setVisit(Visit visit) {
    this.visit = visit;
    if (visit != null && visit.getAppointment() != this) {
      visit.setAppointment(this);
    }
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (!(o instanceof Appointment other)) return false;
    return getId() != null && getId().equals(other.getId());
  }

  @Override
  public int hashCode() {
    return getClass().hashCode();
  }
}
