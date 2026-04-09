package tfg.psygcv.entity.user;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import tfg.psygcv.entity.appointment.Appointment;
import tfg.psygcv.entity.audit.AuditableEntity;
import tfg.psygcv.entity.clinic.VeterinaryClinic;
import tfg.psygcv.entity.pet.Pet;
import tfg.psygcv.enums.user.Role;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "APP_USER")
public class User extends AuditableEntity {

  @Column(name = "FIRST_NAME", nullable = false, length = 50)
  private String firstName;

  @Column(name = "LAST_NAME", nullable = false, length = 50)
  private String lastName;

  @Column(name = "PHONE", nullable = false, length = 20)
  private String phone;

  @Column(name = "EMAIL", nullable = false, unique = true, length = 254)
  private String email;

  @Column(name = "PASSWORD", nullable = false, length = 72)
  private String password;

  @Enumerated(EnumType.STRING)
  @Column(name = "ROLE", length = 25, nullable = false)
  private Role role;

  @OneToMany(
      mappedBy = "owner",
      cascade = {CascadeType.PERSIST, CascadeType.MERGE},
      fetch = FetchType.LAZY)
  private List<Pet> pets = new ArrayList<>();

  @OneToMany(
      mappedBy = "customer",
      cascade = {CascadeType.PERSIST, CascadeType.MERGE},
      fetch = FetchType.LAZY)
  private List<Appointment> appointmentsAsCustomer = new ArrayList<>();

  @OneToMany(mappedBy = "owner", fetch = FetchType.LAZY)
  private Set<VeterinaryClinic> clinicsOwned = new LinkedHashSet<>();

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "WORK_CLINIC_ID")
  private VeterinaryClinic workClinic;

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (!(o instanceof User other)) return false;
    return getId() != null && getId().equals(other.getId());
  }

  @Override
  public int hashCode() {
    return getClass().hashCode();
  }
}
