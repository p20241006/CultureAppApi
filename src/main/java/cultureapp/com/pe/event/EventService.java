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

    public Integer borrowEvent(Integer eventId, Authentication connectedUser) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new EntityNotFoundException("No event found with ID:: " + eventId));
        if (event.isArchived() || !event.isShareable()) {
            throw new OperationNotPermittedException("The requested event cannot be borrowed since it is archived or not shareable");
        }
        User user = ((User) connectedUser.getPrincipal());
        if (Objects.equals(event.getOwner().getId(), user.getId())) {
            throw new OperationNotPermittedException("You cannot borrow your own event");
        }
        final boolean isAlreadyBorrowedByUser = transactionHistoryRepository.isAlreadyBorrowedByUser(eventId, user.getId());
        if (isAlreadyBorrowedByUser) {
            throw new OperationNotPermittedException("You already borrowed this event and it is still not returned or the return is not approved by the owner");
        }

        final boolean isAlreadyBorrowedByOtherUser = transactionHistoryRepository.isAlreadyBorrowed(eventId);
        if (isAlreadyBorrowedByOtherUser) {
            throw new OperationNotPermittedException("Te requested event is already borrowed");
        }

        EventTransactionHistory eventTransactionHistory = EventTransactionHistory.builder()
                .user(user)
                .event(event)
                .returned(false)
                .returnApproved(false)
                .build();
        return transactionHistoryRepository.save(eventTransactionHistory).getId();

    }

    public Integer returnBorrowedEvent(Integer eventId, Authentication connectedUser) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new EntityNotFoundException("No event found with ID:: " + eventId));
        if (event.isArchived() || !event.isShareable()) {
            throw new OperationNotPermittedException("The requested event is archived or not shareable");
        }
        User user = ((User) connectedUser.getPrincipal());
        if (Objects.equals(event.getOwner().getId(), user.getId())) {
            throw new OperationNotPermittedException("You cannot borrow or return your own event");
        }

        EventTransactionHistory eventTransactionHistory = transactionHistoryRepository.findByEventIdAndUserId(eventId, user.getId())
                .orElseThrow(() -> new OperationNotPermittedException("You did not borrow this event"));

        eventTransactionHistory.setReturned(true);
        return transactionHistoryRepository.save(eventTransactionHistory).getId();
    }

    public Integer approveReturnBorrowedEvent(Integer eventId, Authentication connectedUser) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new EntityNotFoundException("No event found with ID:: " + eventId));
        if (event.isArchived() || !event.isShareable()) {
            throw new OperationNotPermittedException("The requested event is archived or not shareable");
        }
        User user = ((User) connectedUser.getPrincipal());
        if (!Objects.equals(event.getOwner().getId(), user.getId())) {
            throw new OperationNotPermittedException("You cannot approve the return of a event you do not own");
        }

        EventTransactionHistory eventTransactionHistory = transactionHistoryRepository.findByEventIdAndOwnerId(eventId, user.getId())
                .orElseThrow(() -> new OperationNotPermittedException("The event is not returned yet. You cannot approve its return"));

        eventTransactionHistory.setReturnApproved(true);
        return transactionHistoryRepository.save(eventTransactionHistory).getId();
    }

    public void uploadEventCoverPicture(MultipartFile file, Authentication connectedUser, Integer eventId) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new EntityNotFoundException("No event found with ID:: " + eventId));
        User user = ((User) connectedUser.getPrincipal());
        var profilePicture = fileStorageService.saveFile(file, eventId, user.getId());
        event.setEventCover(profilePicture);
        eventRepository.save(event);
    }

    public PageResponse<BorrowedEventResponse> findAllBorrowedEvents(int page, int size, Authentication connectedUser) {
        User user = ((User) connectedUser.getPrincipal());
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdDate").descending());
        Page<EventTransactionHistory> allBorrowedEvents = transactionHistoryRepository.findAllBorrowedEvents(pageable, user.getId());
        List<BorrowedEventResponse> eventsResponse = allBorrowedEvents.stream()
                .map(eventMapper::toBorrowedEventResponse)
                .toList();
        return new PageResponse<>(
                eventsResponse,
                allBorrowedEvents.getNumber(),
                allBorrowedEvents.getSize(),
                allBorrowedEvents.getTotalElements(),
                allBorrowedEvents.getTotalPages(),
                allBorrowedEvents.isFirst(),
                allBorrowedEvents.isLast()
        );
    }

    public PageResponse<BorrowedEventResponse> findAllReturnedEvents(int page, int size, Authentication connectedUser) {
        User user = ((User) connectedUser.getPrincipal());
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdDate").descending());
        Page<EventTransactionHistory> allBorrowedEvents = transactionHistoryRepository.findAllReturnedEvents(pageable, user.getId());
        List<BorrowedEventResponse> eventsResponse = allBorrowedEvents.stream()
                .map(eventMapper::toBorrowedEventResponse)
                .toList();
        return new PageResponse<>(
                eventsResponse,
                allBorrowedEvents.getNumber(),
                allBorrowedEvents.getSize(),
                allBorrowedEvents.getTotalElements(),
                allBorrowedEvents.getTotalPages(),
                allBorrowedEvents.isFirst(),
                allBorrowedEvents.isLast()
        );
    }
}
