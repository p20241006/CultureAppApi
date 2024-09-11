package cultureapp.com.pe.event;

import cultureapp.com.pe.file.FileUtils;
import cultureapp.com.pe.history.EventTransactionHistory;
import org.springframework.stereotype.Service;

@Service
public class EventMapper {
    public Event toEvent(EventRequest request) {
        return Event.builder()
                .id(request.id())
                .title(request.title())
                .urlEvent(request.urlEvent())
                .description(request.description())
                .imgEvent(request.imgEvent())
                .archived(false)
                .shareable(request.shareable())
                .build();
    }

    public EventResponse toEventResponse(Event event) {
        return EventResponse.builder()
                .id(event.getId())
                .title(event.getTitle())
                .description(event.getDescription())
                .imgEvent(event.getImgEvent())
                .urlEvent(event.getUrlEvent())
                .rate(event.getRate())
                .archived(event.isArchived())
                .shareable(event.isShareable())
                .owner(event.getOwner().fullName())
                .cover(FileUtils.readFileFromLocation(event.getCompany()))
                .build();
    }

    public ScoredEventResponse toScoredEventResponse(EventTransactionHistory history) {
        return ScoredEventResponse.builder()
                .id(history.getEvent().getId())
                .id_User(history.getUser().getId())
                .title(history.getEvent().getTitle())
                .description(history.getEvent().getDescription())
                .urlEvent(history.getEvent().getUrlEvent())
                .rate(history.getEvent().getRate())
                .build();
    }
}
