package tfg.psygcv.model.medical;

import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
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
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "DIAGNOSTIC")
public class Diagnostic {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "ID")
  private Long id;

  @ElementCollection
  @CollectionTable(name = "DIAGNOSTIC_PROBLEMS", joinColumns = @JoinColumn(name = "DIAGNOSTIC_ID"))
  @Column(name = "PROBLEM")
  private List<@NotBlank String> problems = new ArrayList<>();

  @NotNull
  @Column(name = "CREATED_AT", nullable = false, updatable = false)
  private LocalDateTime createdAt;

  @Column(name = "UPDATED_AT")
  private LocalDateTime updatedAt;

  @NotNull
  @Column(name = "ACTIVE", nullable = false)
  private Boolean active = true;

  @Version
  @Column(name = "VERSION")
  private Integer version;

  @NotNull
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "VISIT_ID", nullable = false)
  private Visit visit;

  public void setVisit(Visit visit) {
    this.visit = visit;
    if (visit != null && !visit.getDiagnostics().contains(this)) {
      visit.getDiagnostics().add(this);
    }
  }
}
