package tfg.psygcv.repository.base;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tfg.psygcv.entity.medical.Anamnesis;

@Repository
public interface AnamnesisRepository extends JpaRepository<Anamnesis, Long> {}
