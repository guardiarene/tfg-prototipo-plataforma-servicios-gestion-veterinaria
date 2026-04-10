package tfg.psygcv.statistics.service;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tfg.psygcv.appointment.repository.AppointmentStatisticsRepository;
import tfg.psygcv.clinic.entity.VeterinaryClinic;
import tfg.psygcv.clinic.repository.VeterinaryClinicRepository;
import tfg.psygcv.medical.visit.repository.DiagnosticStatisticsRepository;
import tfg.psygcv.medical.visit.repository.TreatmentStatisticsRepository;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class StatisticsServiceImpl implements StatisticsService {

  private final DiagnosticStatisticsRepository diagnosticStatisticsRepository;
  private final TreatmentStatisticsRepository treatmentStatisticsRepository;
  private final VeterinaryClinicRepository veterinaryClinicRepository;
  private final AppointmentStatisticsRepository appointmentStatisticsRepository;
  private final StatisticsValidator statisticsValidator;

  @Override
  public Map<String, Long> getCommonDiseases(Long userId, LocalDate startDate, LocalDate endDate) {
    statisticsValidator.validateDateRange(startDate, endDate);
    statisticsValidator.validateUserId(userId);
    VeterinaryClinic clinic = getClinic(userId);
    List<String> problems =
        diagnosticStatisticsRepository.getFrequentProblemsByClinicAndDate(
            clinic.getId(), startDate, endDate);
    return processProblems(problems);
  }

  @Override
  public Map<String, Long> getFrequentTreatments(
      Long userId, LocalDate startDate, LocalDate endDate) {
    statisticsValidator.validateDateRange(startDate, endDate);
    statisticsValidator.validateUserId(userId);
    VeterinaryClinic clinic = getClinic(userId);
    List<String> treatments =
        treatmentStatisticsRepository.getFrequentTreatmentsByClinicAndDate(
            clinic.getId(), startDate, endDate);
    return processTreatments(treatments);
  }

  @Override
  public Map<LocalDate, Long> getAppointmentsByDate(
      Long userId, LocalDate startDate, LocalDate endDate) {
    statisticsValidator.validateDateRange(startDate, endDate);
    statisticsValidator.validateUserId(userId);
    VeterinaryClinic clinic = getClinic(userId);
    return appointmentStatisticsRepository
        .countAppointmentsByDate(clinic.getId(), startDate, endDate)
        .stream()
        .collect(
            Collectors.toMap(
                tuple -> (LocalDate) tuple[0],
                tuple -> (Long) tuple[1],
                (v1, v2) -> v1,
                TreeMap::new));
  }

  @Override
  public Map<String, Long> getRequestedServices(
      Long userId, LocalDate startDate, LocalDate endDate) {
    statisticsValidator.validateDateRange(startDate, endDate);
    statisticsValidator.validateUserId(userId);
    VeterinaryClinic clinic = getClinic(userId);
    return appointmentStatisticsRepository
        .countRequestedServices(clinic.getId(), startDate, endDate)
        .stream()
        .collect(
            Collectors.toMap(
                tuple -> (String) tuple[0],
                tuple -> (Long) tuple[1],
                (v1, v2) -> v1,
                LinkedHashMap::new));
  }

  private VeterinaryClinic getClinic(Long userId) {
    return veterinaryClinicRepository
        .findByVeterinarianId(userId)
        .or(() -> veterinaryClinicRepository.findByOwnerIdOptional(userId))
        .or(() -> veterinaryClinicRepository.findByReceptionistId(userId))
        .orElseThrow(
            () -> new IllegalArgumentException("No clinic found for dashboard with ID: " + userId));
  }

  private Map<String, Long> processProblems(List<String> problems) {
    return problems.stream()
        .flatMap(problem -> Arrays.stream(problem.split(",\\s*")))
        .filter(problem -> !problem.isBlank())
        .map(String::trim)
        .map(String::toLowerCase)
        .collect(Collectors.groupingBy(problem -> problem, Collectors.counting()))
        .entrySet()
        .stream()
        .sorted(Map.Entry.<String, Long>comparingByValue().reversed())
        .collect(
            Collectors.toMap(
                Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e1, LinkedHashMap::new));
  }

  private Map<String, Long> processTreatments(List<String> treatments) {
    return treatments.stream()
        .filter(treatment -> !treatment.isBlank())
        .map(String::trim)
        .map(String::toLowerCase)
        .collect(Collectors.groupingBy(treatment -> treatment, Collectors.counting()))
        .entrySet()
        .stream()
        .sorted(Map.Entry.<String, Long>comparingByValue().reversed())
        .collect(
            Collectors.toMap(
                Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e1, LinkedHashMap::new));
  }
}
