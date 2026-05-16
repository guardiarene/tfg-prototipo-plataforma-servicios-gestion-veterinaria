package tfg.psygcv.medical.visit.mapper;

import java.util.ArrayList;
import tfg.psygcv.medical.visit.dto.request.DiagnosticRequest;
import tfg.psygcv.medical.visit.dto.response.DiagnosticResponse;
import tfg.psygcv.medical.visit.entity.Diagnostic;

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

  public static DiagnosticRequest toRequest(Diagnostic diagnostic) {
    DiagnosticRequest request = new DiagnosticRequest();
    request.setId(diagnostic.getId());
    request.setProblems(new ArrayList<>(diagnostic.getProblems()));
    return request;
  }
}
