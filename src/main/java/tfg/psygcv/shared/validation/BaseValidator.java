package tfg.psygcv.shared.validation;

import tfg.psygcv.exception.domain.BusinessRuleException;

public abstract class BaseValidator {

  public void validateId(Long id) {
    if (id == null || id <= 0) {
      throw new BusinessRuleException("El identificador no es válido");
    }
  }

  protected void validateNotNull(Object object, String message) {
    if (object == null) {
      throw new BusinessRuleException(message);
    }
  }

  protected void validateStringNotBlank(String value, String fieldName) {
    if (value == null || value.trim().isEmpty()) {
      throw new BusinessRuleException(fieldName + " no puede estar vacío");
    }
  }

  public void validateEmail(String email) {
    validateStringNotBlank(email, "El email");
    if (!isValidEmail(email)) {
      throw new BusinessRuleException("El formato del email no es válido");
    }
  }

  protected boolean isValidEmail(String email) {
    return email != null && email.matches("^[^@\\s]+@[^@\\s]+\\.[^@\\s]+$");
  }
}
