package tfg.prototipo.servicio;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import tfg.prototipo.modelo.Rol;
import tfg.prototipo.modelo.Usuario;
import tfg.prototipo.repositorio.UsuarioRepository;

import java.util.List;
import java.util.NoSuchElementException;

@RequiredArgsConstructor
@Service
public class UsuarioService implements UserDetailsService {

    private final UsuarioRepository usuarioRepository;

    private final PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Usuario usuario = buscarPorEmail(email);

        if (usuario == null) {
            throw new UsernameNotFoundException("Usuario no encontrado con email: " + email);
        }

        return usuario;
    }

    public List<Usuario> listarTodosUsuarios() {
        return usuarioRepository.findAll();
    }

    public List<Usuario> obtenerClientesActivos() {
        return usuarioRepository.findByTipoRolAndActivo(Rol.CLIENTE, true);
    }

    public Usuario obtenerPorId(Long idUsuario) {
        return usuarioRepository.findById(idUsuario).orElseThrow(() -> new NoSuchElementException("Usuario no encontrado con ID: " + idUsuario));
    }

    public Usuario buscarPorEmail(String email) {
        return usuarioRepository.findByEmail(email).orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado con email: " + email));
    }

    public void registrar(Usuario usuario) {
        usuario.setContrasena(passwordEncoder.encode(usuario.getContrasena()));
        usuarioRepository.save(usuario);
    }

    public void registrarUsuarioCompleto(Usuario usuario) {
        if (usuario.getActivo() == null) {
            usuario.setActivo(true);
        }

        registrar(usuario);
    }

    public void actualizar(Usuario usuario) {
        Usuario usuarioExistente = usuarioRepository.findById(usuario.getId())
                .orElseThrow(() -> new NoSuchElementException("Usuario no encontrado con ID: " + usuario.getId()));

        usuarioExistente.setNombre(usuario.getNombre());
        usuarioExistente.setApellido(usuario.getApellido());
        usuarioExistente.setTelefono(usuario.getTelefono());
        usuarioExistente.setEmail(usuario.getEmail());

        usuarioRepository.save(usuarioExistente);
    }

    public void actualizarUsuarioCompleto(Long id, Usuario usuarioActualizado) {
        Usuario usuarioExistente = obtenerPorId(id);

        usuarioExistente.setNombre(usuarioActualizado.getNombre());
        usuarioExistente.setApellido(usuarioActualizado.getApellido());
        usuarioExistente.setTelefono(usuarioActualizado.getTelefono());
        usuarioExistente.setEmail(usuarioActualizado.getEmail());
        usuarioExistente.setTipoRol(usuarioActualizado.getTipoRol());
        usuarioExistente.setActivo(usuarioActualizado.getActivo());

        if (usuarioActualizado.getContrasena() != null && !usuarioActualizado.getContrasena().isEmpty()) {
            usuarioExistente.setContrasena(passwordEncoder.encode(usuarioActualizado.getContrasena()));
        }

        usuarioRepository.save(usuarioExistente);
    }

    public void darseDeBaja(Long idUsuario) {
        Usuario usuarioEliminado = obtenerPorId(idUsuario);
        usuarioEliminado.setActivo(false);

        usuarioRepository.save(usuarioEliminado);
    }

}
