package tfg.psygcv.service.interfaces;

import tfg.psygcv.model.user.User;

import java.time.LocalDate;
import java.util.Map;

public interface StatisticsServiceInterface {

    Map<String, Long> getCommonDiseases(User veterinarian, LocalDate startDate, LocalDate endDate);

    Map<String, Long> getFrequentTreatments(User veterinarian, LocalDate startDate, LocalDate endDate);

    Map<LocalDate, Long> getAppointmentsByDate(User veterinarian, LocalDate startDate, LocalDate endDate);

    Map<String, Long> getRequestedServices(User veterinarian, LocalDate startDate, LocalDate endDate);

}
