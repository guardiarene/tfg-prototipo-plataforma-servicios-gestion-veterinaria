package tfg.psygcv.service.interfaces;

import java.util.List;
import tfg.psygcv.model.user.User;

public interface UserServiceInterface {

  List<User> findAll();

  List<User> findActiveCustomers();

  User findById(Long userId);

  User findByEmail(String email);

  User save(User user);

  User saveComplete(User user);

  User update(User user);

  User updateComplete(Long userId, User updatedUser);

  void deactivate(Long userId);
}
