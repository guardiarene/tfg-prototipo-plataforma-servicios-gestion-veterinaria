package tfg.psygcv.entity.medical;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.OrderBy;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import tfg.psygcv.entity.audit.AuditableEntity;
import tfg.psygcv.entity.pet.Pet;

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

  @OneToMany(
      mappedBy = "medicalRecord",
      cascade = CascadeType.ALL,
      orphanRemoval = true,
      fetch = FetchType.LAZY)
  @OrderBy("date DESC, createdAt DESC")
  private List<Visit> visits = new ArrayList<>();

  @OneToMany(
      mappedBy = "medicalRecord",
      cascade = CascadeType.ALL,
      orphanRemoval = true,
      fetch = FetchType.LAZY)
  @OrderBy("applicationDate DESC")
  private List<Vaccine> vaccines = new ArrayList<>();

  public void setPet(Pet pet) {
    this.pet = pet;
    if (pet != null && pet.getMedicalRecord() != this) {
      pet.setMedicalRecord(this);
    }
  }

  @Transient
  public Anamnesis getCurrentAnamnesis() {
    Anamnesis current = new Anamnesis();
    for (Visit visit : visits) {
      Anamnesis source = visit.getAnamnesis();
      if (source != null) {
        mergeAnamnesis(current, source);
      }
    }
    return current;
  }

  private void mergeAnamnesis(Anamnesis current, Anamnesis source) {
    mergeTextFields(current, source);
    mergeReproductiveField(current, source);
    mergeDateFields(current, source);
  }

  private void mergeTextFields(Anamnesis current, Anamnesis source) {
    if (current.getAllergies() == null && source.getAllergies() != null) {
      current.setAllergies(source.getAllergies());
    }
    if (current.getPreviousDiseases() == null && source.getPreviousDiseases() != null) {
      current.setPreviousDiseases(source.getPreviousDiseases());
    }
    if (current.getSurgeries() == null && source.getSurgeries() != null) {
      current.setSurgeries(source.getSurgeries());
    }
    if (current.getCurrentMedications() == null && source.getCurrentMedications() != null) {
      current.setCurrentMedications(source.getCurrentMedications());
    }
    if (current.getDiet() == null && source.getDiet() != null) {
      current.setDiet(source.getDiet());
    }
  }

  private void mergeReproductiveField(Anamnesis current, Anamnesis source) {
    if (current.getReproductiveStatus() == null && source.getReproductiveStatus() != null) {
      current.setReproductiveStatus(source.getReproductiveStatus());
    }
  }

  private void mergeDateFields(Anamnesis current, Anamnesis source) {
    if (current.getLastDewormingDate() == null && source.getLastDewormingDate() != null) {
      current.setLastDewormingDate(source.getLastDewormingDate());
    }
    if (current.getLastHeatDate() == null && source.getLastHeatDate() != null) {
      current.setLastHeatDate(source.getLastHeatDate());
    }
    if (current.getLastBirthDate() == null && source.getLastBirthDate() != null) {
      current.setLastBirthDate(source.getLastBirthDate());
    }
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (!(o instanceof MedicalRecord other)) return false;
    return getId() != null && getId().equals(other.getId());
  }

  @Override
  public int hashCode() {
    return getClass().hashCode();
  }
}
