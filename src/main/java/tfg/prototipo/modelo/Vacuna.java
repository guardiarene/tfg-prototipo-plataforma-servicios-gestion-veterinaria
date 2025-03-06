package tfg.prototipo.modelo;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@Entity
@Table(name = "VACUNA")
public class Vacuna {

    @Version
    private Integer version;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Long id;

    @Column(name = "FECHA_APLICACION")
    private LocalDate fechaAplicacion;

    @Column(name = "MARCA")
    private String marca;

    @Column(name = "DOSIS")
    private String dosis;

    @Column(name = "LOTE")
    private String lote;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ID_ANAMNESIS")
    private Anamnesis anamnesis;

}
