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

  User registerCustomer(RegisterCustomerCommand command);

  void saveComplete(CreateAdminUserCommand command);

  void updateComplete(Long userId, UpdateAdminUserCommand command);

  void updateVeterinarianProfile(Long veterinarianId, UpdateUserProfileCommand command);

  void deactivate(Long userId);

  List<User> findActiveByWorkClinicId(Long clinicId);
}
