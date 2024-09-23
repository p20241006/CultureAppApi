package cultureapp.com.pe.region;

import com.fasterxml.jackson.annotation.JsonIgnore;
import cultureapp.com.pe.event.Event;
import cultureapp.com.pe.user.User;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "REGION")
public class Region {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "nombre")
    private String name;

    @Column(name = "descripcion")
    private String description;

    // Relación uno a muchos con eventos
    @OneToMany(mappedBy = "region", fetch = FetchType.LAZY)
    private List<Event> events;


}
