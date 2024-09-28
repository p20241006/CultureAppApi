package cultureapp.com.pe.favoritos;

import cultureapp.com.pe.event.EventMapper;
import cultureapp.com.pe.event.EventResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("favoritos")
@RequiredArgsConstructor
@Tag(name = "Favoritos")
public class FavoriteController {

    private final FavoritoService favoritoService;
    private final EventMapper eventMapper;

    @PostMapping("/toggle/{eventId}")
    public ResponseEntity<?> toggleFavorito(@PathVariable Integer eventId, Authentication authentication) {
        favoritoService.toggleFavorito(eventId, authentication);
        return ResponseEntity.ok(Map.of("message", "Evento guardado en favoritos", "eventId", eventId));
    }

    @GetMapping("/events")
    public List<EventResponse> getFavoritos(Authentication authentication) {
        return favoritoService.getFavoritos(authentication)
                .stream()
                .map(eventMapper::toEventResponse)
                .toList();
    }
}