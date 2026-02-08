package tfg.psygcv.model.user;

import lombok.Getter;

@Getter
public enum Role {
  CUSTOMER("Customer user"),
  VETERINARIAN("Veterinarian user"),
  RECEPTIONIST("Receptionist user"),
  SYSTEM_ADMINISTRATOR("System administrator");

  private final String description;

  Role(String description) {
    this.description = description;
  }

  @Override
  public String toString() {
    return this.name();
  }
}
