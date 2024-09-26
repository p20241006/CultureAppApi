package cultureapp.com.pe.preference;

import cultureapp.com.pe.event.Event;
import cultureapp.com.pe.feedback.Feedback;
import cultureapp.com.pe.feedback.FeedbackRequest;
import cultureapp.com.pe.feedback.FeedbackResponse;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
public class PreferenceMapper {

    public PreferenceUser toPreference(PreferenceRequest request) {
        return PreferenceUser.builder()
                .rating(request.rating())
                .event(Event.builder()
                        .id(request.eventId())
                        .shareable(false) // Not required and has no impact :: just to satisfy lombok
                        .archived(false) // Not required and has no impact :: just to satisfy lombok
                        .build()
                )
                .build();
    }

    public PreferenceResponse toFeedbackResponse(PreferenceUser preferenceUser, Integer id) {
        return PreferenceResponse.builder()
                .rating(preferenceUser.getRating())
                .build();
    }
}
