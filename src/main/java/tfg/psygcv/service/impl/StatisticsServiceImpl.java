package tfg.psygcv.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tfg.psygcv.model.clinic.VeterinaryClinic;
import tfg.psygcv.model.user.User;
import tfg.psygcv.repository.base.VeterinaryClinicRepository;
import tfg.psygcv.repository.statistics.AppointmentStatisticsRepository;
import tfg.psygcv.repository.statistics.DiagnosticStatisticsRepository;
import tfg.psygcv.repository.statistics.TreatmentStatisticsRepository;
import tfg.psygcv.service.interfaces.StatisticsServiceInterface;
import tfg.psygcv.service.validator.StatisticsValidator;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class StatisticsServiceImpl implements StatisticsServiceInterface {

    private final DiagnosticStatisticsRepository diagnosticStatisticsRepository;

    private final TreatmentStatisticsRepository treatmentStatisticsRepository;

    private final VeterinaryClinicRepository veterinaryClinicRepository;

    private final AppointmentStatisticsRepository appointmentStatisticsRepository;

    private final StatisticsValidator statisticsValidator;

    @Override
    public Map<String, Long> getCommonDiseases(User veterinarian, LocalDate startDate, LocalDate endDate) {
        statisticsValidator.validateDateRange(startDate, endDate);
        statisticsValidator.validateVeterinarian(veterinarian);
        VeterinaryClinic clinic = getClinic(veterinarian);
        List<String> problems = diagnosticStatisticsRepository.getFrequentProblemsByClinicAndDate(clinic.getId(), startDate, endDate);
        return processProblems(problems);
    }

    @Override
    public Map<String, Long> getFrequentTreatments(User veterinarian, LocalDate startDate, LocalDate endDate) {
        statisticsValidator.validateDateRange(startDate, endDate);
        statisticsValidator.validateVeterinarian(veterinarian);
        VeterinaryClinic clinic = getClinic(veterinarian);
        List<String> treatments = treatmentStatisticsRepository.getFrequentTreatmentsByClinicAndDate(clinic.getId(), startDate, endDate);
        return processTreatments(treatments);
    }

    @Override
    public Map<LocalDate, Long> getAppointmentsByDate(User veterinarian, LocalDate startDate, LocalDate endDate) {
        statisticsValidator.validateDateRange(startDate, endDate);
        statisticsValidator.validateVeterinarian(veterinarian);
        VeterinaryClinic clinic = getClinic(veterinarian);
        return appointmentStatisticsRepository.countAppointmentsByDate(clinic.getId(), startDate, endDate)
                .stream()
                .collect(Collectors.toMap(
                        tuple -> (LocalDate) tuple[0],
                        tuple -> (Long) tuple[1],
                        (v1, v2) -> v1,
                        TreeMap::new
                ));
    }

    @Override
    public Map<String, Long> getRequestedServices(User veterinarian, LocalDate startDate, LocalDate endDate) {
        statisticsValidator.validateDateRange(startDate, endDate);
        statisticsValidator.validateVeterinarian(veterinarian);
        VeterinaryClinic clinic = getClinic(veterinarian);
        return appointmentStatisticsRepository.countRequestedServices(clinic.getId(), startDate, endDate)
                .stream()
                .collect(Collectors.toMap(
                        tuple -> (String) tuple[0],
                        tuple -> (Long) tuple[1],
                        (v1, v2) -> v1,
                        LinkedHashMap::new
                ));
    }

    private VeterinaryClinic getClinic(User veterinarian) {
        VeterinaryClinic clinic = veterinaryClinicRepository.findByVeterinarianId(veterinarian.getId());
        if (clinic == null) {
            throw new IllegalArgumentException("No clinic found for veterinarian with ID: " + veterinarian.getId());
        }
        return clinic;
    }

    private Map<String, Long> processProblems(List<String> problems) {
        return problems.stream()
                .flatMap(problem -> Arrays.stream(problem.split(",\\s*")))
                .filter(problem -> !problem.isBlank())
                .map(String::trim)
                .map(String::toLowerCase)
                .collect(Collectors.groupingBy(
                        problem -> problem,
                        Collectors.counting()
                ))
                .entrySet().stream()
                .sorted(Map.Entry.<String, Long>comparingByValue().reversed())
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue,
                        (e1, e2) -> e1,
                        LinkedHashMap::new
                ));
    }

    private Map<String, Long> processTreatments(List<String> treatments) {
        return treatments.stream()
                .filter(treatment -> !treatment.isBlank())
                .map(String::trim)
                .map(String::toLowerCase)
                .collect(Collectors.groupingBy(
                        treatment -> treatment,
                        Collectors.counting()
                ))
                .entrySet().stream()
                .sorted(Map.Entry.<String, Long>comparingByValue().reversed())
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue,
                        (e1, e2) -> e1,
                        LinkedHashMap::new
                ));
    }

}
