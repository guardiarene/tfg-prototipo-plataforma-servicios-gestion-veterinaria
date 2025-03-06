package tfg.prototipo.modelo;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "SERVICIO_MEDICO")
public class ServicioMedico {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Long id;

    @Column(name = "NOMBRE")
    private String nombre;

    @Column(name = "DESCRIPCION")
    private String descripcion;

    @ManyToOne
    @JoinColumn(name = "ID_CLINICA_VETERINARIA")
    private ClinicaVeterinaria clinica;

    @OneToMany(mappedBy = "servicioMedico", cascade = CascadeType.ALL)
    private List<Turno> turnos = new ArrayList<>();

    @Column(name = "ACTIVO", nullable = false)
    private Boolean activo = true;

}
