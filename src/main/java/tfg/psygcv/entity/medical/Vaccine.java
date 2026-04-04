package tfg.psygcv.entity.medical;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import java.time.LocalDate;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;
import tfg.psygcv.entity.audit.AuditableEntity;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "VACCINE")
public class Vaccine extends AuditableEntity {

  @NotNull
  @PastOrPresent
  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
  @Column(name = "APPLICATION_DATE", nullable = false)
  private LocalDate applicationDate;

  @NotBlank
  @Column(name = "BRAND", nullable = false, length = 100)
  private String brand;

  @NotBlank
  @Column(name = "DOSE", nullable = false, length = 50)
  private String dose;

  @NotBlank
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
}
