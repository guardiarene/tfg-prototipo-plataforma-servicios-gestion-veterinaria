package tfg.prototipo.servicio;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import tfg.prototipo.modelo.*;
import tfg.prototipo.repositorio.TurnoRepository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TurnoService {

    private final TurnoRepository turnoRepository;

    private final ClinicaVeterinariaService clinicaService;

    private final ServicioMedicoService servicioService;

    private final MascotaService mascotaService;

    public void crearTurnoCliente(String fechaStr, Long idMascota, Long idServicio, Long idClinica, Usuario cliente) {
        LocalDate fecha = LocalDate.parse(fechaStr);
        Mascota mascota = mascotaService.obtenerMascota(idMascota);
        ServicioMedico servicio = servicioService.obtenerPorId(idServicio);
        ClinicaVeterinaria clinica = clinicaService.obtenerPorId(idClinica);
        Turno turno = new Turno();
        turno.setFecha(fecha);
        turno.setHora(LocalTime.of(0, 0));
        turno.setClinica(clinica);
        turno.setServicioMedico(servicio);
        turno.setCliente(cliente);
        turno.setMascota(mascota);
        turno.setEstado(EstadoTurno.PENDIENTE);
        turnoRepository.save(turno);
    }

    public List<Turno> obtenerTurnosPorCliente(Long idCliente) {
        return turnoRepository.findByCliente_Id(idCliente);
    }

    public Turno obtenerTurnoConDetalles(Long idTurno) {
        return turnoRepository.findById(idTurno)
                .orElseThrow(() -> new EntityNotFoundException("Turno no encontrado"));
    }

    public String obtenerNombreVeterinario(Turno turno) {
        return Optional.ofNullable(turno.getServicioMedico())
                .map(ServicioMedico::getClinica)
                .map(ClinicaVeterinaria::getVeterinario)
                .map(Usuario::getNombre)
                .orElse("Veterinario no asignado");
    }

    public void actualizarEstadoTurno(Long idTurno, EstadoTurno estado) {
        Turno turno = obtenerTurnoPorId(idTurno);
        turno.setEstado(estado);
        turnoRepository.save(turno);
    }

    public void cancelarTurno(Long idTurno) {
        actualizarEstadoTurno(idTurno, EstadoTurno.CANCELADO);
    }

    public void reprogramarTurno(Long idTurno, Turno turnoActualizado) {
        Turno turno = obtenerTurnoPorId(idTurno);
        turno.setFecha(turnoActualizado.getFecha());
        turno.setHora(turnoActualizado.getHora());
        turno.setServicioMedico(servicioService.obtenerPorId(turnoActualizado.getServicioMedico().getId()));
        turnoRepository.save(turno);
    }

    public void crearTurnoRecepcionista(Turno turno, Long servicioId, Long idRecepcionista) {
        ClinicaVeterinaria clinica = clinicaService.obtenerClinicaPorRecepcionista(idRecepcionista);
        ServicioMedico servicio = servicioService.obtenerPorId(servicioId);

        turno.setClinica(clinica);
        turno.setServicioMedico(servicio);
        turno.setEstado(EstadoTurno.CONFIRMADO);

        turnoRepository.save(turno);
    }

    public List<Turno> obtenerPorClinica(Long idClinica) {
        return turnoRepository.findByClinicaId(idClinica);
    }

    public Turno obtenerTurnoPorId(Long id) {
        return turnoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Turno no encontrado"));
    }

}
