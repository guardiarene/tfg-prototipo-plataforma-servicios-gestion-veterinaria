package tfg.psygcv.dto.appointment.response;

import java.time.LocalDate;
import java.time.LocalTime;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import tfg.psygcv.entity.appointment.AppointmentStatus;

@Getter
@Setter
@NoArgsConstructor
public class AppointmentSummaryResponse {

  private Long id;
  private String clinicName;
  private String clinicAddress;
  private LocalDate date;
  private LocalTime time;
  private String petName;
  private String petSpeciesDescription;
  private String customerFullName;
  private AppointmentStatus appointmentStatus;
}
