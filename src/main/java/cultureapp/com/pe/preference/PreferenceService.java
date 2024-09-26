package cultureapp.com.pe.preference;

import cultureapp.com.pe.event.Event;
import cultureapp.com.pe.event.EventRepository;
import cultureapp.com.pe.exception.OperationNotPermittedException;
import cultureapp.com.pe.feedback.FeedBackRepository;
import cultureapp.com.pe.feedback.Feedback;
import cultureapp.com.pe.feedback.FeedbackMapper;
import cultureapp.com.pe.feedback.FeedbackRequest;
import cultureapp.com.pe.user.User;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PreferenceService {

    private final PreferenceUserRepository preferenceUserRepository;
    private final EventRepository eventRepository;
    private final PreferenceMapper preferenceMapper;

    public Integer saveOrUpdateRating(PreferenceRequest request, Authentication connectedUser) {
        // Buscar el evento
        Event event = eventRepository.findById(request.eventId())
                .orElseThrow(() -> new EntityNotFoundException("No event found with ID:: " + request.eventId()));

        // Validar si el evento es archivado o no es compartible
        if (event.isArchived() || !event.isShareable()) {
            throw new OperationNotPermittedException("You cannot give feedback for an archived or non-shareable event");
        }

        // Obtener el usuario conectado
        User user = (User) connectedUser.getPrincipal();

        // Verificar si el usuario es el dueño del evento
        if (Objects.equals(event.getOwner().getId(), user.getId())) {
            throw new OperationNotPermittedException("You cannot give feedback to your own event");
        }

        // Verificar si ya existe una calificación para este evento por el usuario
        Optional<PreferenceUser> existingPreference = preferenceUserRepository.findByUserAndEvent(user, event);

        if (existingPreference.isPresent()) {
            // Actualizar el rating si ya existe una calificación
            PreferenceUser preferenceUser = existingPreference.get();
            preferenceUser.setRating(request.rating()); // Asumiendo que tienes un método setRating
            return preferenceUserRepository.save(preferenceUser).getId();
        } else {
            // Crear una nueva calificación si no existe
            PreferenceUser preferenceUser = preferenceMapper.toPreference(request);
            preferenceUser.setUser(user); // Asignar el usuario conectado
            preferenceUser.setEvent(event); // Asignar el evento correspondiente
            return preferenceUserRepository.save(preferenceUser).getId();
        }
    }
}