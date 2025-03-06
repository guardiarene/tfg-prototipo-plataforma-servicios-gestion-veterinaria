package tfg.prototipo.modelo;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "CLINICA_VETERINARIA")
public class ClinicaVeterinaria {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Long id;

    @Column(name = "NOMBRE")
    private String nombre;

    @Column(name = "DIRECCION")
    private String direccion;

    @Column(name = "TELEFONO")
    private String telefono;

    @Column(name = "EMAIL")
    private String email;

    @OneToMany(mappedBy = "clinica", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<ServicioMedico> servicios = new ArrayList<>();

    @OneToMany(mappedBy = "clinica", cascade = CascadeType.ALL)
    private List<Turno> turnos = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "ID_VETERINARIO")
    private Usuario veterinario;

    @OneToOne
    @JoinColumn(name = "ID_RECEPCIONISTA")
    private Usuario recepcionista;

    @Column(name = "ACTIVO", nullable = false)
    private Boolean activo = true;

}
