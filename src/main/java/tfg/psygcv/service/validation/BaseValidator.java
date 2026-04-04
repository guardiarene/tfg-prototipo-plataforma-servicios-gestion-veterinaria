package tfg.psygcv.service.validation;

public abstract class BaseValidator {

  public void validateId(Long id) {
    if (id == null || id <= 0) {
      throw new IllegalArgumentException("Invalid ID: must be positive and not null");
    }
  }

  protected void validateNotNull(Object object, String message) {
    if (object == null) {
      throw new IllegalArgumentException(message);
    }
  }

  protected void validateStringNotBlank(String value, String fieldName) {
    if (value == null || value.trim().isEmpty()) {
      throw new IllegalArgumentException(fieldName + " cannot be null or empty");
    }
  }

  public void validateEmail(String email) {
    validateStringNotBlank(email, "Email");
    if (!isValidEmail(email)) {
      throw new IllegalArgumentException("Invalid email format");
    }
  }

  protected boolean isValidEmail(String email) {
    return email != null && email.matches("^[^@\\s]+@[^@\\s]+\\.[^@\\s]+$");
  }
}
