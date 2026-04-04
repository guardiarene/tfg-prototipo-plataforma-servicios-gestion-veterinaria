package tfg.psygcv.repository.user;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import tfg.psygcv.entity.user.Role;
import tfg.psygcv.entity.user.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

  Optional<User> findByEmail(String email);

  @EntityGraph(attributePaths = {"clinicsOwned", "workClinic"})
  @Query("SELECT DISTINCT u FROM User u WHERE u.id = :userId")
  Optional<User> findByIdWithClinicContext(@Param("userId") Long userId);

  List<User> findByRoleAndActive(Role role, Boolean active);
}
