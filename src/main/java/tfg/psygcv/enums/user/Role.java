package tfg.psygcv.enums.user;

import lombok.Getter;

@Getter
public enum Role {
  CUSTOMER("Cliente"),
  VETERINARIAN("Veterinario"),
  RECEPTIONIST("Recepcionista"),
  SYSTEM_ADMINISTRATOR("Administrador del sistema");

  private final String description;

  Role(String description) {
    this.description = description;
  }

  @Override
  public String toString() {
    return this.name();
  }
}
