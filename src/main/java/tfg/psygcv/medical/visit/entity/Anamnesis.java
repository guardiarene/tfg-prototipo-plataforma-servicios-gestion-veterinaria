package tfg.psygcv.medical.visit.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import java.time.LocalDate;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import tfg.psygcv.pet.entity.ReproductiveStatus;
import tfg.psygcv.shared.entity.AuditableEntity;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "ANAMNESIS")
public class Anamnesis extends AuditableEntity {

  @Column(name = "ALLERGIES", columnDefinition = "TEXT")
  private String allergies;

  @Column(name = "PREVIOUS_DISEASES", columnDefinition = "TEXT")
  private String previousDiseases;

  @Column(name = "SURGERIES", columnDefinition = "TEXT")
  private String surgeries;

  @Column(name = "CURRENT_MEDICATIONS", columnDefinition = "TEXT")
  private String currentMedications;

  @Column(name = "DIET", columnDefinition = "TEXT")
  private String diet;

  @Enumerated(EnumType.STRING)
  @Column(name = "REPRODUCTIVE_STATUS", length = 30)
  private ReproductiveStatus reproductiveStatus;

  @Column(name = "LAST_DEWORMING_DATE")
  private LocalDate lastDewormingDate;

  @Column(name = "LAST_HEAT_DATE")
  private LocalDate lastHeatDate;

  @Column(name = "LAST_BIRTH_DATE")
  private LocalDate lastBirthDate;

  @OneToOne(mappedBy = "anamnesis")
  private Visit visit;

  public void setVisit(Visit visit) {
    this.visit = visit;
    if (visit != null && visit.getAnamnesis() != this) {
      visit.setAnamnesis(this);
    }
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (!(o instanceof Anamnesis other)) return false;
    return getId() != null && getId().equals(other.getId());
  }

  @Override
  public int hashCode() {
    return getClass().hashCode();
  }
}
