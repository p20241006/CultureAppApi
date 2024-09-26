package cultureapp.com.pe.event;

import cultureapp.com.pe.category.Category;
import cultureapp.com.pe.common.BaseEntity;
import cultureapp.com.pe.feedback.Feedback;
import cultureapp.com.pe.preference.PreferenceUser;
import cultureapp.com.pe.region.Region;
import cultureapp.com.pe.user.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import java.time.LocalDate;
import java.util.List;



@Getter
@Setter
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "EVENTO")
public class Event extends BaseEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "titulo")
    private String title;
    @Column(name = "descripcion",length = 1000000)
    private String description; //authorname
    @Column(name = "fecha_inicio")
    private LocalDate start_date;
    @Column(name = "fecha_fin")
    private LocalDate end_date;
    @Column(name = "costo")
    private Float price;
    @Column(name = "url_evento",length = 10000)
    private String urlEvent;  //isbn
    @Column(name = "url_img",length = 10000)
    private String imgEvent;  //synopsis
    @Column(name = "compañia")
    private String company;  //bookcover
    @Column(name = "archivado")
    private boolean archived;
    @Column(name = "compartible")
    private boolean shareable;

    //private boolean favorite;
    @ManyToOne
    @JoinColumn(name = "owner_id")
    private User owner;

    @OneToMany(mappedBy = "event")
    private List<Feedback> feedbacks;


    @OneToMany(mappedBy = "event")
    private List<PreferenceUser> preferenceUsers;

    // Relación muchos a uno con Category
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "categoria_id", nullable = false)
    private Category category;

    // Relación muchos a uno con regiones
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "region_id", nullable = false)
    private Region region;

    @Transient
    public double getRate() {
        if (preferenceUsers == null || preferenceUsers.isEmpty()) {
            return 0.0;
        }
        var rate = this.preferenceUsers.stream()
                .mapToDouble(PreferenceUser::getRating)
                .average()
                .orElse(0.0);
        double roundedRate = Math.round(rate * 10.0) / 10.0;

        // Loguear el valor de roundedRate
        System.out.println("El valor de roundedRate es: {}"+ roundedRate);

        // Return 4.0 if roundedRate is less than 4.5, otherwise return 4.5
        return roundedRate;
    }
}
