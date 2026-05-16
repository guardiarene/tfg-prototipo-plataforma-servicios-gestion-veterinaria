package tfg.psygcv.medical.record.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.OrderBy;
import jakarta.persistence.Table;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import tfg.psygcv.medical.visit.entity.Vaccine;
import tfg.psygcv.medical.visit.entity.Visit;
import tfg.psygcv.pet.entity.Pet;
import tfg.psygcv.shared.entity.AuditableEntity;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "MEDICAL_RECORD")
public class MedicalRecord extends AuditableEntity {

  @Column(name = "GENERAL_OBSERVATIONS", columnDefinition = "TEXT")
  private String generalObservations;

  @OneToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "PET_ID", nullable = false, unique = true)
  private Pet pet;

  @OneToMany(mappedBy = "medicalRecord", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
  @OrderBy("date DESC, createdAt DESC")
  private List<Visit> visits = new ArrayList<>();

  @OneToMany(mappedBy = "medicalRecord", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
  @OrderBy("applicationDate DESC")
  private List<Vaccine> vaccines = new ArrayList<>();

  public void setPet(Pet pet) {
    this.pet = pet;
    if (pet != null && pet.getMedicalRecord() != this) {
      pet.setMedicalRecord(this);
    }
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (!(o instanceof MedicalRecord other)) {
      return false;
    }
    return getId() != null && getId().equals(other.getId());
  }

  @Override
  public int hashCode() {
    return getClass().hashCode();
  }
}
