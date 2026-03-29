package tfg.psygcv.service.interfaces;

import java.util.List;
import tfg.psygcv.model.user.User;

public interface UserServiceInterface {

  List<User> findAll();

  List<User> findActiveCustomers();

  User findById(Long userId);

  User findByEmail(String email);

  User save(User user);

  void saveComplete(User user);

  User update(User user);

  void updateComplete(Long userId, User updatedUser);

  void updateVeterinarianProfile(User currentVeterinarian, User updatedVeterinarian);

  void deactivate(Long userId);
}
