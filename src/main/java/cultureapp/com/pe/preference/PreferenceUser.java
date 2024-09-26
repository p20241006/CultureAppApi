package cultureapp.com.pe.preference;

import cultureapp.com.pe.common.BaseEntity;
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
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "PREFERENCIA")
public class PreferenceUser {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
    @ManyToOne
    @JoinColumn(name = "event_id")
    private Event event;

    @Column(name="rating")
    private Double rating;




}
