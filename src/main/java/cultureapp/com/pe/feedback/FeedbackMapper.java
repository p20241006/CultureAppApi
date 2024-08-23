package cultureapp.com.pe.feedback;


import cultureapp.com.pe.event.Event;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
public class FeedbackMapper {
    public Feedback toFeedback(FeedbackRequest request) {
        return Feedback.builder()
                .note(request.note())
                .comment(request.comment())
                .event(Event.builder()
                        .id(request.eventId())
                        .shareable(false) // Not required and has no impact :: just to satisfy lombok
                        .archived(false) // Not required and has no impact :: just to satisfy lombok
                        .build()
                )
                .build();
    }

    public FeedbackResponse toFeedbackResponse(Feedback feedback, Integer id) {
        return FeedbackResponse.builder()
                .note(feedback.getNote())
                .comment(feedback.getComment())
                .ownFeedback(Objects.equals(feedback.getCreatedBy(), id))
                .build();
    }
}
