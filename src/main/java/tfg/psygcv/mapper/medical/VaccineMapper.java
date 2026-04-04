package tfg.psygcv.mapper.medical;

import tfg.psygcv.dto.medical.request.VaccineRequest;
import tfg.psygcv.dto.medical.response.VaccineResponse;
import tfg.psygcv.entity.medical.Vaccine;

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

  public static Vaccine toEntity(VaccineRequest request) {
    Vaccine vaccine = new Vaccine();
    vaccine.setApplicationDate(request.getApplicationDate());
    vaccine.setBrand(request.getBrand());
    vaccine.setDose(request.getDose());
    vaccine.setBatch(request.getBatch());
    return vaccine;
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
