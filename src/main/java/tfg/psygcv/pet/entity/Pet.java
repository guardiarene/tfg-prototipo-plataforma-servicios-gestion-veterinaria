package tfg.psygcv.pet.entity;

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
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import tfg.psygcv.appointment.entity.Appointment;
import tfg.psygcv.medical.record.entity.MedicalRecord;
import tfg.psygcv.shared.entity.AuditableEntity;
import tfg.psygcv.user.entity.User;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "PET")
public class Pet extends AuditableEntity {

  @Column(name = "NAME", nullable = false, length = 50)
  private String name;

  @Enumerated(EnumType.STRING)
  @Column(name = "SEX", nullable = false, length = 10)
  private Sex sex;

  @Enumerated(EnumType.STRING)
  @Column(name = "BREED", nullable = false, length = 30)
  private Breed breed;

  @Enumerated(EnumType.STRING)
  @Column(name = "SPECIES", nullable = false, length = 15)
  private Species species;

  @Column(name = "BIRTH_DATE", nullable = false)
  private LocalDate birthDate;

  @Column(name = "WEIGHT", nullable = false)
  private Float weight;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "OWNER_ID", nullable = false)
  private User owner;

  @OneToOne(mappedBy = "pet", cascade = CascadeType.ALL)
  private MedicalRecord medicalRecord;

  @OneToMany(mappedBy = "pet", cascade = {CascadeType.PERSIST,
      CascadeType.MERGE}, fetch = FetchType.LAZY)
  private List<Appointment> appointments = new ArrayList<>();

  public void setMedicalRecord(MedicalRecord medicalRecord) {
    this.medicalRecord = medicalRecord;
    if (medicalRecord != null && medicalRecord.getPet() != this) {
      medicalRecord.setPet(this);
    }
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (!(o instanceof Pet other)) {
      return false;
    }
    return getId() != null && getId().equals(other.getId());
  }

  @Override
  public int hashCode() {
    return getClass().hashCode();
  }
}
