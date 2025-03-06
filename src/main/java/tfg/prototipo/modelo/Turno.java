package tfg.prototipo.modelo;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@Setter
@Entity
@Table(name = "TURNO")
public class Turno {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Long id;

    @Column(name = "FECHA")
    private LocalDate fecha;

    @Column(name = "HORA")
    private LocalTime hora;

    @ManyToOne
    @JoinColumn(name = "ID_CLINICA_VETERINARIA")
    private ClinicaVeterinaria clinica;

    @ManyToOne
    @JoinColumn(name = "ID_SERVICIO_MEDICO")
    private ServicioMedico servicioMedico;

    @ManyToOne
    @JoinColumn(name = "ID_CLIENTE")
    private Usuario cliente;

    @Enumerated(EnumType.STRING)
    @Column(name = "ESTADO")
    private EstadoTurno estado;

    @ManyToOne
    @JoinColumn(name = "ID_MASCOTA")
    private Mascota mascota;

    @Column(name = "ACTIVO", nullable = false)
    private Boolean activo = true;

}
