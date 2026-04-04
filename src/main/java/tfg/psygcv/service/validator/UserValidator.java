package tfg.psygcv.service.validator;

import org.springframework.stereotype.Component;
import tfg.psygcv.entity.user.User;

@Component
public class UserValidator extends BaseValidator {

  public void validateForCreation(User user) {
    validateNotNull(user, "User cannot be null");
    validateBasicFields(user);
    validatePassword(user.getPassword());
  }

  public void validateForUpdate(User user) {
    validateNotNull(user, "User cannot be null");
    validateId(user.getId());
    validateBasicFields(user);
  }

  public void validateForCompleteUpdate(User user) {
    validateNotNull(user, "User cannot be null");
    validateBasicFields(user);
    validateNotNull(user.getRole(), "User role cannot be null");
  }

  private void validateBasicFields(User user) {
    validateStringNotBlank(user.getFirstName(), "First name");
    validateStringNotBlank(user.getLastName(), "Last name");
    validateEmail(user.getEmail());
  }

  private void validatePassword(String password) {
    if (password == null || password.length() < 6) {
      throw new IllegalArgumentException("Password must be at least 6 characters long");
    }
  }
}
