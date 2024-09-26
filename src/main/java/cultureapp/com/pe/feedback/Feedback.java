package cultureapp.com.pe.feedback;


import cultureapp.com.pe.common.BaseEntity;
import cultureapp.com.pe.event.Event;
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
@Table(name = "FEEDBACK")
public class Feedback extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column
    private Double note;
    private String comment;

    @ManyToOne
    @JoinColumn(name = "event_id")
    private Event event;
}
