package tfg.psygcv.model.medical;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Version;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import java.time.LocalDate;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import tfg.psygcv.model.pet.ReproductiveStatus;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "ANAMNESIS")
public class Anamnesis {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "ID")
  private Long id;

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

  @PastOrPresent
  @Column(name = "LAST_DEWORMING_DATE")
  private LocalDate lastDewormingDate;

  @PastOrPresent
  @Column(name = "LAST_HEAT_DATE")
  private LocalDate lastHeatDate;

  @PastOrPresent
  @Column(name = "LAST_BIRTH_DATE")
  private LocalDate lastBirthDate;

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

  @OneToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "VISIT_ID", unique = true)
  private Visit visit;

  public void setVisit(Visit visit) {
    this.visit = visit;
    if (visit != null && visit.getAnamnesis() != this) {
      visit.setAnamnesis(this);
    }
  }
}
