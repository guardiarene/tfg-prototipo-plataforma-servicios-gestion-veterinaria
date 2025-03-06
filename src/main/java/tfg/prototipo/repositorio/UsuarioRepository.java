package tfg.prototipo.repositorio;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tfg.prototipo.modelo.Rol;
import tfg.prototipo.modelo.Usuario;

import java.util.List;
import java.util.Optional;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    Optional<Usuario> findByEmail(String email);

    List<Usuario> findByTipoRolAndActivo(Rol tipoRol, Boolean activo);

}
