package tfg.psygcv.service.impl;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tfg.psygcv.model.clinic.VeterinaryClinic;
import tfg.psygcv.repository.base.VeterinaryClinicRepository;
import tfg.psygcv.service.interfaces.VeterinaryClinicServiceInterface;
import tfg.psygcv.service.validator.VeterinaryClinicValidator;

import java.util.List;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class VeterinaryClinicServiceImpl implements VeterinaryClinicServiceInterface {

    private final VeterinaryClinicRepository veterinaryClinicRepository;

    private final VeterinaryClinicValidator veterinaryClinicValidator;

    @Override
    public List<VeterinaryClinic> findAll() {
        return veterinaryClinicRepository.findAllActive();
    }

    @Override
    public VeterinaryClinic findById(Long clinicId) {
        veterinaryClinicValidator.validateId(clinicId);
        return veterinaryClinicRepository.findByIdAndActive(clinicId).orElseThrow(() -> new EntityNotFoundException("Veterinary clinic not found with ID: " + clinicId));
    }

    @Override
    public List<VeterinaryClinic> searchByName(String query) {
        veterinaryClinicValidator.validateSearchQuery(query);
        return veterinaryClinicRepository.findByNameContainingIgnoreCase(query);
    }

    @Override
    public VeterinaryClinic findByVeterinarianId(Long veterinarianId) {
        veterinaryClinicValidator.validateId(veterinarianId);
        VeterinaryClinic clinic = veterinaryClinicRepository.findByVeterinarianId(veterinarianId);
        if (clinic == null) {
            throw new EntityNotFoundException("Veterinary clinic not found for veterinarian ID: " + veterinarianId);
        }
        return clinic;
    }

    @Override
    public VeterinaryClinic findByReceptionistId(Long receptionistId) {
        veterinaryClinicValidator.validateId(receptionistId);
        return veterinaryClinicRepository.findByReceptionistId(receptionistId).orElseThrow(() -> new EntityNotFoundException("Veterinary clinic not found for receptionist ID: " + receptionistId));
    }

    @Override
    @Transactional
    public VeterinaryClinic save(VeterinaryClinic veterinaryClinic) {
        veterinaryClinicValidator.validateForCreation(veterinaryClinic);
        return veterinaryClinicRepository.save(veterinaryClinic);
    }

    @Override
    @Transactional
    public VeterinaryClinic update(VeterinaryClinic veterinaryClinic) {
        veterinaryClinicValidator.validateForUpdate(veterinaryClinic);
        VeterinaryClinic existingClinic = findById(veterinaryClinic.getId());
        updateClinicFields(existingClinic, veterinaryClinic);
        return veterinaryClinicRepository.save(existingClinic);
    }

    @Override
    @Transactional
    public void deactivate(Long clinicId) {
        VeterinaryClinic existingClinic = findById(clinicId);
        existingClinic.setActive(false);
        veterinaryClinicRepository.save(existingClinic);
    }

    private void updateClinicFields(VeterinaryClinic existing, VeterinaryClinic updated) {
        existing.setVeterinarian(updated.getVeterinarian());
        existing.setName(updated.getName());
        existing.setAddress(updated.getAddress());
        existing.setEmail(updated.getEmail());
        existing.setPhone(updated.getPhone());
    }

}
