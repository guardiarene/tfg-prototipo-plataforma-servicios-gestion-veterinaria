package tfg.psygcv.service.appointment;

import java.time.LocalDate;
import java.time.LocalTime;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class ScheduleAppointmentCommand {

  private final LocalDate date;
  private final LocalTime time;
  private final Long petId;
}
