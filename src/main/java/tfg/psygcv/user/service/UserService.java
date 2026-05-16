package tfg.psygcv.user.service;

import java.util.List;
import tfg.psygcv.clinic.entity.VeterinaryClinic;
import tfg.psygcv.user.command.CreateAdminUserCommand;
import tfg.psygcv.user.command.CreateStaffCommand;
import tfg.psygcv.user.command.RegisterCustomerCommand;
import tfg.psygcv.user.command.RegisterVeterinarianCommand;
import tfg.psygcv.user.command.UpdateAdminUserCommand;
import tfg.psygcv.user.command.UpdateUserProfileCommand;
import tfg.psygcv.user.entity.User;

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

  List<User> findVeterinariansByClinicId(Long clinicId);

  List<User> findReceptionistsByClinicId(Long clinicId);

  User registerVeterinarian(RegisterVeterinarianCommand command);

  User registerStaffForClinic(CreateStaffCommand command, VeterinaryClinic clinic);
}
