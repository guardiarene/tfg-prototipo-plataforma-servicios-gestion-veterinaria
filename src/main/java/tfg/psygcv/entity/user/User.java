package tfg.psygcv.entity.user;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
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

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "APP_USER")
public class User extends AuditableEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "ID")
  private Long id;

  @NotBlank
  @Column(name = "FIRST_NAME", nullable = false, length = 50)
  private String firstName;

  @NotBlank
  @Column(name = "LAST_NAME", nullable = false, length = 50)
  private String lastName;

  @NotBlank
  @Column(name = "PHONE", nullable = false, length = 20)
  private String phone;

  @NotBlank
  @Email
  @Column(name = "EMAIL", nullable = false, unique = true, length = 254)
  private String email;

  @NotBlank
  @Column(name = "PASSWORD", nullable = false, length = 72)
  private String password;

  @NotNull
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
}
