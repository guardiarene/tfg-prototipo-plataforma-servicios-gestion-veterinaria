package tfg.psygcv.entity.medical;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import tfg.psygcv.entity.appointment.Appointment;
import tfg.psygcv.entity.audit.AuditableEntity;
import tfg.psygcv.entity.user.User;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "VISIT")
public class Visit extends AuditableEntity {

  @Column(name = "DATE", nullable = false)
  private LocalDate date;

  @Column(name = "REASON_FOR_VISIT", nullable = false, length = 255)
  private String reasonForVisit;

  @Enumerated(EnumType.STRING)
  @Column(name = "VISIT_TYPE", nullable = false, length = 30)
  private VisitType visitType;

  @Column(name = "OBSERVATIONS", columnDefinition = "TEXT")
  private String observations;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "MEDICAL_RECORD_ID", nullable = false)
  private MedicalRecord medicalRecord;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "VETERINARIAN_ID", nullable = false)
  private User veterinarian;

  @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
  @JoinColumn(name = "CLINICAL_EXAM_ID", unique = true)
  private ClinicalExam clinicalExam;

  @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
  @JoinColumn(name = "ANAMNESIS_ID", unique = true)
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

  @OneToMany(
      mappedBy = "visit",
      cascade = CascadeType.ALL,
      orphanRemoval = true,
      fetch = FetchType.LAZY)
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
