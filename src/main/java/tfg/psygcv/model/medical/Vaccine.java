package tfg.psygcv.model.medical;

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
@Table(name = "VACCINE")
public class Vaccine {

    @Version
    @Column(name = "VERSION")
    private Integer version;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Long id;

    @NotNull
    @PastOrPresent
    @Column(name = "APPLICATION_DATE", nullable = false)
    private LocalDate applicationDate;

    @NotBlank
    @Column(name = "BRAND", nullable = false)
    private String brand;

    @NotBlank
    @Column(name = "DOSE", nullable = false)
    private String dose;

    @NotBlank
    @Column(name = "BATCH", nullable = false)
    private String batch;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ANAMNESIS_ID", nullable = false)
    private Anamnesis anamnesis;

    public void setAnamnesis(Anamnesis anamnesis) {
        this.anamnesis = anamnesis;
        if (anamnesis != null && !anamnesis.getVaccines().contains(this)) {
            anamnesis.getVaccines().add(this);
        }
    }

}
