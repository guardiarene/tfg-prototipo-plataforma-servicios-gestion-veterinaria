package tfg.prototipo.modelo;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "DIAGNOSTICO")
public class Diagnostico {

    @Version
    private Integer version;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Long id;

    @ElementCollection
    @CollectionTable(name = "DIAGNOSTICO_PROBLEMAS", joinColumns = @JoinColumn(name = "ID_DIAGNOSTICO"))
    @Column(name = "PROBLEMA")
    private List<String> problemas = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ID_HISTORIA_CLINICA")
    private HistoriaClinica historiaClinica;

}
