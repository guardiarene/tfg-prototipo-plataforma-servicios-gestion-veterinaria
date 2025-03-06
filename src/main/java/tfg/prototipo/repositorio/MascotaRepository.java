package tfg.prototipo.repositorio;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import tfg.prototipo.modelo.Mascota;

import java.util.List;
import java.util.Optional;

@Repository
public interface MascotaRepository extends JpaRepository<Mascota, Long> {

    @Query("SELECT m FROM Mascota m WHERE m.propietario.id = :propietarioId AND m.activo = TRUE")
    List<Mascota> obtenerPorPropietario(@Param("propietarioId") Long propietarioId);

    @Query("SELECT m FROM Mascota m WHERE m.id = :mascotaId AND m.activo = true")
    Optional<Mascota> obtenerPorId(@Param("mascotaId") Long mascotaId);

}
