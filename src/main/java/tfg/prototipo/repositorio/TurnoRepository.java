package tfg.prototipo.repositorio;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import tfg.prototipo.modelo.ClinicaVeterinaria;
import tfg.prototipo.modelo.Mascota;
import tfg.prototipo.modelo.Turno;
import tfg.prototipo.modelo.Usuario;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface TurnoRepository extends JpaRepository<Turno, Long> {

    List<Turno> findByClinicaId(Long clinicaId);

    List<Turno> findByCliente_Id(Long idCliente);

    @Query("SELECT DISTINCT t.mascota FROM Turno t WHERE t.clinica IN :clinicas AND t.mascota.activo = true")
    List<Mascota> obtenerMascotasConTurnosEnClinica(@Param("clinicas") List<ClinicaVeterinaria> clinicas);

    @Query("SELECT COUNT(t) > 0 FROM Turno t WHERE t.mascota = :mascota AND t.clinica IN :clinicas AND t.cliente = :cliente")
    boolean existeTurnoPorMascotaYClinicasYCliente(@Param("mascota") Mascota mascota, @Param("clinicas") List<ClinicaVeterinaria> clinicas, @Param("cliente") Usuario cliente);

    @Query("SELECT t.fecha, COUNT(t) FROM Turno t WHERE t.clinica.id = :clinicaId AND t.fecha BETWEEN :inicio AND :fin GROUP BY t.fecha ORDER BY t.fecha ASC")
    List<Object[]> contarTurnosPorFecha(@Param("clinicaId") Long clinicaId, @Param("inicio") LocalDate inicio, @Param("fin") LocalDate fin);

    @Query("SELECT s.nombre, COUNT(t) FROM Turno t JOIN t.servicioMedico s WHERE t.clinica.id = :clinicaId AND t.fecha BETWEEN :inicio AND :fin GROUP BY s.nombre ORDER BY COUNT(t) DESC")
    List<Object[]> contarServiciosSolicitados(@Param("clinicaId") Long clinicaId, @Param("inicio") LocalDate inicio, @Param("fin") LocalDate fin);

}
