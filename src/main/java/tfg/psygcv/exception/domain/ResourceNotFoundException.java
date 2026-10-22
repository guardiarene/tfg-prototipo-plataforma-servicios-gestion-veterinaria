package tfg.psygcv.exception.domain;

public class ResourceNotFoundException extends RuntimeException {

  public ResourceNotFoundException(String message) {
    super(message);
  }

  public ResourceNotFoundException(String message, Long resourceId) {
    super(message + " (ID: " + resourceId + ")");
  }
}
