package tfg.psygcv.mapper.medical;

import tfg.psygcv.dto.medical.request.AnamnesisRequest;
import tfg.psygcv.dto.medical.response.AnamnesisResponse;
import tfg.psygcv.entity.medical.Anamnesis;

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

  public static Anamnesis toEntity(AnamnesisRequest request) {
    if (request == null) {
      return null;
    }
    Anamnesis anamnesis = new Anamnesis();
    anamnesis.setAllergies(request.getAllergies());
    anamnesis.setPreviousDiseases(request.getPreviousDiseases());
    anamnesis.setSurgeries(request.getSurgeries());
    anamnesis.setCurrentMedications(request.getCurrentMedications());
    anamnesis.setDiet(request.getDiet());
    anamnesis.setReproductiveStatus(request.getReproductiveStatus());
    anamnesis.setLastDewormingDate(request.getLastDewormingDate());
    anamnesis.setLastHeatDate(request.getLastHeatDate());
    anamnesis.setLastBirthDate(request.getLastBirthDate());
    return anamnesis;
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
