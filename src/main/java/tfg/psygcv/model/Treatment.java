package tfg.psygcv.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

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

    public Treatment(String product, String route, String frequency, LocalDate startDate, LocalDate endDate, MedicalRecord medicalRecord) {
        this.product = product;
        this.route = route;
        this.frequency = frequency;
        this.startDate = startDate;
        this.endDate = endDate;
        setMedicalRecord(medicalRecord);
    }

    public void setMedicalRecord(MedicalRecord medicalRecord) {
        this.medicalRecord = medicalRecord;
        if ((medicalRecord != null) && (!medicalRecord.getTreatments().contains(this))) {
            medicalRecord.getTreatments().add(this);
        }
    }

    public void updateFrom(Treatment source) {
        if (source == null) {
            return;
        }
        this.product = source.getProduct();
        this.route = source.getRoute();
        this.frequency = source.getFrequency();
        this.startDate = source.getStartDate();
        this.endDate = source.getEndDate();
    }

    @Transient
    public boolean isDateRangeValid() {
        if ((startDate == null) || (endDate == null)) {
            return true;
        }
        return !endDate.isBefore(startDate);
    }

}
