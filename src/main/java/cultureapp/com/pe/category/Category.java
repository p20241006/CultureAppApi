package cultureapp.com.pe.category;

import com.fasterxml.jackson.annotation.JsonIgnore;
import cultureapp.com.pe.event.Event;
import jakarta.persistence.*;
import lombok.*;
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
@Table(name = "CATEGORIA")
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "nombre")
    private String name;

    @Column(name = "descripcion")
    private String description;

    // Relación uno a muchos con eventos
    @OneToMany(mappedBy = "category", fetch = FetchType.LAZY)
    private List<Event> events;


}
