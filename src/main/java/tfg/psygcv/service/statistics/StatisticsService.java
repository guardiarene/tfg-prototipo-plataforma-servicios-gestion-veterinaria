package tfg.psygcv.service.statistics;

import java.time.LocalDate;
import java.util.Map;
import tfg.psygcv.entity.user.User;

public interface StatisticsService {

  Map<String, Long> getCommonDiseases(User user, LocalDate startDate, LocalDate endDate);

  Map<String, Long> getFrequentTreatments(User user, LocalDate startDate, LocalDate endDate);

  Map<LocalDate, Long> getAppointmentsByDate(User user, LocalDate startDate, LocalDate endDate);

  Map<String, Long> getRequestedServices(User user, LocalDate startDate, LocalDate endDate);
}
