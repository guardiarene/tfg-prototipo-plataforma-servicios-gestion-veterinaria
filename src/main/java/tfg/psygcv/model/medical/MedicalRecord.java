package tfg.psygcv.model.medical;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.OrderBy;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import jakarta.persistence.Version;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import tfg.psygcv.model.pet.Pet;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "MEDICAL_RECORD")
public class MedicalRecord {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "ID")
  private Long id;

  @NotNull
  @Column(name = "CREATED_AT", nullable = false, updatable = false)
  private LocalDateTime createdAt;

  @Column(name = "UPDATED_AT")
  private LocalDateTime updatedAt;

  @Column(name = "GENERAL_OBSERVATIONS", columnDefinition = "TEXT")
  private String generalObservations;

  @NotNull
  @Column(name = "ACTIVE", nullable = false)
  private Boolean active = true;

  @Version
  @Column(name = "VERSION")
  private Integer version;

  @NotNull
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
      if (visit.getAnamnesis() != null) {
        Anamnesis a = visit.getAnamnesis();

        if (current.getAllergies() == null && a.getAllergies() != null) {
          current.setAllergies(a.getAllergies());
        }

        if (current.getPreviousDiseases() == null && a.getPreviousDiseases() != null) {
          current.setPreviousDiseases(a.getPreviousDiseases());
        }

        if (current.getSurgeries() == null && a.getSurgeries() != null) {
          current.setSurgeries(a.getSurgeries());
        }

        if (current.getCurrentMedications() == null && a.getCurrentMedications() != null) {
          current.setCurrentMedications(a.getCurrentMedications());
        }

        if (current.getDiet() == null && a.getDiet() != null) {
          current.setDiet(a.getDiet());
        }

        if (current.getReproductiveStatus() == null && a.getReproductiveStatus() != null) {
          current.setReproductiveStatus(a.getReproductiveStatus());
        }

        if (current.getLastDewormingDate() == null && a.getLastDewormingDate() != null) {
          current.setLastDewormingDate(a.getLastDewormingDate());
        }

        if (current.getLastHeatDate() == null && a.getLastHeatDate() != null) {
          current.setLastHeatDate(a.getLastHeatDate());
        }

        if (current.getLastBirthDate() == null && a.getLastBirthDate() != null) {
          current.setLastBirthDate(a.getLastBirthDate());
        }
      }
    }

    return current;
  }
}
