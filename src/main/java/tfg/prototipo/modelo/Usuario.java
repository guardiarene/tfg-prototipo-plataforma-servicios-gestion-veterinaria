package tfg.prototipo.modelo;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "USUARIO", uniqueConstraints = {@UniqueConstraint(columnNames = "EMAIL")})
public class Usuario implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Long id;

    @Column(name = "NOMBRE")
    private String nombre;

    @Column(name = "APELLIDO")
    private String apellido;

    @Column(name = "TELEFONO")
    private String telefono;

    @Column(name = "EMAIL", unique = true)
    private String email;

    @Column(name = "CONTRASENA")
    private String contrasena;

    @Enumerated(EnumType.STRING)
    @Column(name = "ROL", length = 20)
    private Rol tipoRol;

    @OneToMany(mappedBy = "propietario", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Mascota> mascotas = new ArrayList<>();

    @OneToMany(mappedBy = "cliente", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Turno> turnos = new ArrayList<>();

    @Column(name = "ACTIVO", nullable = false)
    private Boolean activo = true;

    @OneToMany(mappedBy = "veterinario", fetch = FetchType.LAZY)
    @Fetch(FetchMode.JOIN)
    private List<ClinicaVeterinaria> clinicas = new ArrayList<>();

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        String rol = "ROLE_" + this.tipoRol.name();
        return List.of(new SimpleGrantedAuthority(rol));
    }

    @Override
    public String getUsername() {
        return this.email;
    }

    @Override
    public String getPassword() {
        return this.contrasena;
    }

}
