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
@Table(name = "ANAMNESIS")
public class Anamnesis {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Long id;

    @Column(name = "ULTIMA_DESPARACITACION")
    private LocalDate ultimaDesparasitacion;

    @OneToMany(mappedBy = "anamnesis", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Vacuna> vacunas = new ArrayList<>();

    @Column(name = "ALIMENTACION")
    private String alimentacion;

    @Enumerated(EnumType.STRING)
    @Column(name = "ESTADO_REPRODUCTIVO")
    private EstadoReproductivo estadoReproductivo;

    @Column(name = "ULTIMO_CELO")
    private LocalDate ultimoCelo;

    @Column(name = "ULTIMO_PARTO")
    private LocalDate ultimoParto;

    @OneToOne
    @JoinColumn(name = "ID_HISTORIA_CLINICA")
    private HistoriaClinica historiaClinica;

    public void actualizarDatos(Anamnesis nuevo) {
        this.ultimaDesparasitacion = nuevo.getUltimaDesparasitacion();
        this.alimentacion = nuevo.getAlimentacion();
        this.estadoReproductivo = nuevo.getEstadoReproductivo();
        this.ultimoCelo = nuevo.getUltimoCelo();
        this.ultimoParto = nuevo.getUltimoParto();
    }

}
