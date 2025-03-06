package tfg.prototipo.servicio;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import tfg.prototipo.modelo.ClinicaVeterinaria;
import tfg.prototipo.repositorio.ClinicaVeterinariaRepository;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class ClinicaVeterinariaService {

    private final ClinicaVeterinariaRepository clinicaVeterinariaRepository;

    public List<ClinicaVeterinaria> obtenerTodas() {
        return clinicaVeterinariaRepository.obtenerTodas();
    }

    public List<ClinicaVeterinaria> buscarPorNombre(String query) {
        return clinicaVeterinariaRepository.findByNombreContainingIgnoreCase(query);
    }

    public Optional<ClinicaVeterinaria> obtenerPorId(Long idClinica) {
        return clinicaVeterinariaRepository.obtenerPorId(idClinica);
    }

    public void registrar(ClinicaVeterinaria clinicaVeterinaria) {
        clinicaVeterinariaRepository.save(clinicaVeterinaria);
    }

    public void actualizar(ClinicaVeterinaria clinicaVeterinaria) {
        ClinicaVeterinaria clinicaExistente = clinicaVeterinariaRepository
                .findById(clinicaVeterinaria.getId())
                .orElseThrow(() -> new RuntimeException("Clínica no encontrada"));

        clinicaExistente.setVeterinario(clinicaVeterinaria.getVeterinario());
        clinicaExistente.setNombre(clinicaVeterinaria.getNombre());
        clinicaExistente.setDireccion(clinicaVeterinaria.getDireccion());
        clinicaExistente.setEmail(clinicaVeterinaria.getEmail());
        clinicaExistente.setTelefono(clinicaVeterinaria.getTelefono());

        clinicaVeterinariaRepository.save(clinicaExistente);
    }

    public void darDeBaja(Long idClinica) {
        ClinicaVeterinaria clinicaExistente = clinicaVeterinariaRepository.obtenerPorId(idClinica).orElseThrow();
        clinicaExistente.setActivo(false);

        clinicaVeterinariaRepository.save(clinicaExistente);
    }

    public ClinicaVeterinaria obtenerClinicaPorVeterinario(Long idVeterinario) {
        return clinicaVeterinariaRepository.obtenerPorIdVeterinario(idVeterinario);
    }

    public ClinicaVeterinaria obtenerClinicaPorRecepcionista(Long idRecepcionista) {
        return clinicaVeterinariaRepository.findByRecepcionistaId(idRecepcionista).orElseThrow(() -> new EntityNotFoundException("Clínica no encontrada"));
    }

}
