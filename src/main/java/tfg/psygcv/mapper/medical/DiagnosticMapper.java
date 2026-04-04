package tfg.psygcv.mapper.medical;

import java.util.ArrayList;
import tfg.psygcv.dto.medical.request.DiagnosticRequest;
import tfg.psygcv.dto.medical.response.DiagnosticResponse;
import tfg.psygcv.entity.medical.Diagnostic;

public class DiagnosticMapper {

  private DiagnosticMapper() {
    throw new UnsupportedOperationException("Utility class");
  }

  public static DiagnosticResponse toResponse(Diagnostic diagnostic) {
    DiagnosticResponse response = new DiagnosticResponse();
    response.setId(diagnostic.getId());
    response.setProblems(new ArrayList<>(diagnostic.getProblems()));
    return response;
  }

  public static Diagnostic toEntity(DiagnosticRequest request) {
    Diagnostic diagnostic = new Diagnostic();
    if (request.getProblems() != null) {
      diagnostic.setProblems(new ArrayList<>(request.getProblems()));
    }
    return diagnostic;
  }

  public static DiagnosticRequest toRequest(Diagnostic diagnostic) {
    DiagnosticRequest request = new DiagnosticRequest();
    request.setId(diagnostic.getId());
    request.setProblems(new ArrayList<>(diagnostic.getProblems()));
    return request;
  }
}
