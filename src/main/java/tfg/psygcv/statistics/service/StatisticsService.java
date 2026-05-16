package tfg.psygcv.statistics.service;

import java.time.LocalDate;
import java.util.Map;

public interface StatisticsService {

  Map<String, Long> getCommonDiseases(Long userId, LocalDate startDate, LocalDate endDate);

  Map<String, Long> getFrequentTreatments(Long userId, LocalDate startDate, LocalDate endDate);

  Map<LocalDate, Long> getAppointmentsByDate(Long userId, LocalDate startDate, LocalDate endDate);

  Map<String, Long> getRequestedServices(Long userId, LocalDate startDate, LocalDate endDate);
}
