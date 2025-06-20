package tfg.psygcv.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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

    @Column(name = "DESCRIPTION")
    private String description;

    @OneToOne(mappedBy = "clinicalExam", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private MedicalRecord medicalRecord;

    public void setMedicalRecord(MedicalRecord medicalRecord) {
        this.medicalRecord = medicalRecord;
        if (medicalRecord != null && medicalRecord.getClinicalExam() != this) {
            medicalRecord.setClinicalExam(this);
        }
    }

}
