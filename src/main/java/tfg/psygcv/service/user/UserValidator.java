package tfg.psygcv.service.user;

import org.springframework.stereotype.Component;
import tfg.psygcv.entity.user.User;
import tfg.psygcv.service.validation.BaseValidator;

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

  public void validateForCompleteUpdate(UpdateAdminUserCommand command) {
    validateNotNull(command, "Update command cannot be null");
    validateStringNotBlank(command.getFirstName(), "First name");
    validateStringNotBlank(command.getLastName(), "Last name");
    validateEmail(command.getEmail());
    validateNotNull(command.getRole(), "User role cannot be null");
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
