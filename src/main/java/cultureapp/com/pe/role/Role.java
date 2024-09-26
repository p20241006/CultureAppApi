package cultureapp.com.pe.role;

import cultureapp.com.pe.user.User;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "ROL")
@EntityListeners(AuditingEntityListener.class)
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(name="nombre",unique = true)
    private String name;
    @Column(name="descripcion",unique = true)
    private String description;
    @ManyToMany(mappedBy = "roles")
    @JsonIgnore
    private List<User> user;

    @CreatedDate
    @Column(name="fecha_creacion",nullable = false, updatable = false)
    private LocalDateTime createdDate;

    @LastModifiedDate
    @Column(name="fecha_modificacion",insertable = false)
    private LocalDateTime lastModifiedDate;
}
