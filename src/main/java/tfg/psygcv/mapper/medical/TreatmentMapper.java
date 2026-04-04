package tfg.psygcv.mapper.medical;

import tfg.psygcv.dto.medical.request.TreatmentRequest;
import tfg.psygcv.dto.medical.response.TreatmentResponse;
import tfg.psygcv.entity.medical.Treatment;

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

  public static Treatment toEntity(TreatmentRequest request) {
    Treatment treatment = new Treatment();
    treatment.setProduct(request.getProduct());
    treatment.setRoute(request.getRoute());
    treatment.setFrequency(request.getFrequency());
    treatment.setStartDate(request.getStartDate());
    treatment.setEndDate(request.getEndDate());
    return treatment;
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
