package tfg.psygcv.exception.domain;

public class DuplicateEmailException extends BusinessRuleException {

  public DuplicateEmailException(String message) {
    super(message);
  }
}
