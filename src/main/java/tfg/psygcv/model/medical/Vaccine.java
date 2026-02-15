package tfg.psygcv.model.medical;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Version;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import java.time.LocalDate;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "VACCINE")
public class Vaccine {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "ID")
  private Long id;

  @NotNull
  @PastOrPresent
  @Column(name = "APPLICATION_DATE", nullable = false)
  private LocalDate applicationDate;

  @NotBlank
  @Column(name = "BRAND", nullable = false)
  private String brand;

  @NotBlank
  @Column(name = "DOSE", nullable = false)
  private String dose;

  @NotBlank
  @Column(name = "BATCH", nullable = false)
  private String batch;

  @NotNull
  @Column(name = "CREATED_AT", nullable = false, updatable = false)
  private LocalDateTime createdAt;

  @Column(name = "UPDATED_AT")
  private LocalDateTime updatedAt;

  @NotNull
  @Column(name = "ACTIVE", nullable = false)
  private Boolean active = true;

  @Version
  @Column(name = "VERSION")
  private Integer version;

  @NotNull
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
