package tfg.psygcv.repository.medical;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tfg.psygcv.entity.medical.Anamnesis;

@Repository
public interface AnamnesisRepository extends JpaRepository<Anamnesis, Long> {}
