package cultureapp.com.pe.user;

import cultureapp.com.pe.event.EventResponse;
import cultureapp.com.pe.role.RoleRequest;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
@Tag(name = "User")
public class UserController {


    private final UserService userService;

    @PostMapping("/{userId}/roles")
    public ResponseEntity<UserResponse> addRoleToUser(
            @PathVariable Integer userId,
            @RequestBody RoleRequest roleRequest) {

        UserResponse updatedUser = userService.addRoleToUser(userId, roleRequest.getRoleName());
        return ResponseEntity.ok(updatedUser);
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

    @PutMapping("/{id}")
    public ResponseEntity<UserResponse> updateUser(
            @PathVariable Integer id,
            @RequestBody UserRequest userRequest) {

        // Llamar al servicio para actualizar el usuario
        UserResponse updatedUser = userService.updateUser(id, userRequest);

        // Devolver la respuesta HTTP con el usuario actualizado
        return ResponseEntity.ok(updatedUser);
    }

    @GetMapping("/envio-formulario")
    public List<UserResponse> getUsersWithEnvioFormulario() {
        return userService.getUsersWithEnvioFormularioTrue(); // Llama al servicio
    }
}
