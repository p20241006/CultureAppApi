package cultureapp.com.pe.event;

import cultureapp.com.pe.common.BaseEntity;
import cultureapp.com.pe.feedback.Feedback;
import cultureapp.com.pe.history.EventTransactionHistory;
import cultureapp.com.pe.user.User;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Transient;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.util.List;

@Getter
@Setter
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Event extends BaseEntity {

    private String title;
    private String description; //authorname
    private String urlEvent;  //isbn
    private String imgEvent;  //synopsis
    private String company;  //bookcover
    private boolean archived;
    private boolean shareable;
    //private boolean favorite;
    @ManyToOne
    @JoinColumn(name = "owner_id")
    private User owner;
    @OneToMany(mappedBy = "event")
    private List<Feedback> feedbacks;
    @OneToMany(mappedBy = "event")
    private List<EventTransactionHistory> histories;

    @Transient
    public double getRate() {
        if (feedbacks == null || feedbacks.isEmpty()) {
            return 0.0;
        }
        var rate = this.feedbacks.stream()
                .mapToDouble(Feedback::getNote)
                .average()
                .orElse(0.0);
        double roundedRate = Math.round(rate * 10.0) / 10.0;

        // Return 4.0 if roundedRate is less than 4.5, otherwise return 4.5
        return roundedRate;
    }
}
