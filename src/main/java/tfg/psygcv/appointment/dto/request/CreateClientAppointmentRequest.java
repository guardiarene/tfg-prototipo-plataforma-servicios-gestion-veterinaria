package tfg.psygcv.appointment.dto.request;

import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

@Getter
@Setter
@NoArgsConstructor
public class CreateClientAppointmentRequest {

  @NotNull(message = "La clínica es obligatoria")
  private Long clinicId;

  @NotNull(message = "La fecha es obligatoria")
  @FutureOrPresent(message = "La fecha no puede ser en el pasado")
  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
  private LocalDate date;

  @NotNull(message = "La mascota es obligatoria")
  private Long petId;

  @NotNull(message = "El servicio es obligatorio")
  private Long serviceId;
}
