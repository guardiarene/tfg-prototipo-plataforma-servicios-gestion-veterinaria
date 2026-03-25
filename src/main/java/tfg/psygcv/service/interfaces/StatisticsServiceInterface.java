package tfg.psygcv.service.interfaces;

import java.time.LocalDate;
import java.util.Map;
import tfg.psygcv.model.user.User;

public interface StatisticsServiceInterface {

  Map<String, Long> getCommonDiseases(User user, LocalDate startDate, LocalDate endDate);

  Map<String, Long> getFrequentTreatments(User user, LocalDate startDate, LocalDate endDate);

  Map<LocalDate, Long> getAppointmentsByDate(User user, LocalDate startDate, LocalDate endDate);

  Map<String, Long> getRequestedServices(User user, LocalDate startDate, LocalDate endDate);
}
