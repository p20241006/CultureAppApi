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
                .isbn(request.isbn())
                .authorName(request.authorName())
                .synopsis(request.synopsis())
                .archived(false)
                .shareable(request.shareable())
                .build();
    }

    public EventResponse toEventResponse(Event event) {
        return EventResponse.builder()
                .id(event.getId())
                .title(event.getTitle())
                .authorName(event.getAuthorName())
                .isbn(event.getIsbn())
                .synopsis(event.getSynopsis())
                .rate(event.getRate())
                .archived(event.isArchived())
                .shareable(event.isShareable())
                .owner(event.getOwner().fullName())
                .cover(FileUtils.readFileFromLocation(event.getEventCover()))
                .build();
    }

    public BorrowedEventResponse toBorrowedEventResponse(EventTransactionHistory history) {
        return BorrowedEventResponse.builder()
                .id(history.getEvent().getId())
                .title(history.getEvent().getTitle())
                .authorName(history.getEvent().getAuthorName())
                .isbn(history.getEvent().getIsbn())
                .rate(history.getEvent().getRate())
                .returned(history.isReturned())
                .returnApproved(history.isReturnApproved())
                .build();
    }
}
