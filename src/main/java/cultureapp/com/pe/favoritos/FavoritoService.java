package cultureapp.com.pe.favoritos;

import cultureapp.com.pe.event.Event;
import cultureapp.com.pe.event.EventRepository;
import cultureapp.com.pe.user.User;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class FavoritoService {

    private final FavoritoRepository favoritoRepository;
    private final EventRepository eventRepository;

    @Transactional
    public String toggleFavorito(Integer eventId, Authentication connectedUser) {
        User user = (User) connectedUser.getPrincipal();
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new EntityNotFoundException("Evento no encontrado con ID:: " + eventId));

        Optional<Favorito> existingFavorito = favoritoRepository.findByUserIdAndEventId(user.getId(), eventId);

        if (existingFavorito.isPresent()) {
            Favorito favorito = existingFavorito.get();
            favorito.setIsFavorite(!favorito.getIsFavorite());
            favoritoRepository.save(favorito);
            return favorito.getIsFavorite() ? "Evento marcado como favorito" : "Evento eliminado de favoritos";
        } else {
            Favorito nuevoFavorito = Favorito.builder()
                    .user(user)
                    .event(event)
                    .isFavorite(true)
                    .build();
            favoritoRepository.save(nuevoFavorito);
            return "Evento marcado como favorito";
        }
    }


    public List<Event> getFavoritos(Authentication connectedUser) {
        User user = (User) connectedUser.getPrincipal();
        List<Favorito> favoritos = favoritoRepository.findAllByUserIdAndFavoritoTrue(user.getId());
        return favoritos.stream()
                .map(Favorito::getEvent)
                .toList();
    }

    public boolean isEventFavorito(Integer eventId, Authentication connectedUser) {
        User user = (User) connectedUser.getPrincipal();
        Optional<Favorito> favorito = favoritoRepository.findByUserIdAndEventId(user.getId(), eventId);
        return favorito.map(Favorito::getIsFavorite).orElse(false);
    }
}