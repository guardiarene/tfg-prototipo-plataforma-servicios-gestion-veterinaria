package tfg.psygcv.medical.visit.mapper;

import tfg.psygcv.medical.visit.dto.request.ClinicalExamRequest;
import tfg.psygcv.medical.visit.dto.response.ClinicalExamResponse;
import tfg.psygcv.medical.visit.entity.ClinicalExam;

public class ClinicalExamMapper {

  private ClinicalExamMapper() {
    throw new UnsupportedOperationException("Utility class");
  }

  public static ClinicalExamResponse toResponse(ClinicalExam clinicalExam) {
    if (clinicalExam == null) {
      return null;
    }
    ClinicalExamResponse response = new ClinicalExamResponse();
    response.setTemperature(clinicalExam.getTemperature());
    response.setHeartRate(clinicalExam.getHeartRate());
    response.setRespiratoryRate(clinicalExam.getRespiratoryRate());
    response.setWeight(clinicalExam.getWeight());
    response.setPulse(clinicalExam.getPulse());
    response.setMucosalMembranes(clinicalExam.getMucosalMembranes());
    response.setTemperament(clinicalExam.getTemperament());
    response.setDescription(clinicalExam.getDescription());
    return response;
  }

  public static ClinicalExamRequest toRequest(ClinicalExam clinicalExam) {
    if (clinicalExam == null) {
      return new ClinicalExamRequest();
    }
    ClinicalExamRequest request = new ClinicalExamRequest();
    request.setTemperature(clinicalExam.getTemperature());
    request.setHeartRate(clinicalExam.getHeartRate());
    request.setRespiratoryRate(clinicalExam.getRespiratoryRate());
    request.setWeight(clinicalExam.getWeight());
    request.setPulse(clinicalExam.getPulse());
    request.setMucosalMembranes(clinicalExam.getMucosalMembranes());
    request.setTemperament(clinicalExam.getTemperament());
    request.setDescription(clinicalExam.getDescription());
    return request;
  }
}
