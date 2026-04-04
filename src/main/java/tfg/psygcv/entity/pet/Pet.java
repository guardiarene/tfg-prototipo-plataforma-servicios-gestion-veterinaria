package tfg.psygcv.entity.pet;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.PositiveOrZero;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;
import tfg.psygcv.entity.appointment.Appointment;
import tfg.psygcv.entity.audit.AuditableEntity;
import tfg.psygcv.entity.medical.MedicalRecord;
import tfg.psygcv.entity.user.User;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "PET")
public class Pet extends AuditableEntity {

  @NotBlank
  @Column(name = "NAME", nullable = false, length = 50)
  private String name;

  @NotNull
  @Enumerated(EnumType.STRING)
  @Column(name = "SEX", nullable = false, length = 10)
  private Sex sex;

  @NotNull
  @Enumerated(EnumType.STRING)
  @Column(name = "BREED", nullable = false, length = 30)
  private Breed breed;

  @NotNull
  @Enumerated(EnumType.STRING)
  @Column(name = "SPECIES", nullable = false, length = 15)
  private Species species;

  @NotNull
  @PastOrPresent
  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
  @Column(name = "BIRTH_DATE", nullable = false)
  private LocalDate birthDate;

  @NotNull
  @PositiveOrZero
  @Column(name = "WEIGHT", nullable = false)
  private Float weight;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "OWNER_ID", nullable = false)
  private User owner;

  @OneToOne(mappedBy = "pet", cascade = CascadeType.ALL)
  private MedicalRecord medicalRecord;

  @OneToMany(
      mappedBy = "pet",
      cascade = {CascadeType.PERSIST, CascadeType.MERGE},
      fetch = FetchType.LAZY)
  private List<Appointment> appointments = new ArrayList<>();

  public void setMedicalRecord(MedicalRecord medicalRecord) {
    this.medicalRecord = medicalRecord;
    if (medicalRecord != null && medicalRecord.getPet() != this) {
      medicalRecord.setPet(this);
    }
  }
}
