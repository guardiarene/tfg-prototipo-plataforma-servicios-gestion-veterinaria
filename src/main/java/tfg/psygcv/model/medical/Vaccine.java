package tfg.psygcv.model.medical;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Version;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import java.time.LocalDate;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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
