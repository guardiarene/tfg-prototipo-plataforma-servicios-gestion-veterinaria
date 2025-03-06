package tfg.prototipo.repositorio;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tfg.prototipo.modelo.Vacuna;

@Repository
public interface VacunaRepository extends JpaRepository<Vacuna, Long> {
}
