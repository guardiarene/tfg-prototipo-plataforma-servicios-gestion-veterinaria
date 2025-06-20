package tfg.psygcv.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "MEDICAL_RECORD")
public class MedicalRecord {

    @Version
    @Column(name = "VERSION")
    private Integer version;

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
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "OWNER_ID", nullable = false)
    private User owner;

    @NotNull
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "PET_ID", nullable = false, unique = true)
    private Pet pet;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "VETERINARIAN_ID", nullable = false)
    private User veterinarian;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "LAST_VETERINARIAN_ID")
    private User lastVeterinarian;

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @JoinColumn(name = "CLINICAL_EXAM_ID", unique = true)
    private ClinicalExam clinicalExam;

    @OneToOne(mappedBy = "medicalRecord", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private Anamnesis anamnesis;

    @OneToMany(mappedBy = "medicalRecord", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<Diagnostic> diagnostics = new ArrayList<>();

    @OneToMany(mappedBy = "medicalRecord", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<Treatment> treatments = new ArrayList<>();

    public void setAnamnesis(Anamnesis anamnesis) {
        this.anamnesis = anamnesis;
        if (anamnesis != null && anamnesis.getMedicalRecord() != this) {
            anamnesis.setMedicalRecord(this);
        }
    }

}
