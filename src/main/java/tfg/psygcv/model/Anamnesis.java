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
@Table(name = "ANAMNESIS")
public class Anamnesis {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Long id;

    @PastOrPresent
    @Column(name = "LAST_DEWORMING_DATE")
    private LocalDate lastDewormingDate;

    @OneToMany(mappedBy = "anamnesis", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<Vaccine> vaccines = new ArrayList<>();

    @NotBlank
    @Column(name = "DIET")
    private String diet;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "REPRODUCTIVE_STATUS", nullable = false)
    private ReproductiveStatus reproductiveStatus;

    @PastOrPresent
    @Column(name = "LAST_HEAT_DATE")
    private LocalDate lastHeatDate;

    @PastOrPresent
    @Column(name = "LAST_BIRTH_DATE")
    private LocalDate lastBirthDate;

    @NotNull
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "MEDICAL_RECORD_ID", nullable = false, unique = true)
    private MedicalRecord medicalRecord;

    public Anamnesis(LocalDate lastDewormingDate, String diet, ReproductiveStatus reproductiveStatus, LocalDate lastHeatDate, LocalDate lastBirthDate, MedicalRecord medicalRecord) {
        this.lastDewormingDate = lastDewormingDate;
        this.diet = diet;
        this.reproductiveStatus = reproductiveStatus;
        this.lastHeatDate = lastHeatDate;
        this.lastBirthDate = lastBirthDate;
        this.setMedicalRecord(medicalRecord);
    }

    public void updateFrom(Anamnesis source) {
        if (source == null) {
            return;
        }
        this.lastDewormingDate = source.getLastDewormingDate();
        this.diet = source.getDiet();
        this.reproductiveStatus = source.getReproductiveStatus();
        this.lastHeatDate = source.getLastHeatDate();
        this.lastBirthDate = source.getLastBirthDate();
    }

    public void addVaccine(Vaccine vaccine) {
        if (vaccine != null) {
            vaccines.add(vaccine);
            vaccine.setAnamnesis(this);
        }
    }

    public void removeVaccine(Vaccine vaccine) {
        if ((vaccine != null) && (vaccines.remove(vaccine))) {
            vaccine.setAnamnesis(null);
        }
    }

    public void setMedicalRecord(MedicalRecord medicalRecord) {
        this.medicalRecord = medicalRecord;
        if ((medicalRecord != null) && (medicalRecord.getAnamnesis() != this)) {
            medicalRecord.setAnamnesis(this);
        }
    }

}
