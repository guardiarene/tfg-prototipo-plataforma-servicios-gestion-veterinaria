package tfg.psygcv.service.validator;

import java.time.LocalDate;
import org.springframework.stereotype.Component;
import tfg.psygcv.model.user.User;

@Component
public class StatisticsValidator {

  public void validateVeterinarian(User veterinarian) {
    if (veterinarian == null) {
      throw new IllegalArgumentException("Veterinarian cannot be null");
    }
    if (veterinarian.getId() == null) {
      throw new IllegalArgumentException("Veterinarian must have a valid ID");
    }
  }

  public void validateDateRange(LocalDate startDate, LocalDate endDate) {
    if (startDate == null) {
      throw new IllegalArgumentException("Start date cannot be null");
    }
    if (endDate == null) {
      throw new IllegalArgumentException("End date cannot be null");
    }
    if (startDate.isAfter(endDate)) {
      throw new IllegalArgumentException("Start date cannot be after end date");
    }
    if (endDate.isAfter(LocalDate.now())) {
      throw new IllegalArgumentException("End date cannot be in the future");
    }
    if (startDate.plusYears(2).isBefore(endDate)) {
      throw new IllegalArgumentException("Date range cannot exceed 2 years");
    }
  }
}
