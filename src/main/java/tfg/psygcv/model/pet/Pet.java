package tfg.psygcv.model.pet;

import jakarta.persistence.CascadeType;
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
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.PositiveOrZero;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import tfg.psygcv.model.appointment.Appointment;
import tfg.psygcv.model.medical.MedicalRecord;
import tfg.psygcv.model.user.User;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "PET")
public class Pet {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "ID")
  private Long id;

  @NotBlank
  @Column(name = "NAME", nullable = false)
  private String name;

  @NotNull
  @Enumerated(EnumType.STRING)
  @Column(name = "SEX", nullable = false)
  private Sex sex;

  @NotBlank
  @Column(name = "BREED", nullable = false)
  private String breed;

  @NotBlank
  @Column(name = "SPECIES", nullable = false)
  private String species;

  @NotNull
  @Past
  @Column(name = "BIRTH_DATE", nullable = false)
  private LocalDate birthDate;

  @NotNull
  @PositiveOrZero
  @Column(name = "WEIGHT", nullable = false)
  private Float weight;

  @NotNull
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "OWNER_ID", nullable = false)
  private User owner;

  @OneToOne(mappedBy = "pet", cascade = CascadeType.ALL)
  private MedicalRecord medicalRecord;

  @OneToMany(
      mappedBy = "pet",
      cascade = CascadeType.ALL,
      orphanRemoval = true,
      fetch = FetchType.LAZY)
  private List<Appointment> appointments = new ArrayList<>();

  @NotNull
  @Column(name = "ACTIVE", nullable = false)
  private Boolean active = true;

  public void setMedicalRecord(MedicalRecord record) {
    this.medicalRecord = record;
    if (record != null && record.getPet() != this) {
      record.setPet(this);
    }
  }
}
