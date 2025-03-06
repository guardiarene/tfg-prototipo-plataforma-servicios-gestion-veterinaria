package tfg.prototipo.servicio;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import tfg.prototipo.modelo.ClinicaVeterinaria;
import tfg.prototipo.modelo.Usuario;
import tfg.prototipo.repositorio.ClinicaVeterinariaRepository;
import tfg.prototipo.repositorio.DiagnosticoRepository;
import tfg.prototipo.repositorio.TratamientoRepository;
import tfg.prototipo.repositorio.TurnoRepository;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class EstadisticaService {

    private final DiagnosticoRepository diagnosticoRepository;

    private final TratamientoRepository tratamientoRepository;

    private final ClinicaVeterinariaRepository clinicaVeterinariaRepository;

    private final TurnoRepository turnoRepository;

    private ClinicaVeterinaria obtenerClinica(Usuario veterinario) {
        return clinicaVeterinariaRepository.obtenerPorIdVeterinario(veterinario.getId());
    }

    public Map<String, Long> obtenerEnfermedadesComunes(Usuario veterinario, LocalDate inicio, LocalDate fin) {
        List<String> problemas = diagnosticoRepository.obtenerProblemasFrecuentesPorClinicaYFecha(
                obtenerClinica(veterinario).getId(), inicio, fin);
        return procesarProblemas(problemas);
    }

    public Map<String, Long> obtenerTratamientosFrecuentes(Usuario veterinario, LocalDate inicio, LocalDate fin) {
        List<String> tratamientos = tratamientoRepository.obtenerTratamientosFrecuentesPorClinicaYFecha(
                obtenerClinica(veterinario).getId(), inicio, fin);
        return procesarTratamientos(tratamientos);
    }

    private Map<String, Long> procesarProblemas(List<String> problemas) {
        return problemas.stream()
                .flatMap(p -> Arrays.stream(p.split(",\\s*")))
                .filter(p -> !p.isBlank())
                .map(String::trim)
                .map(String::toLowerCase)
                .collect(Collectors.groupingBy(p -> p, Collectors.counting()))
                .entrySet().stream()
                .sorted(Map.Entry.<String, Long>comparingByValue().reversed())
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e1, LinkedHashMap::new));
    }

    private Map<String, Long> procesarTratamientos(List<String> tratamientos) {
        return tratamientos.stream()
                .filter(t -> !t.isBlank())
                .map(String::trim)
                .map(String::toLowerCase)
                .collect(Collectors.groupingBy(t -> t, Collectors.counting()))
                .entrySet().stream()
                .sorted(Map.Entry.<String, Long>comparingByValue().reversed())
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e1, LinkedHashMap::new));
    }

    public Map<LocalDate, Long> obtenerSolicitudesPorFecha(Usuario veterinario, LocalDate inicio, LocalDate fin) {
        return turnoRepository.contarTurnosPorFecha(obtenerClinica(veterinario).getId(), inicio, fin)
                .stream()
                .collect(Collectors.toMap(tuple -> (LocalDate) tuple[0], tuple -> (Long) tuple[1], (v1, v2) -> v1, TreeMap::new));
    }

    public Map<String, Long> obtenerServiciosSolicitados(Usuario veterinario, LocalDate inicio, LocalDate fin) {
        return turnoRepository.contarServiciosSolicitados(obtenerClinica(veterinario).getId(), inicio, fin)
                .stream()
                .collect(Collectors.toMap(tuple -> (String) tuple[0], tuple -> (Long) tuple[1], (v1, v2) -> v1, LinkedHashMap::new));
    }

}
