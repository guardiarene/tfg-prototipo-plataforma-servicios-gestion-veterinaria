package tfg.psygcv.pet.mapper;

import tfg.psygcv.pet.command.CreatePetCommand;
import tfg.psygcv.pet.command.UpdatePetCommand;
import tfg.psygcv.pet.dto.request.CreatePetRequest;
import tfg.psygcv.pet.dto.request.UpdatePetRequest;
import tfg.psygcv.pet.dto.response.PetResponse;
import tfg.psygcv.pet.dto.response.PetSummaryResponse;
import tfg.psygcv.pet.entity.Pet;

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

  public static CreatePetCommand toCreateCommand(CreatePetRequest request, Long ownerId) {
    return CreatePetCommand.builder()
        .name(request.getName())
        .sex(request.getSex())
        .breed(request.getBreed())
        .species(request.getSpecies())
        .birthDate(request.getBirthDate())
        .weight(request.getWeight())
        .ownerId(ownerId)
        .build();
  }

  public static UpdatePetCommand toUpdateCommand(UpdatePetRequest request) {
    return UpdatePetCommand.builder()
        .name(request.getName())
        .sex(request.getSex())
        .breed(request.getBreed())
        .species(request.getSpecies())
        .birthDate(request.getBirthDate())
        .weight(request.getWeight())
        .build();
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
