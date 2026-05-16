package tfg.psygcv.medical.visit.entity;

import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import tfg.psygcv.shared.entity.AuditableEntity;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "DIAGNOSTIC")
public class Diagnostic extends AuditableEntity {

  @ElementCollection
  @CollectionTable(name = "DIAGNOSTIC_PROBLEMS", joinColumns = @JoinColumn(name = "DIAGNOSTIC_ID"))
  @Column(name = "PROBLEM")
  private List<String> problems = new ArrayList<>();

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "VISIT_ID", nullable = false)
  private Visit visit;

  public void setVisit(Visit visit) {
    this.visit = visit;
    if (visit != null && !visit.getDiagnostics().contains(this)) {
      visit.getDiagnostics().add(this);
    }
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (!(o instanceof Diagnostic other)) {
      return false;
    }
    return getId() != null && getId().equals(other.getId());
  }

  @Override
  public int hashCode() {
    return getClass().hashCode();
  }
}
