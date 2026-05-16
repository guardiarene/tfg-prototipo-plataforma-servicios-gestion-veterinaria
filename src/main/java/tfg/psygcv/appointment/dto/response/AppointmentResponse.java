package tfg.psygcv.appointment.dto.response;

import java.time.LocalDate;
import java.time.LocalTime;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import tfg.psygcv.appointment.entity.AppointmentStatus;

@Getter
@Setter
@NoArgsConstructor
public class AppointmentResponse {

  private Long id;
  private String clinicName;
  private String clinicAddress;
  private String clinicPhone;
  private String clinicEmail;
  private LocalDate date;
  private LocalTime time;
  private String medicalServiceName;
  private Long medicalServiceId;
  private AppointmentStatus appointmentStatus;
  private String petName;
  private String petSpeciesDescription;
  private String customerFullName;
}
