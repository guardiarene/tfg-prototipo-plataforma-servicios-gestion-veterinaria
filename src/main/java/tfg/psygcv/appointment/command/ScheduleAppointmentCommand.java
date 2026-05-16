package tfg.psygcv.appointment.command;

import java.time.LocalDate;
import java.time.LocalTime;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class ScheduleAppointmentCommand {

  private final Long customerId;
  private final LocalDate date;
  private final LocalTime time;
  private final Long petId;
  private final Long serviceId;
}
