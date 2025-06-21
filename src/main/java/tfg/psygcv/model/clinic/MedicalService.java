package tfg.psygcv.model.clinic;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import tfg.psygcv.model.appointment.Appointment;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "MEDICAL_SERVICE")
public class MedicalService {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Long id;

    @NotBlank
    @Column(name = "NAME", nullable = false)
    private String name;

    @Column(name = "DESCRIPTION")
    private String description;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "CLINIC_ID", nullable = false)
    private VeterinaryClinic clinic;

    @OneToMany(mappedBy = "medicalService", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<Appointment> appointments = new ArrayList<>();

    @NotNull
    @Column(name = "ACTIVE", nullable = false)
    private Boolean active = true;

    public void setClinic(VeterinaryClinic veterinaryClinic) {
        this.clinic = veterinaryClinic;
        if (veterinaryClinic != null && !veterinaryClinic.getServices().contains(this)) {
            veterinaryClinic.getServices().add(this);
        }
    }

}
