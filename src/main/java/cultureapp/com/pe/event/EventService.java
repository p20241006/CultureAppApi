package cultureapp.com.pe.event;

import cultureapp.com.pe.common.PageResponse;
import cultureapp.com.pe.exception.OperationNotPermittedException;
import cultureapp.com.pe.file.FileStorageService;
import cultureapp.com.pe.history.EventTransactionHistory;
import cultureapp.com.pe.history.EventTransactionHistoryRepository;
import cultureapp.com.pe.user.User;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class EventService {

    private final EventRepository eventRepository;
    private final EventMapper eventMapper;
    private final EventTransactionHistoryRepository transactionHistoryRepository;
    private final FileStorageService fileStorageService;

    public Integer save(EventRequest request, Authentication connectedUser) {
        User user = ((User) connectedUser.getPrincipal());
        Event event = eventMapper.toEvent(request);
        event.setOwner(user);
        return eventRepository.save(event).getId();
    }

    public EventResponse findById(Integer eventId) {
        return eventRepository.findById(eventId)
                .map(eventMapper::toEventResponse)
                .orElseThrow(() -> new EntityNotFoundException("No event found with ID:: " + eventId));
    }

    public PageResponse<EventResponse> findAllEvents(int page, int size, Authentication connectedUser) {
        User user = ((User) connectedUser.getPrincipal());
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdDate").descending());
        Page<Event> events = eventRepository.findAllDisplayableEvents(pageable, user.getId());
        List<EventResponse> eventsResponse = events.stream()
                .map(eventMapper::toEventResponse)
                .toList();
        return new PageResponse<>(
                eventsResponse,
                events.getNumber(),
                events.getSize(),
                events.getTotalElements(),
                events.getTotalPages(),
                events.isFirst(),
                events.isLast()
        );
    }

    public PageResponse<EventResponse> findAllEventsByOwner(int page, int size, Authentication connectedUser) {
        User user = ((User) connectedUser.getPrincipal());
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdDate").descending());
        Page<Event> events = eventRepository.findAll(EventSpecification.withOwnerId(user.getId()), pageable);
        List<EventResponse> eventsResponse = events.stream()
                .map(eventMapper::toEventResponse)
                .toList();
        return new PageResponse<>(
                eventsResponse,
                events.getNumber(),
                events.getSize(),
                events.getTotalElements(),
                events.getTotalPages(),
                events.isFirst(),
                events.isLast()
        );
    }

    public Integer updateShareableStatus(Integer eventId, Authentication connectedUser) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new EntityNotFoundException("No event found with ID:: " + eventId));
        User user = ((User) connectedUser.getPrincipal());
        if (!Objects.equals(event.getOwner().getId(), user.getId())) {
            throw new OperationNotPermittedException("You cannot update others events shareable status");
        }
        event.setShareable(!event.isShareable());
        eventRepository.save(event);
        return eventId;
    }

    public Integer updateArchivedStatus(Integer eventId, Authentication connectedUser) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new EntityNotFoundException("No event found with ID:: " + eventId));
        User user = ((User) connectedUser.getPrincipal());
        if (!Objects.equals(event.getOwner().getId(), user.getId())) {
            throw new OperationNotPermittedException("You cannot update others events archived status");
        }
        event.setArchived(!event.isArchived());
        eventRepository.save(event);
        return eventId;
    }


    public void uploadEventCoverPicture(MultipartFile file, Authentication connectedUser, Integer eventId) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new EntityNotFoundException("No event found with ID:: " + eventId));
        User user = ((User) connectedUser.getPrincipal());
        var profilePicture = fileStorageService.saveFile(file, eventId, user.getId());
        event.setCompany(profilePicture);
        eventRepository.save(event);
    }

    public PageResponse<ScoredEventResponse> findAllBorrowedEvents(int page, int size, Authentication connectedUser) {
        User user = ((User) connectedUser.getPrincipal());
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdDate").descending());
        Page<EventTransactionHistory> allScoredEvents = transactionHistoryRepository.findAllScoredEvents(pageable, user.getId());
        List<ScoredEventResponse> eventsResponse = allScoredEvents.stream()
                .map(eventMapper::toScoredEventResponse)
                .toList();
        return new PageResponse<>(
                eventsResponse,
                allScoredEvents.getNumber(),
                allScoredEvents.getSize(),
                allScoredEvents.getTotalElements(),
                allScoredEvents.getTotalPages(),
                allScoredEvents.isFirst(),
                allScoredEvents.isLast()
        );
    }

}
