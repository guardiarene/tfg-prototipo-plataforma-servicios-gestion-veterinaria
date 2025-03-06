package tfg.prototipo.modelo;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "HISTORIA_CLINICA")
public class HistoriaClinica {

    @Version
    private Integer version;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Long id;

    @Column(name = "FECHA")
    private LocalDate fecha;

    @Column(name = "MOTIVO_CONSULTA")
    private String motivoConsulta;

    @ManyToOne
    @JoinColumn(name = "ID_USUARIO")
    private Usuario propietario;

    @OneToOne
    @JoinColumn(name = "ID_MASCOTA")
    private Mascota paciente;

    @ManyToOne
    @JoinColumn(name = "ID_VETERINARIO")
    private Usuario veterinario;

    @ManyToOne
    @JoinColumn(name = "ID_ULTIMO_VETERINARIO")
    private Usuario ultimoVeterinario;

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "ID_EXAMEN_CLINICO")
    private ExamenClinico examenClinico;

    @OneToOne(mappedBy = "historiaClinica", cascade = CascadeType.ALL, orphanRemoval = true)
    private Anamnesis anamnesis;

    @OneToMany(mappedBy = "historiaClinica", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Diagnostico> diagnosticos = new ArrayList<>();

    @OneToMany(mappedBy = "historiaClinica", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Tratamiento> tratamientos = new ArrayList<>();

}
