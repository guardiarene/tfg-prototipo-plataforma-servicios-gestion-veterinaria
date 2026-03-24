package tfg.psygcv.model.clinic;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.util.LinkedHashSet;
import java.util.Set;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import tfg.psygcv.model.appointment.Appointment;
import tfg.psygcv.model.audit.AuditableEntity;
import tfg.psygcv.model.user.User;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "VETERINARY_CLINIC")
public class VeterinaryClinic extends AuditableEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "ID")
  private Long id;

  @NotBlank
  @Column(name = "NAME", nullable = false)
  private String name;

  @NotBlank
  @Column(name = "ADDRESS", nullable = false)
  private String address;

  @NotBlank
  @Column(name = "PHONE", nullable = false)
  private String phone;

  @NotBlank
  @Email
  @Column(name = "EMAIL", nullable = false)
  private String email;

  @OneToMany(
      mappedBy = "clinic",
      cascade = CascadeType.ALL,
      orphanRemoval = true,
      fetch = FetchType.LAZY)
  private Set<MedicalService> services = new LinkedHashSet<>();

  @OneToMany(
      mappedBy = "clinic",
      cascade = CascadeType.ALL,
      orphanRemoval = true,
      fetch = FetchType.LAZY)
  private Set<Appointment> appointments = new LinkedHashSet<>();

  @NotNull
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "OWNER_ID", nullable = false)
  private User owner;

  @OneToMany(mappedBy = "workClinic", fetch = FetchType.LAZY)
  private Set<User> veterinarians = new LinkedHashSet<>();

  @OneToMany(mappedBy = "workClinic", fetch = FetchType.LAZY)
  private Set<User> receptionists = new LinkedHashSet<>();

  public void setOwner(User owner) {
    this.owner = owner;
    if (owner != null) {
      owner.getClinicsOwned().add(this);
    }
  }
}
