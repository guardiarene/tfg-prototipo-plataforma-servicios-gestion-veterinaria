package tfg.prototipo.modelo;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@Entity
@Table(name = "TRATAMIENTO")
public class Tratamiento {

    @Version
    private Integer version;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Long id;

    @Column(name = "PRODUCTO")
    private String producto;

    @Column(name = "VIA")
    private String via;

    @Column(name = "FRECUENCIA")
    private String frecuencia;

    @Column(name = "FECHA_INICIO")
    private LocalDate fechaInicio;

    @Column(name = "FECHA_FIN")
    private LocalDate fechaFin;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ID_HISTORIA_CLINICA")
    private HistoriaClinica historiaClinica;

}
