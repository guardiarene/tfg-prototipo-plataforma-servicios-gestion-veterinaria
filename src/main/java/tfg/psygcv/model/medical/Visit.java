package tfg.psygcv.model.medical;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Version;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import tfg.psygcv.model.appointment.Appointment;
import tfg.psygcv.model.user.User;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "VISIT")
public class Visit {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "ID")
  private Long id;

  @NotNull
  @PastOrPresent
  @Column(name = "DATE", nullable = false)
  private LocalDate date;

  @NotBlank
  @Column(name = "REASON_FOR_VISIT", nullable = false)
  private String reasonForVisit;

  @NotNull
  @Enumerated(EnumType.STRING)
  @Column(name = "VISIT_TYPE", nullable = false, length = 30)
  private VisitType visitType;

  @Column(name = "OBSERVATIONS", columnDefinition = "TEXT")
  private String observations;

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

  @NotNull
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "VETERINARIAN_ID", nullable = false)
  private User veterinarian;

  @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
  @JoinColumn(name = "CLINICAL_EXAM_ID", unique = true)
  private ClinicalExam clinicalExam;

  @OneToOne(mappedBy = "visit", cascade = CascadeType.ALL, orphanRemoval = true)
  private Anamnesis anamnesis;

  @OneToMany(
      mappedBy = "visit",
      cascade = CascadeType.ALL,
      orphanRemoval = true,
      fetch = FetchType.LAZY)
  private List<Diagnostic> diagnostics = new ArrayList<>();

  @OneToMany(
      mappedBy = "visit",
      cascade = CascadeType.ALL,
      orphanRemoval = true,
      fetch = FetchType.LAZY)
  private List<Treatment> treatments = new ArrayList<>();

  @OneToMany(mappedBy = "visit", fetch = FetchType.LAZY)
  private List<Vaccine> vaccines = new ArrayList<>();

  @OneToOne(mappedBy = "visit")
  private Appointment appointment;

  public void setMedicalRecord(MedicalRecord medicalRecord) {
    this.medicalRecord = medicalRecord;
    if (medicalRecord != null && !medicalRecord.getVisits().contains(this)) {
      medicalRecord.getVisits().add(this);
    }
  }

  public void setClinicalExam(ClinicalExam clinicalExam) {
    this.clinicalExam = clinicalExam;
    if (clinicalExam != null && clinicalExam.getVisit() != this) {
      clinicalExam.setVisit(this);
    }
  }

  public void setAnamnesis(Anamnesis anamnesis) {
    this.anamnesis = anamnesis;
    if (anamnesis != null && anamnesis.getVisit() != this) {
      anamnesis.setVisit(this);
    }
  }
}
