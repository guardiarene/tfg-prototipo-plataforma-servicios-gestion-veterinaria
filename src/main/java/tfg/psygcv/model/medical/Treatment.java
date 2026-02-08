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
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "TREATMENT")
public class Treatment {

  @Version
  @Column(name = "VERSION")
  private Integer version;

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "ID")
  private Long id;

  @NotBlank
  @Column(name = "PRODUCT", nullable = false)
  private String product;

  @NotBlank
  @Column(name = "ROUTE", nullable = false)
  private String route;

  @NotBlank
  @Column(name = "FREQUENCY", nullable = false)
  private String frequency;

  @NotNull
  @PastOrPresent
  @Column(name = "START_DATE", nullable = false)
  private LocalDate startDate;

  @PastOrPresent
  @Column(name = "END_DATE")
  private LocalDate endDate;

  @NotNull
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "MEDICAL_RECORD_ID", nullable = false)
  private MedicalRecord medicalRecord;

  public void setMedicalRecord(MedicalRecord medicalRecord) {
    this.medicalRecord = medicalRecord;
    if (medicalRecord != null && !medicalRecord.getTreatments().contains(this)) {
      medicalRecord.getTreatments().add(this);
    }
  }
}
