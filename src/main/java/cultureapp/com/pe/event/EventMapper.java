package cultureapp.com.pe.event;

import cultureapp.com.pe.category.Category;
import cultureapp.com.pe.file.FileUtils;
import cultureapp.com.pe.history.EventTransactionHistory;
import cultureapp.com.pe.region.Region;
import org.springframework.stereotype.Service;

@Service
public class EventMapper {
    public Event toEvent(EventRequest request) {
        return Event.builder()
                .id(request.id())
                .title(request.title())
                .description(request.description())
                .start_date(request.start_date())
                .end_date(request.end_date())
                .price(request.price())
                .imgEvent(request.imgEvent())
                .archived(false)
                .shareable(request.shareable())
                .category(Category.builder()
                        .id(request.categoryId())
                        .build())
                .region(Region.builder()
                        .id(request.regionId())
                        .build())
                .build();
    }

    public EventResponse toEventResponse(Event event) {
        return EventResponse.builder()
                .id(event.getId())
                .title(event.getTitle())
                .description(event.getDescription())
                .start_date(event.getStart_date())
                .end_date(event.getEnd_date())
                .price(event.getPrice())
                .urlEvent(event.getUrlEvent())
                .imgEvent(event.getImgEvent())
                .company(event.getCompany())
                .owner(event.getOwner().fullName())
                .categoria_id(event.getCategory().getId())
                .region_id(event.getRegion().getId())
                .cover(FileUtils.readFileFromLocation(event.getCompany()))
                .rate(event.getRate())
                .archived(event.isArchived())
                .shareable(event.isShareable())
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
