package cultureapp.com.pe.event;

import cultureapp.com.pe.common.PageResponse;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("events")
@RequiredArgsConstructor
@Tag(name = "Event")
public class EventController {

    private final EventService service;

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

    @GetMapping("/borrowed")
    public ResponseEntity<PageResponse<BorrowedEventResponse>> findAllBorrowedEvents(
            @RequestParam(name = "page", defaultValue = "0", required = false) int page,
            @RequestParam(name = "size", defaultValue = "10", required = false) int size,
            Authentication connectedUser
    ) {
        return ResponseEntity.ok(service.findAllBorrowedEvents(page, size, connectedUser));
    }

    @GetMapping("/returned")
    public ResponseEntity<PageResponse<BorrowedEventResponse>> findAllReturnedEvents(
            @RequestParam(name = "page", defaultValue = "0", required = false) int page,
            @RequestParam(name = "size", defaultValue = "10", required = false) int size,
            Authentication connectedUser
    ) {
        return ResponseEntity.ok(service.findAllReturnedEvents(page, size, connectedUser));
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

    @PostMapping("borrow/{event-id}")
    public ResponseEntity<Integer> borrowEvent(
            @PathVariable("event-id") Integer eventId,
            Authentication connectedUser
    ) {
        return ResponseEntity.ok(service.borrowEvent(eventId, connectedUser));
    }

    @PatchMapping("borrow/return/{event-id}")
    public ResponseEntity<Integer> returnBorrowEvent(
            @PathVariable("event-id") Integer eventId,
            Authentication connectedUser
    ) {
        return ResponseEntity.ok(service.returnBorrowedEvent(eventId, connectedUser));
    }

    @PatchMapping("borrow/return/approve/{event-id}")
    public ResponseEntity<Integer> approveReturnBorrowEvent(
            @PathVariable("event-id") Integer eventId,
            Authentication connectedUser
    ) {
        return ResponseEntity.ok(service.approveReturnBorrowedEvent(eventId, connectedUser));
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
}
