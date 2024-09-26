package cultureapp.com.pe.user;


import cultureapp.com.pe.event.Event;
import cultureapp.com.pe.preference.PreferenceUser;
import cultureapp.com.pe.role.Role;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import static jakarta.persistence.FetchType.EAGER;


@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "USUARIO")
@EntityListeners(AuditingEntityListener.class)
public class User implements UserDetails, Principal {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "nombre", length = 50)
    private String name;

    @Column(name = "apellido_materno", length = 50)
    private String motherSurname;

    @Column(name = "apellido_paterno", length = 50)
    private String fatherSurname;

    @Column(unique = true)
    private String email;

    private String password;

    @Column(name = "edad")
    private Integer age;

    @Column(name = "telefono", length = 50)
    private String phone;

    @Column(name = "ciudad", length = 50)
    private String city;

    @Column(name = "genero", length = 50)
    private String gender;

    @Column(name = "foto")
    private String photo;

    @Column(name = "cuenta_bloqueada")
    private boolean accountLocked;

    @Column(name="cuenta_activa")
    private boolean enabled;

    @Column(name="nombre_comercial")
    private String nombreComercial;

    @Column(name="DNI")
    private String dni;

    @Column(name="email2")
    private String email2;

    @Column(name="empresa")
    private String empresa;

    @Column(name="comentario")
    private String comentario;

    @Column(name="envio_formulario")
    private Boolean envioFormulario;

    @ManyToMany(fetch = EAGER)
    private List<Role> roles;

    @OneToMany(mappedBy = "owner")
    private List<Event> events;

    @OneToMany(mappedBy = "user")
    private List<PreferenceUser> histories;

    @CreatedDate
    @Column(nullable = false, name = "fecha_creacion", updatable = false)
    private LocalDateTime createdDate;

    @LastModifiedDate
    @Column(insertable = false, name = "fecha_modificacion")
    private LocalDateTime lastModifiedDate;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.roles
                .stream()
                .map(r -> new SimpleGrantedAuthority(r.getName()))
                .collect(Collectors.toList());
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return !accountLocked;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }

    @Override
    public String getName() {
        return email;
    }

    public String fullName() {
        return getName() + " " + getFatherSurname() + " " + getMotherSurname();
    }

    public String getFullName() {
        return name + " " + fatherSurname + " " + motherSurname;
    }
}
