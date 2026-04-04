package tfg.psygcv.dto.appointment.request;

import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.LocalTime;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

@Getter
@Setter
@NoArgsConstructor
public class ScheduleAppointmentRequest {

  @NotNull(message = "La fecha es obligatoria")
  @FutureOrPresent(message = "La fecha no puede ser en el pasado")
  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
  private LocalDate date;

  @NotNull(message = "La hora es obligatoria")
  @DateTimeFormat(iso = DateTimeFormat.ISO.TIME)
  private LocalTime time;

  @NotNull(message = "La mascota es obligatoria")
  private Long petId;
}
