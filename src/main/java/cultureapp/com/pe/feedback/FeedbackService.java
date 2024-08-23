package cultureapp.com.pe.feedback;

import cultureapp.com.pe.event.Event;
import cultureapp.com.pe.event.EventRepository;
import cultureapp.com.pe.common.PageResponse;
import cultureapp.com.pe.exception.OperationNotPermittedException;
import cultureapp.com.pe.user.User;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class FeedbackService {

    private final FeedBackRepository feedBackRepository;
    private final EventRepository eventRepository;
    private final FeedbackMapper feedbackMapper;

    public Integer save(FeedbackRequest request, Authentication connectedUser) {
        Event event = eventRepository.findById(request.eventId())
                .orElseThrow(() -> new EntityNotFoundException("No event found with ID:: " + request.eventId()));
        if (event.isArchived() || !event.isShareable()) {
            throw new OperationNotPermittedException("You cannot give a feedback for and archived or not shareable event");
        }
        User user = ((User) connectedUser.getPrincipal());
        if (Objects.equals(event.getOwner().getId(), user.getId())) {
            throw new OperationNotPermittedException("You cannot give feedback to your own event");
        }
        Feedback feedback = feedbackMapper.toFeedback(request);
        return feedBackRepository.save(feedback).getId();
    }

    @Transactional
    public PageResponse<FeedbackResponse> findAllFeedbacksByEvent(Integer eventId, int page, int size, Authentication connectedUser) {
        Pageable pageable = PageRequest.of(page, size);
        User user = ((User) connectedUser.getPrincipal());
        Page<Feedback> feedbacks = feedBackRepository.findAllByEventId(eventId, pageable);
        List<FeedbackResponse> feedbackResponses = feedbacks.stream()
                .map(f -> feedbackMapper.toFeedbackResponse(f, user.getId()))
                .toList();
        return new PageResponse<>(
                feedbackResponses,
                feedbacks.getNumber(),
                feedbacks.getSize(),
                feedbacks.getTotalElements(),
                feedbacks.getTotalPages(),
                feedbacks.isFirst(),
                feedbacks.isLast()
        );

    }
}
