package cultureapp.com.pe.preference;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("preferences")
@RequiredArgsConstructor
@Tag(name = "Preferencia")
public class PreferenceController {


    private final PreferenceService preferenceService;

    // Endpoint para puntuar o actualizar una preferencia
    @PostMapping("/rate")
    public ResponseEntity<Integer> rateEvent(@Valid @RequestBody PreferenceRequest request, Authentication authentication) {

        return ResponseEntity.ok(preferenceService.saveOrUpdateRating(request, authentication));
    }
}
