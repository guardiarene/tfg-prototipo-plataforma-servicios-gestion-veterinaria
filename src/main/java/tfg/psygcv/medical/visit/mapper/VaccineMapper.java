package tfg.psygcv.medical.visit.mapper;

import tfg.psygcv.medical.visit.dto.request.VaccineRequest;
import tfg.psygcv.medical.visit.dto.response.VaccineResponse;
import tfg.psygcv.medical.visit.entity.Vaccine;

public class VaccineMapper {

  private VaccineMapper() {
    throw new UnsupportedOperationException("Utility class");
  }

  public static VaccineResponse toResponse(Vaccine vaccine) {
    VaccineResponse response = new VaccineResponse();
    response.setId(vaccine.getId());
    response.setApplicationDate(vaccine.getApplicationDate());
    response.setBrand(vaccine.getBrand());
    response.setDose(vaccine.getDose());
    response.setBatch(vaccine.getBatch());
    return response;
  }

  public static VaccineRequest toRequest(Vaccine vaccine) {
    VaccineRequest request = new VaccineRequest();
    request.setId(vaccine.getId());
    request.setApplicationDate(vaccine.getApplicationDate());
    request.setBrand(vaccine.getBrand());
    request.setDose(vaccine.getDose());
    request.setBatch(vaccine.getBatch());
    return request;
  }
}
