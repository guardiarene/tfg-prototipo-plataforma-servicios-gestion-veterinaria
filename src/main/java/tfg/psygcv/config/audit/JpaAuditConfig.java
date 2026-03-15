package tfg.psygcv.config.audit;

import java.util.Optional;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

@Configuration
@EnableJpaAuditing(auditorAwareRef = "auditorProvider")
public class JpaAuditConfig {

  private static final String SYSTEM_AUDITOR = "system";

  @Bean
  public AuditorAware<String> auditorProvider() {
    return () -> Optional.of(resolveCurrentAuditor());
  }

  private String resolveCurrentAuditor() {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    if (authentication == null
        || !authentication.isAuthenticated()
        || authentication instanceof AnonymousAuthenticationToken) {
      return SYSTEM_AUDITOR;
    }
    Object principal = authentication.getPrincipal();
    if (principal instanceof UserDetails userDetails) {
      String username = userDetails.getUsername();
      return !username.isBlank() ? username : SYSTEM_AUDITOR;
    }
    if (principal instanceof String username) {
      if (!username.isBlank() && !"anonymousUser".equalsIgnoreCase(username)) {
        return username;
      }
    }
    return SYSTEM_AUDITOR;
  }
}
