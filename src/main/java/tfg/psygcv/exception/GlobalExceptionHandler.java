package tfg.psygcv.exception;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.UUID;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.resource.NoResourceFoundException;
import tfg.psygcv.exception.domain.DuplicateEmailException;
import tfg.psygcv.exception.domain.InvalidAppointmentStateException;
import tfg.psygcv.exception.domain.ResourceNotFoundException;
import tfg.psygcv.exception.domain.UnauthorizedClinicAccessException;

@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {

  @ExceptionHandler(ResourceNotFoundException.class)
  public String handleResourceNotFound(
      ResourceNotFoundException ex,
      HttpServletRequest request,
      HttpServletResponse response,
      RedirectAttributes ra) {
    if (isPost(request)) {
      log.warn("Resource not found on POST [path={}]: {}", request.getRequestURI(),
          ex.getMessage());
      ra.addFlashAttribute("error", ex.getMessage());
      return "redirect:" + referer(request);
    }
    log.warn("Resource not found [path={}]: {}", request.getRequestURI(), ex.getMessage());
    response.setStatus(HttpStatus.NOT_FOUND.value());
    return "error/404";
  }

  @ExceptionHandler({UnauthorizedClinicAccessException.class, AccessDeniedException.class})
  @ResponseStatus(HttpStatus.FORBIDDEN)
  public String handleAccessDenied(RuntimeException ex, HttpServletRequest request) {
    log.warn("Access denied [path={}]: {}", request.getRequestURI(), ex.getMessage());
    return "error/access_denied";
  }

  @ExceptionHandler(DuplicateEmailException.class)
  public String handleDuplicateEmail(
      DuplicateEmailException ex, HttpServletRequest request, RedirectAttributes ra) {
    log.warn("Duplicate email attempt [path={}]: {}", request.getRequestURI(), ex.getMessage());
    if (isPost(request)) {
      ra.addFlashAttribute("error", ex.getMessage());
      return "redirect:" + referer(request);
    }
    return "error/500";
  }

  @ExceptionHandler(InvalidAppointmentStateException.class)
  public String handleInvalidAppointmentState(
      InvalidAppointmentStateException ex, HttpServletRequest request, RedirectAttributes ra) {
    log.warn("Invalid appointment state [path={}]: {}", request.getRequestURI(), ex.getMessage());
    if (isPost(request)) {
      ra.addFlashAttribute("error", ex.getMessage());
      return "redirect:" + referer(request);
    }
    return "error/404";
  }

  @ExceptionHandler({NoResourceFoundException.class, MethodArgumentTypeMismatchException.class})
  @ResponseStatus(HttpStatus.NOT_FOUND)
  public String handleNotFound(Exception ex, HttpServletRequest request) {
    log.warn("No resource found [path={}]: {}", request.getRequestURI(), ex.getMessage());
    return "error/404";
  }

  @ExceptionHandler(Exception.class)
  @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
  public String handleUnexpected(Exception ex, HttpServletRequest request, Model model) {
    String correlationId = UUID.randomUUID().toString();
    log.error(
        "Unexpected error [correlationId={}, path={}, method={}]: {}",
        correlationId,
        request.getRequestURI(),
        request.getMethod(),
        ex.getMessage(),
        ex);
    model.addAttribute("correlationId", correlationId);
    return "error/500";
  }

  private boolean isPost(HttpServletRequest request) {
    return "POST".equalsIgnoreCase(request.getMethod());
  }

  private String referer(HttpServletRequest request) {
    String referer = request.getHeader("Referer");
    return (referer != null && !referer.isBlank()) ? referer : "/";
  }
}
