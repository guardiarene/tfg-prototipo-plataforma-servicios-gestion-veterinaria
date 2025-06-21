package tfg.psygcv.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import tfg.psygcv.model.appointment.Appointment;
import tfg.psygcv.model.appointment.AppointmentStatus;
import tfg.psygcv.model.clinic.VeterinaryClinic;
import tfg.psygcv.model.user.User;
import tfg.psygcv.service.impl.AppointmentServiceImpl;
import tfg.psygcv.service.impl.VeterinaryClinicServiceImpl;

import java.util.List;

@RequiredArgsConstructor
@Controller
@RequestMapping("/recepcionista")
public class RecepcionistaController {

    private final AppointmentServiceImpl appointmentServiceImpl;

    private final VeterinaryClinicServiceImpl veterinaryClinicServiceImpl;

    @GetMapping("/interfaz_recepcionista")
    public String mostrarInterfazRecepcionista(Model model, Authentication authentication) {
        User recepcionista = (User) authentication.getPrincipal();

        VeterinaryClinic clinica = veterinaryClinicServiceImpl.findByReceptionistId(recepcionista.getId());
        List<Appointment> appointments = appointmentServiceImpl.findByClinicId(clinica.getId());

        model.addAttribute("estadosTurno", AppointmentStatus.values());
        model.addAttribute("appointments", appointments);
        return "recepcionista/interfaz_recepcionista";
    }

}
