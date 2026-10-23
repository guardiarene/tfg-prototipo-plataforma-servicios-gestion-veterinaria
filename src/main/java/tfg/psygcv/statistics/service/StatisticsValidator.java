package tfg.psygcv.statistics.service;

import java.time.LocalDate;
import org.springframework.stereotype.Component;
import tfg.psygcv.exception.domain.BusinessRuleException;
import tfg.psygcv.shared.validation.BaseValidator;

@Component
public class StatisticsValidator extends BaseValidator {

  public void validateUserId(Long userId) {
    validateId(userId);
  }

  public void validateDateRange(LocalDate startDate, LocalDate endDate) {
    validateNotNull(startDate, "Start date cannot be null");
    validateNotNull(endDate, "End date cannot be null");
    if (startDate.isAfter(endDate)) {
      throw new BusinessRuleException("Start date cannot be after end date");
    }
    if (endDate.isAfter(LocalDate.now())) {
      throw new BusinessRuleException("End date cannot be in the future");
    }
    if (startDate.plusYears(2).isBefore(endDate)) {
      throw new BusinessRuleException("Date range cannot exceed 2 years");
    }
  }
}
