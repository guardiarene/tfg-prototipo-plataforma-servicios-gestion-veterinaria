package tfg.prototipo.repositorio;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tfg.prototipo.modelo.ExamenClinico;

@Repository
public interface ExamenClinicoRepository extends JpaRepository<ExamenClinico, Long> {
}
