package tfg.psygcv.clinic.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import tfg.psygcv.appointment.entity.Appointment;
import tfg.psygcv.shared.entity.AuditableEntity;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "MEDICAL_SERVICE")
public class MedicalService extends AuditableEntity {

  @Column(name = "NAME", nullable = false, length = 100)
  private String name;

  @Column(name = "DESCRIPTION", columnDefinition = "TEXT")
  private String description;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "CLINIC_ID", nullable = false)
  private VeterinaryClinic clinic;

  @OneToMany(
      mappedBy = "medicalService",
      cascade = {CascadeType.PERSIST, CascadeType.MERGE},
      fetch = FetchType.LAZY)
  private List<Appointment> appointments = new ArrayList<>();

  public void setClinic(VeterinaryClinic veterinaryClinic) {
    this.clinic = veterinaryClinic;
    if (veterinaryClinic != null) {
      veterinaryClinic.getServices().add(this);
    }
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (!(o instanceof MedicalService other)) return false;
    return getId() != null && getId().equals(other.getId());
  }

  @Override
  public int hashCode() {
    return getClass().hashCode();
  }
}
