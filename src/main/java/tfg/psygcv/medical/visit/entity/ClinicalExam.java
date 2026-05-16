package tfg.psygcv.medical.visit.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import tfg.psygcv.pet.entity.Temperament;
import tfg.psygcv.shared.entity.AuditableEntity;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "CLINICAL_EXAM")
public class ClinicalExam extends AuditableEntity {

  @Column(name = "RESPIRATORY_RATE", nullable = false)
  private Integer respiratoryRate;

  @Column(name = "HEART_RATE", nullable = false)
  private Integer heartRate;

  @Column(name = "TEMPERATURE", nullable = false)
  private Float temperature;

  @Column(name = "WEIGHT", nullable = false)
  private Float weight;

  @Column(name = "PULSE", nullable = false)
  private Integer pulse;

  @Column(name = "MUCOSAL_MEMBRANES", nullable = false, length = 100)
  private String mucosalMembranes;

  @Enumerated(EnumType.STRING)
  @Column(name = "TEMPERAMENT", nullable = false, length = 15)
  private Temperament temperament;

  @Column(name = "DESCRIPTION", columnDefinition = "TEXT")
  private String description;

  @OneToOne(mappedBy = "clinicalExam")
  private Visit visit;

  public void setVisit(Visit visit) {
    this.visit = visit;
    if (visit != null && visit.getClinicalExam() != this) {
      visit.setClinicalExam(this);
    }
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (!(o instanceof ClinicalExam other)) {
      return false;
    }
    return getId() != null && getId().equals(other.getId());
  }

  @Override
  public int hashCode() {
    return getClass().hashCode();
  }
}
