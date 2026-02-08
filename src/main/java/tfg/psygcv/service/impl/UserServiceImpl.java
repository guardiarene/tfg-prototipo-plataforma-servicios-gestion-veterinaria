package tfg.psygcv.service.impl;

import jakarta.persistence.EntityNotFoundException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tfg.psygcv.model.user.Role;
import tfg.psygcv.model.user.User;
import tfg.psygcv.repository.base.UserRepository;
import tfg.psygcv.service.interfaces.UserServiceInterface;
import tfg.psygcv.service.validator.UserValidator;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class UserServiceImpl implements UserDetailsService, UserServiceInterface {

  private final UserRepository userRepository;

  private final PasswordEncoder passwordEncoder;

  private final UserValidator userValidator;

  @Override
  @Transactional(readOnly = true)
  public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
    User user = findByEmail(email);
    if (user == null) {
      throw new UsernameNotFoundException("User not found with email: " + email);
    }
    return user;
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
  public User findByEmail(String email) {
    userValidator.validateEmail(email);
    return userRepository
        .findByEmail(email)
        .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + email));
  }

  @Override
  @Transactional
  public User save(User user) {
    userValidator.validateForCreation(user);
    encodePassword(user);
    return userRepository.save(user);
  }

  @Override
  @Transactional
  public User saveComplete(User user) {
    if (user.getActive() == null) {
      user.setActive(true);
    }
    return save(user);
  }

  @Override
  @Transactional
  public User update(User user) {
    userValidator.validateForUpdate(user);
    User existingUser = findById(user.getId());
    updateBasicUserFields(existingUser, user);
    return userRepository.save(existingUser);
  }

  @Override
  @Transactional
  public User updateComplete(Long userId, User updatedUser) {
    userValidator.validateId(userId);
    userValidator.validateForCompleteUpdate(updatedUser);
    User existingUser = findById(userId);
    updateAllUserFields(existingUser, updatedUser);
    if (shouldUpdatePassword(updatedUser)) {
      encodePassword(existingUser, updatedUser.getPassword());
    }
    return userRepository.save(existingUser);
  }

  @Override
  @Transactional
  public void deactivate(Long userId) {
    User user = findById(userId);
    user.setActive(false);
    userRepository.save(user);
  }

  private void encodePassword(User user) {
    user.setPassword(passwordEncoder.encode(user.getPassword()));
  }

  private void encodePassword(User user, String newPassword) {
    user.setPassword(passwordEncoder.encode(newPassword));
  }

  private boolean shouldUpdatePassword(User user) {
    return user.getPassword() != null && !user.getPassword().isEmpty();
  }

  private void updateBasicUserFields(User existing, User updated) {
    existing.setFirstName(updated.getFirstName());
    existing.setLastName(updated.getLastName());
    existing.setPhone(updated.getPhone());
    existing.setEmail(updated.getEmail());
  }

  private void updateAllUserFields(User existing, User updated) {
    updateBasicUserFields(existing, updated);
    existing.setRole(updated.getRole());
    existing.setActive(updated.getActive());
  }
}
