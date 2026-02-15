package tfg.psygcv.model.medical;

import lombok.Getter;

@Getter
public enum VisitType {
  ROUTINE_CHECKUP("Control de rutina"),
  EMERGENCY("Emergencia"),
  VACCINATION("Vacunación"),
  FOLLOW_UP("Seguimiento"),
  SURGERY("Cirugía"),
  CONSULTATION("Consulta general"),
  HOSPITALIZATION("Hospitalización"),
  LABORATORY("Exámenes de laboratorio"),
  OTHER("Otro");

  private final String description;

  VisitType(String description) {
    this.description = description;
  }

  @Override
  public String toString() {
    return this.name();
  }
}
