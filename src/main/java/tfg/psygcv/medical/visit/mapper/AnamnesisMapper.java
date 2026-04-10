package tfg.psygcv.medical.visit.mapper;

import tfg.psygcv.medical.visit.dto.request.AnamnesisRequest;
import tfg.psygcv.medical.visit.dto.response.AnamnesisResponse;
import tfg.psygcv.medical.visit.entity.Anamnesis;

public class AnamnesisMapper {

  private AnamnesisMapper() {
    throw new UnsupportedOperationException("Utility class");
  }

  public static AnamnesisResponse toResponse(Anamnesis anamnesis) {
    if (anamnesis == null) {
      return null;
    }
    AnamnesisResponse response = new AnamnesisResponse();
    response.setAllergies(anamnesis.getAllergies());
    response.setPreviousDiseases(anamnesis.getPreviousDiseases());
    response.setSurgeries(anamnesis.getSurgeries());
    response.setCurrentMedications(anamnesis.getCurrentMedications());
    response.setDiet(anamnesis.getDiet());
    response.setReproductiveStatus(anamnesis.getReproductiveStatus());
    response.setLastDewormingDate(anamnesis.getLastDewormingDate());
    response.setLastHeatDate(anamnesis.getLastHeatDate());
    response.setLastBirthDate(anamnesis.getLastBirthDate());
    return response;
  }

  public static AnamnesisRequest toRequest(Anamnesis anamnesis) {
    if (anamnesis == null) {
      return new AnamnesisRequest();
    }
    AnamnesisRequest request = new AnamnesisRequest();
    request.setAllergies(anamnesis.getAllergies());
    request.setPreviousDiseases(anamnesis.getPreviousDiseases());
    request.setSurgeries(anamnesis.getSurgeries());
    request.setCurrentMedications(anamnesis.getCurrentMedications());
    request.setDiet(anamnesis.getDiet());
    request.setReproductiveStatus(anamnesis.getReproductiveStatus());
    request.setLastDewormingDate(anamnesis.getLastDewormingDate());
    request.setLastHeatDate(anamnesis.getLastHeatDate());
    request.setLastBirthDate(anamnesis.getLastBirthDate());
    return request;
  }
}
