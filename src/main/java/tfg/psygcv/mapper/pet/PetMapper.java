package tfg.psygcv.mapper.pet;

import tfg.psygcv.dto.pet.request.UpdatePetRequest;
import tfg.psygcv.dto.pet.response.PetResponse;
import tfg.psygcv.dto.pet.response.PetSummaryResponse;
import tfg.psygcv.entity.pet.Pet;

public class PetMapper {

  private PetMapper() {
    throw new UnsupportedOperationException("Utility class");
  }

  public static UpdatePetRequest toUpdateRequest(Pet pet) {
    UpdatePetRequest request = new UpdatePetRequest();
    request.setName(pet.getName());
    request.setSex(pet.getSex());
    request.setBreed(pet.getBreed());
    request.setSpecies(pet.getSpecies());
    request.setBirthDate(pet.getBirthDate());
    request.setWeight(pet.getWeight());
    return request;
  }

  public static PetResponse toResponse(Pet pet) {
    PetResponse response = new PetResponse();
    response.setId(pet.getId());
    response.setName(pet.getName());
    response.setSex(pet.getSex());
    response.setBreed(pet.getBreed());
    response.setSpecies(pet.getSpecies());
    response.setBirthDate(pet.getBirthDate());
    response.setWeight(pet.getWeight());
    response.setMedicalRecordId(
        pet.getMedicalRecord() != null ? pet.getMedicalRecord().getId() : null);
    return response;
  }

  public static PetSummaryResponse toSummary(Pet pet) {
    PetSummaryResponse summary = new PetSummaryResponse();
    summary.setId(pet.getId());
    summary.setName(pet.getName());
    summary.setSpecies(pet.getSpecies());
    summary.setBreed(pet.getBreed());
    summary.setBirthDate(pet.getBirthDate());
    summary.setMedicalRecordId(
        pet.getMedicalRecord() != null ? pet.getMedicalRecord().getId() : null);
    if (pet.getOwner() != null) {
      summary.setOwnerFullName(pet.getOwner().getFirstName() + " " + pet.getOwner().getLastName());
    }
    return summary;
  }
}
