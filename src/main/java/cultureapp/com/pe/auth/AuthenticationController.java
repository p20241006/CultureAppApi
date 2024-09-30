package cultureapp.com.pe.auth;

import cultureapp.com.pe.event.Event;
import cultureapp.com.pe.event.EventResponse;
import cultureapp.com.pe.event.EventService;
import cultureapp.com.pe.exception.EmailAlreadyExistsException;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.mail.MessagingException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("auth")
@RequiredArgsConstructor
@Tag(name = "Authentication")
public class AuthenticationController {

    private final AuthenticationService service;


    @PostMapping("/register")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public ResponseEntity<?> register(
            @RequestBody @Valid RegistrationRequest request
    ) throws MessagingException {
        service.register(request);
        return ResponseEntity.accepted().build();
    }

    @PostMapping("/login")
    public ResponseEntity<AuthenticationResponse> authenticate(
            @RequestBody AuthenticationRequest request
    ) {
        return ResponseEntity.ok(service.authenticate(request));
    }

    @GetMapping("/check-email")
    public ResponseEntity<String> checkEmail(@RequestParam String email) {
        boolean isTaken = service.isEmailTaken(email);
        if (isTaken) {
            throw new EmailAlreadyExistsException("Este email ya se encuentra en uso, elige otro por favor");
        }
        return ResponseEntity.ok("Email disponible");
    }


}
