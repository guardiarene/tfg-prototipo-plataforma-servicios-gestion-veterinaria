package tfg.prototipo.servicio;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import tfg.prototipo.modelo.ClinicaVeterinaria;
import tfg.prototipo.modelo.Mascota;
import tfg.prototipo.modelo.Usuario;
import tfg.prototipo.repositorio.MascotaRepository;
import tfg.prototipo.repositorio.TurnoRepository;

import java.util.List;
import java.util.NoSuchElementException;

@RequiredArgsConstructor
@Service
public class MascotaService {

    private final MascotaRepository mascotaRepository;

    private final UsuarioService usuarioService;

    private final TurnoRepository turnoRepository;

    public List<Mascota> obtenerMascotasPorCliente(Long idCliente) {
        return mascotaRepository.obtenerPorPropietario(idCliente);
    }

    public Mascota obtenerMascota(Long idMascota) {
        return mascotaRepository.obtenerPorId(idMascota).orElseThrow(() -> new NoSuchElementException("Mascota no encontrada con ID: " + idMascota));
    }

    public List<Mascota> obtenerMascotasConTurnosEnClinica(Usuario veterinario) {
        List<ClinicaVeterinaria> clinicas = veterinario.getClinicas();
        return turnoRepository.obtenerMascotasConTurnosEnClinica(clinicas);
    }

    public void guardarMascota(Mascota mascota, Long idCliente) {
        Usuario cliente = usuarioService.obtenerPorId(idCliente);
        mascota.setPropietario(cliente);

        mascotaRepository.save(mascota);
    }

    public void actualizarMascota(Long idMascota, Mascota mascota) {
        Mascota mascotaActualizada = obtenerMascota(idMascota);
        mascotaActualizada.setNombre(mascota.getNombre());
        mascotaActualizada.setSexo(mascota.getSexo());
        mascotaActualizada.setRaza(mascota.getRaza());
        mascotaActualizada.setEspecie(mascota.getEspecie());
        mascotaActualizada.setFechaNacimiento(mascota.getFechaNacimiento());
        mascotaActualizada.setPeso(mascota.getPeso());

        mascotaRepository.save(mascotaActualizada);
    }

    public void eliminarMascota(Long idMascota) {
        Mascota mascotaExistente = mascotaRepository.obtenerPorId(idMascota).orElseThrow();
        mascotaExistente.setActivo(false);

        mascotaRepository.save(mascotaExistente);
    }

}
