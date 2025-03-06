package tfg.prototipo.modelo;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "EXAMEN_CLINICO")
public class ExamenClinico {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Long id;

    @Column(name = "FRECUENCIA_RESPIRATORIA")
    private Integer frecuenciaRespiratoria;

    @Column(name = "FRECUENCIA_CARDIACA")
    private Integer frecuenciaCardiaca;

    @Column(name = "TEMPERATURA")
    private Float temperatura;

    @Column(name = "PULSO")
    private Integer pulso;

    @Column(name = "MUCOSAS")
    private String mucosas;

    @Enumerated(EnumType.STRING)
    @Column(name = "TEMPERAMENTO")
    private Temperamento temperamento;

    @Column(name = "DESCRIPCION")
    private String descripcion;

    @OneToOne(mappedBy = "examenClinico", cascade = CascadeType.ALL)
    private HistoriaClinica historiaClinica;

    public void actualizarDatos(ExamenClinico nuevo) {
        this.frecuenciaRespiratoria = nuevo.getFrecuenciaRespiratoria();
        this.frecuenciaCardiaca = nuevo.getFrecuenciaCardiaca();
        this.temperatura = nuevo.getTemperatura();
        this.pulso = nuevo.getPulso();
        this.mucosas = nuevo.getMucosas();
        this.temperamento = nuevo.getTemperamento();
        this.descripcion = nuevo.getDescripcion();
    }

}
