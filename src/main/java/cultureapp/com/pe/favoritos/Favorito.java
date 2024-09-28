package cultureapp.com.pe.favoritos;

import cultureapp.com.pe.event.Event;
import cultureapp.com.pe.user.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "FAVORITO")
public class Favorito {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "evento_id", nullable = false)
    private Event event;

    @Column(name = "isfavorite")
    private Boolean isFavorite; // true si es favorito, false si no lo es

    // Puedes agregar campos de auditoría si es necesario, como fecha de creación o modificación
}
