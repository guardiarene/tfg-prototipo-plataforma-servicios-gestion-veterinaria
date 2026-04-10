package tfg.psygcv.medical.visit.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.time.LocalDate;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import tfg.psygcv.shared.entity.AuditableEntity;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "TREATMENT")
public class Treatment extends AuditableEntity {

  @Column(name = "PRODUCT", nullable = false, length = 100)
  private String product;

  @Column(name = "ROUTE", nullable = false, length = 50)
  private String route;

  @Column(name = "FREQUENCY", nullable = false, length = 50)
  private String frequency;

  @Column(name = "START_DATE", nullable = false)
  private LocalDate startDate;

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

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (!(o instanceof Treatment other)) return false;
    return getId() != null && getId().equals(other.getId());
  }

  @Override
  public int hashCode() {
    return getClass().hashCode();
  }
}
