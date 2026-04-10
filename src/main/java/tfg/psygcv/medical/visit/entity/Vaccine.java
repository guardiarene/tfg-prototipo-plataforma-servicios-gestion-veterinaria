package tfg.psygcv.medical.visit.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.time.LocalDate;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import tfg.psygcv.medical.record.entity.MedicalRecord;
import tfg.psygcv.shared.entity.AuditableEntity;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "VACCINE")
public class Vaccine extends AuditableEntity {

  @Column(name = "APPLICATION_DATE", nullable = false)
  private LocalDate applicationDate;

  @Column(name = "BRAND", nullable = false, length = 100)
  private String brand;

  @Column(name = "DOSE", nullable = false, length = 50)
  private String dose;

  @Column(name = "BATCH", nullable = false, length = 50)
  private String batch;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "MEDICAL_RECORD_ID", nullable = false)
  private MedicalRecord medicalRecord;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "VISIT_ID")
  private Visit visit;

  public void setMedicalRecord(MedicalRecord medicalRecord) {
    this.medicalRecord = medicalRecord;
    if (medicalRecord != null && !medicalRecord.getVaccines().contains(this)) {
      medicalRecord.getVaccines().add(this);
    }
  }

  public void setVisit(Visit visit) {
    this.visit = visit;
    if (visit != null && !visit.getVaccines().contains(this)) {
      visit.getVaccines().add(this);
    }
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (!(o instanceof Vaccine other)) return false;
    return getId() != null && getId().equals(other.getId());
  }

  @Override
  public int hashCode() {
    return getClass().hashCode();
  }
}
