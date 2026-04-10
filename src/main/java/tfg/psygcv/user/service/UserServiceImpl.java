package tfg.psygcv.user.service;

import jakarta.persistence.EntityNotFoundException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.NonNull;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tfg.psygcv.clinic.entity.VeterinaryClinic;
import tfg.psygcv.config.security.AuthenticatedUser;
import tfg.psygcv.user.command.CreateAdminUserCommand;
import tfg.psygcv.user.command.CreateStaffCommand;
import tfg.psygcv.user.command.RegisterCustomerCommand;
import tfg.psygcv.user.command.RegisterVeterinarianCommand;
import tfg.psygcv.user.command.UpdateAdminUserCommand;
import tfg.psygcv.user.command.UpdateUserProfileCommand;
import tfg.psygcv.user.entity.Role;
import tfg.psygcv.user.entity.User;
import tfg.psygcv.user.repository.UserRepository;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class UserServiceImpl implements UserDetailsService, UserService {

  private final UserRepository userRepository;
  private final PasswordEncoder passwordEncoder;
  private final UserValidator userValidator;

  @Override
  @Transactional(readOnly = true)
  public @NonNull UserDetails loadUserByUsername(@NonNull String email)
      throws UsernameNotFoundException {
    User user = findByEmail(email);
    if (user == null) {
      throw new UsernameNotFoundException("User not found with email: " + email);
    }
    return AuthenticatedUser.from(user);
  }

  @Override
  public List<User> findAll() {
    return userRepository.findAll();
  }

  @Override
  public List<User> findActiveCustomers() {
    return userRepository.findByRoleAndActive(Role.CUSTOMER, true);
  }

  @Override
  public User findById(Long userId) {
    userValidator.validateId(userId);
    return userRepository
        .findById(userId)
        .orElseThrow(() -> new EntityNotFoundException("User not found with ID: " + userId));
  }

  @Override
  public User findByIdWithClinicContext(Long userId) {
    userValidator.validateId(userId);
    return userRepository
        .findByIdWithClinicContext(userId)
        .orElseThrow(() -> new EntityNotFoundException("User not found with ID: " + userId));
  }

  @Override
  public User findByEmail(String email) {
    userValidator.validateEmail(email);
    return userRepository
        .findByEmailAndActiveTrue(email)
        .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + email));
  }

  @Override
  @Transactional
  public User save(User user) {
    userValidator.validateForCreation(user);
    if (userRepository.findByEmailAndActiveTrue(user.getEmail()).isPresent()) {
      throw new IllegalArgumentException("Ya existe un usuario con este email: " + user.getEmail());
    }
    encodePassword(user);
    return userRepository.save(user);
  }

  @Override
  @Transactional
  public User registerCustomer(RegisterCustomerCommand command) {
    User user = new User();
    user.setFirstName(command.getFirstName());
    user.setLastName(command.getLastName());
    user.setEmail(command.getEmail());
    user.setPassword(command.getPassword());
    user.setPhone(command.getPhone());
    user.setRole(Role.CUSTOMER);
    user.setActive(true);
    return save(user);
  }

  @Override
  @Transactional
  public void saveComplete(CreateAdminUserCommand command) {
    User user = new User();
    user.setFirstName(command.getFirstName());
    user.setLastName(command.getLastName());
    user.setEmail(command.getEmail());
    user.setPassword(command.getPassword());
    user.setPhone(command.getPhone());
    user.setRole(command.getRole());
    user.setActive(true);
    save(user);
  }

  @Override
  @Transactional
  public void updateComplete(Long userId, UpdateAdminUserCommand command) {
    userValidator.validateId(userId);
    userValidator.validateForCompleteUpdate(command);
    User existingUser = findById(userId);
    existingUser.setFirstName(command.getFirstName());
    existingUser.setLastName(command.getLastName());
    existingUser.setPhone(command.getPhone());
    existingUser.setEmail(command.getEmail());
    existingUser.setRole(command.getRole());
    existingUser.setActive(command.getActive());
    if (command.getPassword() != null && !command.getPassword().isEmpty()) {
      encodePassword(existingUser, command.getPassword());
    }
    userRepository.save(existingUser);
  }

  @Override
  @Transactional
  public void updateVeterinarianProfile(Long veterinarianId, UpdateUserProfileCommand command) {
    userValidator.validateId(veterinarianId);
    User user = findById(veterinarianId);
    user.setFirstName(command.getFirstName());
    user.setLastName(command.getLastName());
    user.setEmail(command.getEmail());
    user.setPhone(command.getPhone());
    userValidator.validateForUpdate(user);
    userRepository.save(user);
  }

  @Override
  @Transactional
  public void deactivate(Long userId) {
    User user = findById(userId);
    user.setActive(false);
    userRepository.save(user);
  }

  @Override
  public List<User> findActiveByWorkClinicId(Long clinicId) {
    return userRepository.findByWorkClinicId(clinicId);
  }

  @Override
  public List<User> findVeterinariansByClinicId(Long clinicId) {
    return userRepository.findByWorkClinicIdAndRoleAndActiveTrue(clinicId, Role.VETERINARIAN);
  }

  @Override
  public List<User> findReceptionistsByClinicId(Long clinicId) {
    return userRepository.findByWorkClinicIdAndRoleAndActiveTrue(clinicId, Role.RECEPTIONIST);
  }

  @Override
  @Transactional
  public User registerVeterinarian(RegisterVeterinarianCommand command) {
    User user = new User();
    user.setFirstName(command.getFirstName());
    user.setLastName(command.getLastName());
    user.setEmail(command.getEmail());
    user.setPassword(command.getPassword());
    user.setPhone(command.getPhone());
    user.setRole(Role.VETERINARIAN);
    user.setActive(true);
    return save(user);
  }

  @Override
  @Transactional
  public User registerStaffForClinic(CreateStaffCommand command, VeterinaryClinic clinic) {
    User user = new User();
    user.setFirstName(command.getFirstName());
    user.setLastName(command.getLastName());
    user.setEmail(command.getEmail());
    user.setPassword(command.getPassword());
    user.setPhone(command.getPhone());
    user.setRole(command.getRole());
    user.setActive(true);
    user.setWorkClinic(clinic);
    return save(user);
  }

  private void encodePassword(User user) {
    user.setPassword(passwordEncoder.encode(user.getPassword()));
  }

  private void encodePassword(User user, String newPassword) {
    user.setPassword(passwordEncoder.encode(newPassword));
  }
}
