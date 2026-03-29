package tfg.psygcv.entity.medical;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import java.time.LocalDate;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;
import tfg.psygcv.entity.audit.AuditableEntity;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "TREATMENT")
public class Treatment extends AuditableEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "ID")
  private Long id;

  @NotBlank
  @Column(name = "PRODUCT", nullable = false, length = 100)
  private String product;

  @NotBlank
  @Column(name = "ROUTE", nullable = false, length = 50)
  private String route;

  @NotBlank
  @Column(name = "FREQUENCY", nullable = false, length = 50)
  private String frequency;

  @NotNull
  @PastOrPresent
  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
  @Column(name = "START_DATE", nullable = false)
  private LocalDate startDate;

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
  @Column(name = "END_DATE")
  private LocalDate endDate;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "VISIT_ID", nullable = false)
  private Visit visit;

  public void setVisit(Visit visit) {
    this.visit = visit;
    if (visit != null && !visit.getTreatments().contains(this)) {
      visit.getTreatments().add(this);
    }
  }
}
