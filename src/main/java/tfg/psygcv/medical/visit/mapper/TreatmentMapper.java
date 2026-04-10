package tfg.psygcv.medical.visit.mapper;

import tfg.psygcv.medical.visit.dto.request.TreatmentRequest;
import tfg.psygcv.medical.visit.dto.response.TreatmentResponse;
import tfg.psygcv.medical.visit.entity.Treatment;

public class TreatmentMapper {

  private TreatmentMapper() {
    throw new UnsupportedOperationException("Utility class");
  }

  public static TreatmentResponse toResponse(Treatment treatment) {
    TreatmentResponse response = new TreatmentResponse();
    response.setId(treatment.getId());
    response.setProduct(treatment.getProduct());
    response.setRoute(treatment.getRoute());
    response.setFrequency(treatment.getFrequency());
    response.setStartDate(treatment.getStartDate());
    response.setEndDate(treatment.getEndDate());
    return response;
  }

  public static TreatmentRequest toRequest(Treatment treatment) {
    TreatmentRequest request = new TreatmentRequest();
    request.setId(treatment.getId());
    request.setProduct(treatment.getProduct());
    request.setRoute(treatment.getRoute());
    request.setFrequency(treatment.getFrequency());
    request.setStartDate(treatment.getStartDate());
    request.setEndDate(treatment.getEndDate());
    return request;
  }
}
