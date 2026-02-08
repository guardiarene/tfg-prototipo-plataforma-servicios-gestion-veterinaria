package tfg.psygcv.model.appointment;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.LocalTime;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import tfg.psygcv.model.clinic.MedicalService;
import tfg.psygcv.model.clinic.VeterinaryClinic;
import tfg.psygcv.model.pet.Pet;
import tfg.psygcv.model.user.User;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "APPOINTMENT")
public class Appointment {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "ID")
  private Long id;

  @NotNull
  @Column(name = "DATE", nullable = false)
  private LocalDate date;

  @NotNull
  @Column(name = "TIME", nullable = false)
  private LocalTime time;

  @NotNull
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "VETERINARY_CLINIC_ID", nullable = false)
  private VeterinaryClinic clinic;

  @NotNull
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "MEDICAL_SERVICE_ID", nullable = false)
  private MedicalService medicalService;

  @NotNull
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "CUSTOMER_ID", nullable = false)
  private User customer;

  @NotNull
  @Enumerated(EnumType.STRING)
  @Column(name = "STATUS", nullable = false)
  private AppointmentStatus appointmentStatus;

  @NotNull
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "PET_ID", nullable = false)
  private Pet pet;

  @NotNull
  @Column(name = "ACTIVE", nullable = false)
  private Boolean active = true;
}
