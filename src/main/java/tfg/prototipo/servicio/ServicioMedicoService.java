package tfg.prototipo.servicio;

import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import tfg.prototipo.modelo.ClinicaVeterinaria;
import tfg.prototipo.modelo.ServicioMedico;
import tfg.prototipo.modelo.Usuario;
import tfg.prototipo.repositorio.ServicioMedicoRepository;

import java.util.List;
import java.util.NoSuchElementException;

@RequiredArgsConstructor
@Service
public class ServicioMedicoService {

    private final ServicioMedicoRepository servicioMedicoRepository;

    private final ClinicaVeterinariaService clinicaVeterinariaService;

    public List<ServicioMedico> obtenerPorClinica(Long idClinca) {
        return servicioMedicoRepository.obtenerPorIdClinica(idClinca);
    }

    public ServicioMedico obtenerPorId(Long idServicio) {
        return servicioMedicoRepository.obtenerPorId(idServicio)
                .orElseThrow(() -> new IllegalArgumentException("Servicio no encontrado con ID: " + idServicio));
    }

    public List<ServicioMedico> obtenerServiciosPorClinicaVeterinario(Usuario veterinario) {
        ClinicaVeterinaria clinica = clinicaVeterinariaService.obtenerClinicaPorVeterinario(veterinario.getId());
        return servicioMedicoRepository.obtenerPorIdClinica(clinica.getId());
    }

    public ServicioMedico obtenerServicioYValidarClinica(Long idServicio, Long idClinica) {
        ServicioMedico servicio = servicioMedicoRepository.obtenerPorId(idServicio)
                .orElseThrow(() -> new NoSuchElementException("Servicio no encontrado con ID: " + idServicio));

        if (!servicio.getClinica().getId().equals(idClinica)) {
            throw new AccessDeniedException("No se pudo validar el servicio y la clínica");
        }

        return servicio;
    }

    public void crearServicio(ServicioMedico servicio, Long idClinica) {
        ClinicaVeterinaria clinica = clinicaVeterinariaService.obtenerPorId(idClinica)
                .orElseThrow(() -> new IllegalArgumentException("Clínica no válida"));
        servicio.setClinica(clinica);

        servicioMedicoRepository.save(servicio);
    }

    public void actualizarServicio(Long servicioId, ServicioMedico servicioActualizado, Long clinicaId) {
        ServicioMedico servicioExistente = obtenerServicioYValidarClinica(servicioId, clinicaId);
        servicioExistente.setNombre(servicioActualizado.getNombre());
        servicioExistente.setDescripcion(servicioActualizado.getDescripcion());

        servicioMedicoRepository.save(servicioExistente);
    }

    public void eliminarServicio(Long servicioId, Long clinicaId) {
        ServicioMedico servicio = obtenerServicioYValidarClinica(servicioId, clinicaId);
        servicio.setActivo(false);

        servicioMedicoRepository.save(servicio);
    }

}
