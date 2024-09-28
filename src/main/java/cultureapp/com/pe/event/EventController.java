package cultureapp.com.pe.event;

import cultureapp.com.pe.common.PageResponse;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("events")
@RequiredArgsConstructor
@Tag(name = "Event")
public class EventController {

    private final EventService service;
    LocalDate fecha = LocalDate.now();
    LocalDate fechaFutura = fecha.plusDays(30);

    @PreAuthorize("hasAuthority('USER')")
    @PostMapping
    public ResponseEntity<Integer> saveEvent(
            @Valid @RequestBody EventRequest request,
            Authentication connectedUser
    ) {
        return ResponseEntity.ok(service.save(request, connectedUser));
    }

    @GetMapping("/{event-id}")
    public ResponseEntity<EventResponse> findEventById(
            @PathVariable("event-id") Integer eventId
    ) {
        return ResponseEntity.ok(service.findById(eventId));
    }

    @GetMapping
    public ResponseEntity<PageResponse<EventResponse>> findAllEvents(
            @RequestParam(name = "page", defaultValue = "0", required = false) int page,
            @RequestParam(name = "size", defaultValue = "10", required = false) int size,
            Authentication connectedUser
    ) {
        return ResponseEntity.ok(service.findAllEvents(page, size, connectedUser));
    }

    @GetMapping("/owner")
    public ResponseEntity<PageResponse<EventResponse>> findAllEventsByOwner(
            @RequestParam(name = "page", defaultValue = "0", required = false) int page,
            @RequestParam(name = "size", defaultValue = "10", required = false) int size,
            Authentication connectedUser
    ) {
        return ResponseEntity.ok(service.findAllEventsByOwner(page, size, connectedUser));
    }

    @GetMapping("/preferences_user")
    public ResponseEntity<PageResponse<ScoredEventResponse>> findAllScoredEvents(
            @RequestParam(name = "page", defaultValue = "0", required = false) int page,
            @RequestParam(name = "size", defaultValue = "10", required = false) int size,
            Authentication connectedUser
    ) {
        return ResponseEntity.ok(service.findAllScoredEvents(page, size, connectedUser));
    }


    @PatchMapping("/shareable/{event-id}")
    public ResponseEntity<Integer> updateShareableStatus(
            @PathVariable("event-id") Integer eventId,
            Authentication connectedUser
    ) {
        return ResponseEntity.ok(service.updateShareableStatus(eventId, connectedUser));
    }

    @PatchMapping("/archived/{event-id}")
    public ResponseEntity<Integer> updateArchivedStatus(
            @PathVariable("event-id") Integer eventId,
            Authentication connectedUser
    ) {
        return ResponseEntity.ok(service.updateArchivedStatus(eventId, connectedUser));
    }


    @PostMapping(value = "/cover/{event-id}", consumes = "multipart/form-data")
    public ResponseEntity<?> uploadEventCoverPicture(
            @PathVariable("event-id") Integer eventId,
            @Parameter()
            @RequestPart("file") MultipartFile file,
            Authentication connectedUser
    ) {
        service.uploadEventCoverPicture(file, connectedUser, eventId);
        return ResponseEntity.accepted().build();
    }


    @GetMapping("/proximos")
    public ResponseEntity<List<EventResponse>> getTop12UpcomingEvents() {
        return ResponseEntity.ok(service.getTop12UpcomingEvents());
    }

    @DeleteMapping("/{eventId}")
    public ResponseEntity<Void> deleteEvent(@PathVariable Integer eventId, Authentication authentication) {
        service.deleteEventByCategoryId(eventId, authentication);
        return ResponseEntity.noContent().build(); // 204 No Content
    }

    @PutMapping("/{eventId}")
    public ResponseEntity<EventResponse> updateEvent(
            @PathVariable Integer eventId,
            @RequestBody EventRequest eventRequest) {
        EventResponse updatedEvent = service.updateEvent(eventId, eventRequest);
        return ResponseEntity.ok(updatedEvent);
    }



}
