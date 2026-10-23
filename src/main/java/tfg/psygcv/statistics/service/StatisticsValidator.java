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
    validateNotNull(startDate, "La fecha de inicio es obligatoria");
    validateNotNull(endDate, "La fecha de fin es obligatoria");
    if (startDate.isAfter(endDate)) {
      throw new BusinessRuleException(
          "La fecha de inicio no puede ser posterior a la fecha de fin");
    }
    if (endDate.isAfter(LocalDate.now())) {
      throw new BusinessRuleException("La fecha de fin no puede ser en el futuro");
    }
    if (startDate.plusYears(2).isBefore(endDate)) {
      throw new BusinessRuleException("El rango de fechas no puede superar los 2 años");
    }
  }
}
