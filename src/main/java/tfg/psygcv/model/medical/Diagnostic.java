package tfg.psygcv.model.medical;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "DIAGNOSTIC")
public class Diagnostic {

    @Version
    @Column(name = "VERSION")
    private Integer version;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Long id;

    @ElementCollection
    @CollectionTable(name = "DIAGNOSTIC_PROBLEMS", joinColumns = @JoinColumn(name = "DIAGNOSTIC_ID"))
    @Column(name = "PROBLEM")
    private List<@NotBlank String> problems = new ArrayList<>();

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "MEDICAL_RECORD_ID", nullable = false)
    private MedicalRecord medicalRecord;

    public void setMedicalRecord(MedicalRecord medicalRecord) {
        this.medicalRecord = medicalRecord;
        if (medicalRecord != null && !medicalRecord.getDiagnostics().contains(this)) {
            medicalRecord.getDiagnostics().add(this);
        }
    }

}
