package cultureapp.com.pe.event;

import cultureapp.com.pe.category.Category;
import cultureapp.com.pe.common.PageResponse;
import cultureapp.com.pe.exception.OperationNotPermittedException;
import cultureapp.com.pe.file.FileStorageService;
import cultureapp.com.pe.preference.PreferenceUser;
import cultureapp.com.pe.preference.PreferenceUserRepository;
import cultureapp.com.pe.region.Region;
import cultureapp.com.pe.user.User;
import cultureapp.com.pe.user.UserRepository;
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

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class EventService {

    private final EventRepository eventRepository;
    private final EventMapper eventMapper;
    private final PreferenceUserRepository preferenceUserRepository;
    private final FileStorageService fileStorageService;
    private final UserRepository userRepository;

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

    public PageResponse<ScoredEventResponse> findAllScoredEvents(int page, int size, Authentication connectedUser) {
        User user = ((User) connectedUser.getPrincipal());
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdDate").descending());
        Page<PreferenceUser> allScoredEvents = preferenceUserRepository.findAllScoredEvents(pageable, user.getId());
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

    public List<EventResponse> getRandomEventsByCategoryIds(List<Integer> categoryIds) {

        List<Event> events = eventRepository.findEventsByCategoryIds(categoryIds);
        List<EventResponse> eventsResponse = new java.util.ArrayList<>(events.stream()
                .map(eventMapper::toEventResponse)
                .toList());

        Collections.shuffle(eventsResponse);

        // Retornar los primeros 10 eventos, o menos si no hay suficientes
        return eventsResponse.stream()
                .limit(10)
                .collect(Collectors.toList());
    }


    public List<EventResponse> getTop12UpcomingEvents() {
        LocalDate today = LocalDate.now();
        LocalDate tenDaysLater = today.plusDays(30);

        List<Event> events = eventRepository.findTop12UpcomingEvents(today, tenDaysLater);
        List<EventResponse> eventsResponse = new java.util.ArrayList<>(events.stream()
                .map(eventMapper::toEventResponse)
                .toList());

        return eventsResponse;
    }

    public void deleteEventByCategoryId(Integer eventId, Authentication connectedUser) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new EntityNotFoundException("No event found with ID:: " + eventId));
        User user = ((User) connectedUser.getPrincipal());

        // Verifica si el usuario es el propietario del evento
        if (!Objects.equals(event.getOwner().getId(), user.getId())) {
            throw new OperationNotPermittedException("You cannot delete others' events");
        }

        // Elimina el evento
        eventRepository.delete(event);
        log.info("Event with ID:: {} deleted successfully", eventId);
    }

    public EventResponse updateEvent(Integer eventId, EventRequest eventRequest) {
        Optional<Event> eventOptional = eventRepository.findById(eventId);

        if (eventOptional.isPresent()) {
            Event event = eventOptional.get();

            // Actualizar los campos del evento
            event.setTitle(eventRequest.title());
            event.setDescription(eventRequest.description());
            event.setStart_date(eventRequest.start_date());
            event.setPrice(eventRequest.price());
            event.setImgEvent(eventRequest.imgEvent());
            event.setShareable(eventRequest.shareable());
            event.setCompany(eventRequest.company());

            // Guardar los cambios
            Event updatedEvent = eventRepository.save(event);
            return eventMapper.toEventResponse(updatedEvent);

        } else {
            throw new RuntimeException("Event with id " + eventId + " not found");
        }
    }


}
