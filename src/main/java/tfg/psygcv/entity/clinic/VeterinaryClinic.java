package tfg.psygcv.entity.clinic;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
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
import org.hibernate.annotations.SQLRestriction;
import tfg.psygcv.entity.appointment.Appointment;
import tfg.psygcv.entity.audit.AuditableEntity;
import tfg.psygcv.entity.user.User;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "VETERINARY_CLINIC")
public class VeterinaryClinic extends AuditableEntity {

  @NotBlank
  @Column(name = "NAME", nullable = false, length = 100)
  private String name;

  @NotBlank
  @Column(name = "ADDRESS", nullable = false, length = 255)
  private String address;

  @NotBlank
  @Column(name = "PHONE", nullable = false, length = 20)
  private String phone;

  @NotBlank
  @Email
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

  @NotNull
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "OWNER_ID", nullable = false)
  private User owner;

  @OneToMany(mappedBy = "workClinic", fetch = FetchType.LAZY)
  @SQLRestriction("ROLE = 'VETERINARIAN'")
  private Set<User> veterinarians = new LinkedHashSet<>();

  @OneToMany(mappedBy = "workClinic", fetch = FetchType.LAZY)
  @SQLRestriction("ROLE = 'RECEPTIONIST'")
  private Set<User> receptionists = new LinkedHashSet<>();
}
