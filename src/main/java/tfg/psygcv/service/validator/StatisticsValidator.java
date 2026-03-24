package tfg.psygcv.service.validator;

import java.time.LocalDate;
import org.springframework.stereotype.Component;
import tfg.psygcv.model.user.User;

@Component
public class StatisticsValidator extends BaseValidator {

  public void validateVeterinarian(User veterinarian) {
    validateNotNull(veterinarian, "Veterinarian cannot be null");
    validateNotNull(veterinarian.getId(), "Veterinarian must have a valid ID");
  }

  public void validateDateRange(LocalDate startDate, LocalDate endDate) {
    validateNotNull(startDate, "Start date cannot be null");
    validateNotNull(endDate, "End date cannot be null");
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
