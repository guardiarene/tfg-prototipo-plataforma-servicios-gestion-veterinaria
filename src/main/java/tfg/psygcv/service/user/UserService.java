package tfg.psygcv.service.user;

import java.util.List;
import tfg.psygcv.entity.user.User;

public interface UserService {

  List<User> findAll();

  List<User> findActiveCustomers();

  User findById(Long userId);

  User findByIdWithClinicContext(Long userId);

  User findByEmail(String email);

  User save(User user);

  void saveComplete(User user);

  User update(User user);

  void updateComplete(Long userId, User updatedUser);

  void updateVeterinarianProfile(User currentVeterinarian, User updatedVeterinarian);

  void deactivate(Long userId);
}
