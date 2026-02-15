package tfg.psygcv.model.medical;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Version;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import tfg.psygcv.model.pet.Temperament;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "CLINICAL_EXAM")
public class ClinicalExam {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "ID")
  private Long id;

  @NotNull
  @PositiveOrZero
  @Column(name = "RESPIRATORY_RATE", nullable = false)
  private Integer respiratoryRate;

  @NotNull
  @PositiveOrZero
  @Column(name = "HEART_RATE", nullable = false)
  private Integer heartRate;

  @NotNull
  @Column(name = "TEMPERATURE", nullable = false)
  private Float temperature;

  @NotNull
  @PositiveOrZero
  @Column(name = "PULSE", nullable = false)
  private Integer pulse;

  @NotBlank
  @Column(name = "MUCOSAL_MEMBRANES", nullable = false)
  private String mucosalMembranes;

  @NotNull
  @Enumerated(EnumType.STRING)
  @Column(name = "TEMPERAMENT", nullable = false)
  private Temperament temperament;

  @Column(name = "DESCRIPTION", columnDefinition = "TEXT")
  private String description;

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

  @OneToOne(mappedBy = "clinicalExam")
  private Visit visit;

  public void setVisit(Visit visit) {
    this.visit = visit;
    if (visit != null && visit.getClinicalExam() != this) {
      visit.setClinicalExam(this);
    }
  }
}
