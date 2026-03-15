package tfg.psygcv.config.security;

import java.io.Serial;
import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import lombok.Getter;
import org.jspecify.annotations.NonNull;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import tfg.psygcv.model.user.Role;
import tfg.psygcv.model.user.User;

@Getter
public final class AuthenticatedUser implements UserDetails, Serializable {

  @Serial private static final long serialVersionUID = 1L;
  private final Long id;
  private final String email;
  private final String password;
  private final Role role;
  private final boolean active;
  private final List<GrantedAuthority> authorities;

  private AuthenticatedUser(
      Long id,
      String email,
      String password,
      Role role,
      boolean active,
      List<GrantedAuthority> authorities) {
    this.id = id;
    this.email = email;
    this.password = password;
    this.role = role;
    this.active = active;
    this.authorities = List.copyOf(authorities);
  }

  public static AuthenticatedUser from(User user) {
    String authority = "ROLE_" + user.getRole().name();
    return new AuthenticatedUser(
        user.getId(),
        user.getEmail(),
        user.getPassword(),
        user.getRole(),
        Boolean.TRUE.equals(user.getActive()),
        List.of(new SimpleGrantedAuthority(authority)));
  }

  @Override
  public @NonNull Collection<? extends GrantedAuthority> getAuthorities() {
    return authorities;
  }

  @Override
  public @NonNull String getUsername() {
    return email;
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
    return active;
  }
}
