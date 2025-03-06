package tfg.prototipo.servicio;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import tfg.prototipo.modelo.EstadoTurno;
import tfg.prototipo.modelo.Turno;
import tfg.prototipo.repositorio.TurnoRepository;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class TurnoService {

    private final TurnoRepository turnoRepository;

    public List<Turno> obtenerPorCliente(Long idCliente) {
        return turnoRepository.findByCliente_Id(idCliente);
    }

    public void crearTurno(Turno turno) {
        turnoRepository.save(turno);
    }

    public Optional<Turno> obtenerPorId(Long id) {
        return turnoRepository.findById(id);
    }

    public List<Turno> obtenerPorClinica(Long idClinica) {
        return turnoRepository.findByClinicaId(idClinica);
    }

    public void actualizarEstado(Long idTurno, EstadoTurno estado) {
        Turno turno = turnoRepository.findById(idTurno)
                .orElseThrow(() -> new EntityNotFoundException("Turno no encontrado"));

        turno.setEstado(estado);
        turnoRepository.save(turno);
    }

    public void actualizarTurno(Turno turno) {
        turnoRepository.save(turno);
    }

}
