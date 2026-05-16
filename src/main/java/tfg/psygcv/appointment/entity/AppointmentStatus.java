package tfg.psygcv.appointment.entity;

import lombok.Getter;

@Getter
public enum AppointmentStatus {
  PENDING("Pendiente de confirmación"),
  CONFIRMED("Confirmada"),
  CANCELLED("Cancelada"),
  COMPLETED("Completada"),
  NO_SHOW("No se presentó");

  private final String description;

  AppointmentStatus(String description) {
    this.description = description;
  }

  @Override
  public String toString() {
    return this.name();
  }
}
