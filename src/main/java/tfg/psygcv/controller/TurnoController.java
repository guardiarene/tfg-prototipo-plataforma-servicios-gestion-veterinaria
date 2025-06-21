package tfg.psygcv.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import tfg.psygcv.model.appointment.Appointment;
import tfg.psygcv.model.appointment.AppointmentStatus;
import tfg.psygcv.model.clinic.MedicalService;
import tfg.psygcv.model.clinic.VeterinaryClinic;
import tfg.psygcv.model.pet.Pet;
import tfg.psygcv.model.user.Role;
import tfg.psygcv.model.user.User;
import tfg.psygcv.service.impl.*;

import java.util.List;

@RequiredArgsConstructor
@Controller
@RequestMapping("/turnos")
public class TurnoController {

    private final VeterinaryClinicServiceImpl veterinaryClinicServiceImpl;

    private final MedicalServiceServiceImpl medicalServiceServiceImpl;

    private final PetServiceImpl petServiceImpl;

    private final AppointmentServiceImpl appointmentServiceImpl;

    private final UserServiceImpl userServiceImpl;

    @GetMapping("/solicitar_turno/{id}")
    public String mostrarFormularioTurno(@PathVariable Long id, Model model, Authentication authentication) {
        User cliente = (User) authentication.getPrincipal();
        VeterinaryClinic clinica = veterinaryClinicServiceImpl.findById(id);
        List<MedicalService> servicios = medicalServiceServiceImpl.findByClinicId(id);
        List<Pet> pets = petServiceImpl.findByOwnerId(cliente.getId());

        model.addAttribute("clinicaVeterinaria", clinica);
        model.addAttribute("servicios", servicios);
        model.addAttribute("pets", pets);
        return "turnos/solicitar_turno";
    }

    @PostMapping("/solicitar_turno/{id}")
    public String solicitarTurno(@RequestParam("fecha") String fecha,
                                 @RequestParam("mascota") Long idMascota,
                                 @RequestParam("service") Long idServicioMedico,
                                 @RequestParam("id") Long idClinica,
                                 Authentication authentication) {
        User cliente = (User) authentication.getPrincipal();
        appointmentServiceImpl.createClientAppointment(fecha, idMascota, idServicioMedico, idClinica, cliente);
        return "redirect:/turnos/mis_turnos";
    }

    @GetMapping("/mis_turnos")
    public String mostrarMisTurnos(Model model, Authentication authentication) {
        User cliente = (User) authentication.getPrincipal();
        model.addAttribute("turnos", appointmentServiceImpl.findByCustomerId(cliente.getId()));
        return "turnos/mis_turnos";
    }

    @GetMapping("/detalle_turno/{id}")
    public String mostrarDetalleTurno(@PathVariable Long id, Model model, Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        Appointment appointment = appointmentServiceImpl.findWithDetails(id);

        model.addAttribute("nombreVeterinario", appointmentServiceImpl.findVeterinarianName(appointment));
        model.addAttribute("appointment", appointment);

        return user.getRole() == Role.RECEPTIONIST
                ? "recepcionista/detalle_turno"
                : "turnos/detalle_turno";
    }

    @PostMapping("/actualizar_estado/{id}")
    public String actualizarEstadoTurno(@PathVariable Long id, @RequestParam("estado") AppointmentStatus estado) {
        appointmentServiceImpl.updateStatus(id, estado);
        return "redirect:/recepcionista/interfaz_recepcionista";
    }

    @PostMapping("/cancelar_turno/{id}")
    public String cancelarTurno(@PathVariable Long id) {
        appointmentServiceImpl.cancel(id);
        return "redirect:/recepcionista/interfaz_recepcionista";
    }

    @GetMapping("/reprogramar_turno/{id}")
    public String mostrarFormularioReprogramacion(@PathVariable Long id,
                                                  Model model,
                                                  Authentication authentication) {
        User recepcionista = (User) authentication.getPrincipal();
        VeterinaryClinic clinica = veterinaryClinicServiceImpl.findByReceptionistId(recepcionista.getId());

        model.addAttribute("turno", appointmentServiceImpl.findById(id));
        model.addAttribute("servicios", medicalServiceServiceImpl.findByClinicId(clinica.getId()));
        model.addAttribute("veterinarios", clinica.getVeterinarian());

        return "recepcionista/reprogramar_turno";
    }

    @PostMapping("/reprogramar_turno/{id}")
    public String procesarReprogramacion(@PathVariable Long id,
                                         @ModelAttribute("turno") Appointment appointmentActualizado) {
        appointmentServiceImpl.reschedule(id, appointmentActualizado);
        return "redirect:/recepcionista/interfaz_recepcionista";
    }

    @GetMapping("/agendar_turno")
    public String mostrarFormularioNuevoTurno(@RequestParam(required = false) Long clienteId,
                                              Model model,
                                              Authentication authentication) {
        User recepcionista = (User) authentication.getPrincipal();
        VeterinaryClinic clinica = veterinaryClinicServiceImpl.findByReceptionistId(recepcionista.getId());

        model.addAttribute("clienteId", clienteId);
        model.addAttribute("clientes", userServiceImpl.findActiveCustomers());
        model.addAttribute("mascotas", clienteId != null ? petServiceImpl.findByOwnerId(clienteId) : List.of());
        model.addAttribute("servicios", medicalServiceServiceImpl.findByClinicId(clinica.getId()));
        model.addAttribute("veterinarios", clinica.getVeterinarian());
        model.addAttribute("turno", new Appointment());

        return "recepcionista/agendar_turno";
    }

    @PostMapping("/agendar_turno")
    public String procesarNuevoTurno(@ModelAttribute("appointment") Appointment appointment,
                                     @RequestParam("servicioId") Long servicioId,
                                     Authentication authentication) {
        User recepcionista = (User) authentication.getPrincipal();
        appointmentServiceImpl.createReceptionistAppointment(appointment, servicioId, recepcionista.getId());
        return "redirect:/recepcionista/interfaz_recepcionista";
    }

}
