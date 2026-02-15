package tfg.psygcv.model.user;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import tfg.psygcv.model.appointment.Appointment;
import tfg.psygcv.model.clinic.VeterinaryClinic;
import tfg.psygcv.model.pet.Pet;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(
    name = "USERS",
    uniqueConstraints = {@UniqueConstraint(columnNames = "EMAIL")})
public class User implements UserDetails {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "ID")
  private Long id;

  @NotBlank
  @Column(name = "FIRST_NAME", nullable = false)
  private String firstName;

  @NotBlank
  @Column(name = "LAST_NAME", nullable = false)
  private String lastName;

  @NotBlank
  @Column(name = "PHONE", nullable = false)
  private String phone;

  @NotBlank
  @Email
  @Column(name = "EMAIL", nullable = false, unique = true)
  private String email;

  @NotBlank
  @Column(name = "PASSWORD", nullable = false)
  private String password;

  @NotNull
  @Enumerated(EnumType.STRING)
  @Column(name = "ROLE", length = 20, nullable = false)
  private Role role;

  @OneToMany(
      mappedBy = "owner",
      cascade = CascadeType.ALL,
      orphanRemoval = true,
      fetch = FetchType.LAZY)
  private List<Pet> pets = new ArrayList<>();

  @OneToMany(
      mappedBy = "customer",
      cascade = CascadeType.ALL,
      orphanRemoval = true,
      fetch = FetchType.LAZY)
  private List<Appointment> appointmentsAsCustomer = new ArrayList<>();

  @OneToMany(mappedBy = "veterinarian", fetch = FetchType.LAZY)
  @Fetch(FetchMode.JOIN)
  private List<VeterinaryClinic> clinicsOwned = new ArrayList<>();

  @OneToMany(mappedBy = "receptionist", fetch = FetchType.LAZY)
  private List<VeterinaryClinic> clinicsAsReceptionist = new ArrayList<>();

  @NotNull
  @Column(name = "ACTIVE", nullable = false)
  private Boolean active = true;

  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    String roleName = "ROLE_" + this.role.name();
    return Collections.singletonList(new SimpleGrantedAuthority(roleName));
  }

  @Override
  public String getUsername() {
    return this.email;
  }

  @Override
  public String getPassword() {
    return this.password;
  }

  @Override
  public boolean isAccountNonExpired() {
    return true;
  }

  @Override
  public boolean isAccountNonLocked() {
    return true;
  }

  @Override
  public boolean isCredentialsNonExpired() {
    return true;
  }

  @Override
  public boolean isEnabled() {
    return Boolean.TRUE.equals(this.active);
  }
}
