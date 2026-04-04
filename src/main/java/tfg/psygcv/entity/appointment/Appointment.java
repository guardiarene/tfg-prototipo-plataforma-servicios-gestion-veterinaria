package tfg.psygcv.entity.appointment;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.LocalTime;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;
import tfg.psygcv.entity.audit.AuditableEntity;
import tfg.psygcv.entity.clinic.MedicalService;
import tfg.psygcv.entity.clinic.VeterinaryClinic;
import tfg.psygcv.entity.medical.Visit;
import tfg.psygcv.entity.pet.Pet;
import tfg.psygcv.entity.user.User;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "APPOINTMENT")
public class Appointment extends AuditableEntity {

  @NotNull
  @FutureOrPresent
  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
  @Column(name = "DATE", nullable = false)
  private LocalDate date;

  @NotNull
  @DateTimeFormat(iso = DateTimeFormat.ISO.TIME)
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

  @NotNull
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
}
