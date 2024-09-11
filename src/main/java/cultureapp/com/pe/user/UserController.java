package cultureapp.com.pe.user;

import cultureapp.com.pe.event.EventResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
@Tag(name = "User")
public class UserController {


    private final UserService userService;

    @PostMapping("/{userId}/roles/{roleId}")
    public ResponseEntity<User> addRoleUser(@PathVariable int userId, @PathVariable int roleId) {
        User userUpdate = userService.addedRolePerUser(userId, roleId);
        return ResponseEntity.ok(userUpdate);
    }

    @GetMapping("/{user-id}")
    public ResponseEntity<UserResponse> findUserById(
            @PathVariable("user-id") Integer eventId
    ) {
        return ResponseEntity.ok(userService.findById(eventId));
    }

    @GetMapping("/me")
    public ResponseEntity<UserResponse> findMe(Authentication connectedUser) {
        return ResponseEntity.ok(userService.getUserOwner(connectedUser));
    }
}
