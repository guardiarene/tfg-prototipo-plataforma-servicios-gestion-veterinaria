package tfg.psygcv.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.PositiveOrZero;
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
@Table(name = "PET")
public class Pet {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Long id;

    @NotBlank
    @Column(name = "NAME", nullable = false)
    private String name;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "SEX", nullable = false)
    private Sex sex;

    @NotBlank
    @Column(name = "BREED", nullable = false)
    private String breed;

    @NotBlank
    @Column(name = "SPECIES", nullable = false)
    private String species;

    @NotNull
    @Past
    @Column(name = "BIRTH_DATE", nullable = false)
    private LocalDate birthDate;

    @NotNull
    @PositiveOrZero
    @Column(name = "WEIGHT", nullable = false)
    private Float weight;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "OWNER_ID", nullable = false)
    private User owner;

    @OneToOne(mappedBy = "pet", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private MedicalRecord medicalRecord;

    @OneToMany(mappedBy = "pet", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<Appointment> appointments = new ArrayList<>();

    @NotNull
    @Column(name = "ACTIVE", nullable = false)
    private Boolean active = true;

    public void setMedicalRecord(MedicalRecord record) {
        this.medicalRecord = record;
        if (record != null && record.getPet() != this) {
            record.setPet(this);
        }
    }

}
