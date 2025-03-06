package tfg.prototipo.repositorio;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tfg.prototipo.modelo.Anamnesis;

@Repository
public interface AnamnesisRepository extends JpaRepository<Anamnesis, Long> {
}
