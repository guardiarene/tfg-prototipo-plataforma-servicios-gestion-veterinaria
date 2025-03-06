package tfg.prototipo.servicio;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tfg.prototipo.modelo.*;
import tfg.prototipo.repositorio.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class HistoriaClinicaService {

    private final HistoriaClinicaRepository historiaClinicaRepository;

    private final ClinicaVeterinariaRepository clinicaVeterinariaRepository;

    private final MascotaRepository mascotaRepository;

    private final TurnoRepository turnoRepository;

    private final ExamenClinicoRepository examenClinicoRepository;

    private final AnamnesisRepository anamnesisRepository;

    private final TratamientoRepository tratamientoRepository;

    private final DiagnosticoRepository diagnosticoRepository;

    public HistoriaClinica obtenerHistoriaCompletaParaEdicion(Long id) {
        HistoriaClinica historia = historiaClinicaRepository.obtenerHistoriaBase(id)
                .orElseThrow(() -> new EntityNotFoundException("Historia no encontrada"));

        historiaClinicaRepository.obtenerConTratamientos(id)
                .ifPresent(h -> historia.setTratamientos(h.getTratamientos()));
        historiaClinicaRepository.obtenerConDiagnosticos(id)
                .ifPresent(h -> historia.setDiagnosticos(h.getDiagnosticos()));
        historiaClinicaRepository.obtenerConVacunas(id)
                .ifPresent(h -> historia.getAnamnesis().setVacunas(h.getAnamnesis().getVacunas()));

        return historia;
    }

    @Transactional(readOnly = true)
    public HistoriaClinica obtenerHistoriaCompletaPorId(Long clinicaId) {
        HistoriaClinica historiaConVacunas = historiaClinicaRepository.obtenerHistoriaClinicaConVacunas(clinicaId)
                .orElseThrow(() -> new EntityNotFoundException("Historia clínica no encontrada con ID: " + clinicaId));

        HistoriaClinica historiaConTratamientos = historiaClinicaRepository.obtenerHistoriaClinicaConTratamientos(clinicaId)
                .orElseThrow(() -> new EntityNotFoundException("Historia clínica no encontrada con ID: " + clinicaId));

        historiaConVacunas.setTratamientos(historiaConTratamientos.getTratamientos());
        return historiaConVacunas;
    }

    public List<HistoriaClinica> obtenerHistoriasClinicasPorVeterinario(Usuario veterinario) {
        ClinicaVeterinaria clinica = clinicaVeterinariaRepository.obtenerPorIdVeterinario(veterinario.getId());
        return historiaClinicaRepository.obtenerPorIdClinica(clinica.getId());
    }

    @Transactional
    public void guardarHistoriaClinica(HistoriaClinica historia, Usuario veterinario) {
        Mascota mascota = mascotaRepository.obtenerPorId(historia.getPaciente().getId())
                .orElseThrow(() -> new EntityNotFoundException("Mascota no encontrada"));

        historia.setVeterinario(veterinario);
        historia.setUltimoVeterinario(veterinario);
        historia.setVeterinario(veterinario);

        validarTurnosMascota(mascota, veterinario);
        validarHistoriaExistente(mascota);

        configurarRelacionesBase(historia, veterinario, mascota);
        HistoriaClinica historiaGuardada = historiaClinicaRepository.save(historia);
        guardarEntidadesRelacionadas(historiaGuardada);
    }

    @Transactional
    public void actualizarHistoriaClinica(Long id, HistoriaClinica historiaActualizada, Usuario veterinario) {
        HistoriaClinica historiaExistente = historiaClinicaRepository.obtenerHistoriaBase(id)
                .orElseThrow(() -> new EntityNotFoundException("Historia no encontrada"));

        historiaExistente.setUltimoVeterinario(veterinario);

        historiaClinicaRepository.obtenerConTratamientos(id)
                .ifPresent(h -> historiaExistente.setTratamientos(h.getTratamientos()));
        historiaClinicaRepository.obtenerConDiagnosticos(id)
                .ifPresent(h -> historiaExistente.setDiagnosticos(h.getDiagnosticos()));
        historiaClinicaRepository.obtenerConVacunas(id)
                .ifPresent(h -> historiaExistente.getAnamnesis().setVacunas(h.getAnamnesis().getVacunas()));

        historiaExistente.setMotivoConsulta(historiaActualizada.getMotivoConsulta());
        historiaExistente.setFecha(LocalDate.now());

        actualizarExamenClinico(historiaExistente, historiaActualizada.getExamenClinico());
        actualizarAnamnesis(historiaExistente, historiaActualizada.getAnamnesis());
        actualizarTratamientos(historiaExistente, historiaActualizada.getTratamientos());
        actualizarDiagnosticos(historiaExistente, historiaActualizada.getDiagnosticos());

        historiaClinicaRepository.save(historiaExistente);
    }

    private void configurarRelacionesBase(HistoriaClinica historia, Usuario veterinario, Mascota mascota) {
        historia.setFecha(LocalDate.now());
        historia.setVeterinario(veterinario);
        historia.setPropietario(mascota.getPropietario());
        mascota.setHistoriaClinica(historia);
        historia.setPaciente(mascota);
    }

    private void guardarEntidadesRelacionadas(HistoriaClinica historia) {
        HistoriaClinica historiaGuardada = historiaClinicaRepository.save(historia);

        if (historiaGuardada.getExamenClinico() != null) {
            historiaGuardada.getExamenClinico().setHistoriaClinica(historiaGuardada);
            examenClinicoRepository.save(historiaGuardada.getExamenClinico());
        }

        if (historiaGuardada.getAnamnesis() != null) {
            Anamnesis anamnesis = historiaGuardada.getAnamnesis();
            anamnesis.setHistoriaClinica(historiaGuardada);
            anamnesis.getVacunas().forEach(v -> v.setAnamnesis(anamnesis));
            anamnesisRepository.save(anamnesis);
        }

        if (historiaGuardada.getDiagnosticos() != null) {
            historiaGuardada.getDiagnosticos().forEach(d -> d.setHistoriaClinica(historiaGuardada));
            diagnosticoRepository.saveAll(historiaGuardada.getDiagnosticos());
        }

        if (historiaGuardada.getTratamientos() != null) {
            List<Tratamiento> tratamientos = historiaGuardada.getTratamientos().stream()
                    .peek(t -> t.setHistoriaClinica(historiaGuardada))
                    .toList();
            tratamientoRepository.saveAll(tratamientos);
        }
    }

    private void actualizarExamenClinico(HistoriaClinica historiaExistente, ExamenClinico examenActualizado) {
        if (examenActualizado != null) {
            ExamenClinico examenExistente = historiaExistente.getExamenClinico();
            if (examenExistente == null) {
                examenExistente = new ExamenClinico();
                historiaExistente.setExamenClinico(examenExistente);
            }
            BeanUtils.copyProperties(examenActualizado, examenExistente, "id", "version");
        }
    }

    private void actualizarAnamnesis(HistoriaClinica historiaExistente, Anamnesis anamnesisActualizada) {
        if (anamnesisActualizada != null) {
            Anamnesis anamnesisExistente = historiaExistente.getAnamnesis();
            if (anamnesisExistente == null) {
                anamnesisExistente = new Anamnesis();
                historiaExistente.setAnamnesis(anamnesisExistente);
            }
            BeanUtils.copyProperties(anamnesisActualizada, anamnesisExistente, "id", "version", "vacunas", "historiaClinica");
            actualizarVacunas(anamnesisExistente, anamnesisActualizada.getVacunas());
        }
    }

    private void actualizarVacunas(Anamnesis anamnesisExistente, List<Vacuna> nuevasVacunas) {
        Map<Long, Vacuna> vacunasExistentes = anamnesisExistente.getVacunas().stream()
                .collect(Collectors.toMap(Vacuna::getId, Function.identity()));

        List<Vacuna> vacunasActualizadas = nuevasVacunas.stream().map(nuevaVacuna -> {
            if (nuevaVacuna.getId() == null) {
                Vacuna vacuna = new Vacuna();
                BeanUtils.copyProperties(nuevaVacuna, vacuna, "id", "version");
                vacuna.setAnamnesis(anamnesisExistente);
                return vacuna;
            } else {
                Vacuna vacunaExistente = Optional.ofNullable(vacunasExistentes.get(nuevaVacuna.getId()))
                        .orElseThrow(() -> new EntityNotFoundException("Vacuna no encontrada con ID: " + nuevaVacuna.getId()));
                BeanUtils.copyProperties(nuevaVacuna, vacunaExistente, "id", "version", "anamnesis");
                return vacunaExistente;
            }
        }).toList();

        anamnesisExistente.getVacunas().clear();
        anamnesisExistente.getVacunas().addAll(vacunasActualizadas);
    }

    private void actualizarTratamientos(HistoriaClinica historiaExistente, List<Tratamiento> nuevosTratamientos) {
        List<Long> idsNuevosTratamientos = nuevosTratamientos.stream()
                .map(Tratamiento::getId)
                .filter(Objects::nonNull)
                .toList();

        historiaExistente.getTratamientos().removeIf(t ->
                t.getId() != null && !idsNuevosTratamientos.contains(t.getId()));

        nuevosTratamientos.forEach(nuevoTratamiento -> {
            if (nuevoTratamiento.getId() == null) {
                Tratamiento tratamiento = new Tratamiento();
                BeanUtils.copyProperties(nuevoTratamiento, tratamiento, "id", "version");
                tratamiento.setHistoriaClinica(historiaExistente);
                historiaExistente.getTratamientos().add(tratamiento);
            } else {
                Tratamiento tratamientoExistente = historiaExistente.getTratamientos().stream()
                        .filter(t -> t.getId().equals(nuevoTratamiento.getId()))
                        .findFirst()
                        .orElseThrow(() -> new EntityNotFoundException("Tratamiento no encontrado"));
                BeanUtils.copyProperties(nuevoTratamiento, tratamientoExistente, "id", "version", "historiaClinica");
            }
        });
    }

    private void actualizarDiagnosticos(HistoriaClinica historiaExistente, List<Diagnostico> nuevosDiagnosticos) {
        List<Long> idsNuevosDiagnosticos = nuevosDiagnosticos.stream()
                .map(Diagnostico::getId)
                .filter(Objects::nonNull)
                .toList();

        historiaExistente.getDiagnosticos().removeIf(d ->
                d.getId() != null && !idsNuevosDiagnosticos.contains(d.getId()));

        nuevosDiagnosticos.forEach(nuevoDiagnostico -> {
            if (nuevoDiagnostico.getId() == null) {
                Diagnostico diagnostico = new Diagnostico();
                BeanUtils.copyProperties(nuevoDiagnostico, diagnostico, "id", "version");
                diagnostico.setHistoriaClinica(historiaExistente);
                historiaExistente.getDiagnosticos().add(diagnostico);
            } else {
                Diagnostico diagnosticoExistente = historiaExistente.getDiagnosticos().stream()
                        .filter(d -> d.getId().equals(nuevoDiagnostico.getId()))
                        .findFirst()
                        .orElseThrow(() -> new EntityNotFoundException("Diagnóstico no encontrado"));
                BeanUtils.copyProperties(nuevoDiagnostico, diagnosticoExistente, "id", "version", "historiaClinica");
            }
        });
    }

    private void validarTurnosMascota(Mascota mascota, Usuario veterinario) {
        List<ClinicaVeterinaria> clinicasVeterinario = veterinario.getClinicas();
        boolean tieneTurnos = turnoRepository.existeTurnoPorMascotaYClinicasYCliente(
                mascota, clinicasVeterinario, mascota.getPropietario());

        if (!tieneTurnos) {
            throw new IllegalStateException("La mascota no tiene turnos registrados en las clínicas del veterinario");
        }
    }

    private void validarHistoriaExistente(Mascota mascota) {
        if (mascota.getHistoriaClinica() != null) {
            throw new IllegalStateException("La mascota ya tiene una historia clínica registrada");
        }
    }

}
