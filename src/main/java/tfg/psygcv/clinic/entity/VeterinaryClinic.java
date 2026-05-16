package tfg.psygcv.clinic.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.util.LinkedHashSet;
import java.util.Set;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.SQLRestriction;
import tfg.psygcv.appointment.entity.Appointment;
import tfg.psygcv.shared.entity.AuditableEntity;
import tfg.psygcv.user.entity.User;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "VETERINARY_CLINIC")
public class VeterinaryClinic extends AuditableEntity {

  @Column(name = "NAME", nullable = false, length = 100)
  private String name;

  @Column(name = "ADDRESS", nullable = false, length = 255)
  private String address;

  @Column(name = "PHONE", nullable = false, length = 20)
  private String phone;

  @Column(name = "EMAIL", nullable = false, unique = true, length = 254)
  private String email;

  @OneToMany(
      mappedBy = "clinic",
      cascade = {CascadeType.PERSIST, CascadeType.MERGE},
      fetch = FetchType.LAZY)
  private Set<MedicalService> services = new LinkedHashSet<>();

  @OneToMany(
      mappedBy = "clinic",
      cascade = {CascadeType.PERSIST, CascadeType.MERGE},
      fetch = FetchType.LAZY)
  private Set<Appointment> appointments = new LinkedHashSet<>();

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "OWNER_ID", nullable = false)
  private User owner;

  @OneToMany(mappedBy = "workClinic", fetch = FetchType.LAZY)
  @SQLRestriction("ROLE = 'VETERINARIAN'")
  private Set<User> veterinarians = new LinkedHashSet<>();

  @OneToMany(mappedBy = "workClinic", fetch = FetchType.LAZY)
  @SQLRestriction("ROLE = 'RECEPTIONIST'")
  private Set<User> receptionists = new LinkedHashSet<>();

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (!(o instanceof VeterinaryClinic other)) {
      return false;
    }
    return getId() != null && getId().equals(other.getId());
  }

  @Override
  public int hashCode() {
    return getClass().hashCode();
  }
}
