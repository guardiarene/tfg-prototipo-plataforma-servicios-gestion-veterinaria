package tfg.prototipo.controlador;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import tfg.prototipo.configuracion.util.JsonUtil;
import tfg.prototipo.modelo.Usuario;
import tfg.prototipo.servicio.EstadisticaService;

import java.time.LocalDate;
import java.util.LinkedHashMap;
import java.util.Map;

@RequiredArgsConstructor
@Controller
@RequestMapping("/reportes")
public class ReporteController {

    private final EstadisticaService estadisticaService;

    private final JsonUtil jsonUtil;

    @GetMapping("/enfermedades_tratamientos")
    public String mostrarFormularioReporte(Model model) {
        model.addAttribute("fechaInicio", LocalDate.now().minusMonths(1));
        model.addAttribute("fechaFin", LocalDate.now());
        model.addAttribute("enfermedades", new LinkedHashMap<>());
        model.addAttribute("tratamientos", new LinkedHashMap<>());
        model.addAttribute("enfermedadesJson", "{}");
        model.addAttribute("tratamientosJson", "{}");

        return "reportes/enfermedades_tratamientos";
    }

    @PostMapping("/generar_enfermedades_tratamientos")
    public String generarReporte(@RequestParam LocalDate fechaInicio,
                                 @RequestParam LocalDate fechaFin,
                                 Authentication authentication,
                                 Model model) {
        Usuario veterinario = (Usuario) authentication.getPrincipal();

        Map<String, Long> enfermedades = estadisticaService.obtenerEnfermedadesComunes(veterinario, fechaInicio, fechaFin);
        Map<String, Long> tratamientos = estadisticaService.obtenerTratamientosFrecuentes(veterinario, fechaInicio, fechaFin);

        model.addAttribute("enfermedades", enfermedades);
        model.addAttribute("tratamientos", tratamientos);
        model.addAttribute("enfermedadesJson", jsonUtil.toJson(enfermedades));
        model.addAttribute("tratamientosJson", jsonUtil.toJson(tratamientos));
        model.addAttribute("fechaInicio", fechaInicio);
        model.addAttribute("fechaFin", fechaFin);

        return "reportes/enfermedades_tratamientos";
    }

    @GetMapping("/solicitudes_servicios")
    public String mostrarFormularioSolicitudes(Model model) {
        model.addAttribute("fechaInicio", LocalDate.now().minusMonths(1));
        model.addAttribute("fechaFin", LocalDate.now());
        return "reportes/solicitudes_servicios";
    }

    @PostMapping("/generar_solicitudes_servicios")
    public String generarReporteSolicitudes(@RequestParam LocalDate fechaInicio,
                                            @RequestParam LocalDate fechaFin,
                                            Authentication authentication,
                                            Model model) {
        Usuario veterinario = (Usuario) authentication.getPrincipal();

        Map<LocalDate, Long> solicitudes = estadisticaService.obtenerSolicitudesPorFecha(veterinario, fechaInicio, fechaFin);
        Map<String, Long> servicios = estadisticaService.obtenerServiciosSolicitados(veterinario, fechaInicio, fechaFin);

        model.addAttribute("solicitudes", solicitudes);
        model.addAttribute("servicios", servicios);
        model.addAttribute("solicitudesJson", jsonUtil.toJson(solicitudes));
        model.addAttribute("serviciosJson", jsonUtil.toJson(servicios));
        model.addAttribute("fechaInicio", fechaInicio);
        model.addAttribute("fechaFin", fechaFin);

        return "reportes/solicitudes_servicios";
    }

}
