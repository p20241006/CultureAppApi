package cultureapp.com.pe.user;

import cultureapp.com.pe.common.PageResponse;
import cultureapp.com.pe.event.Event;
import cultureapp.com.pe.event.EventResponse;
import cultureapp.com.pe.event.EventSpecification;
import cultureapp.com.pe.role.Role;
import cultureapp.com.pe.role.RoleRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class UserService {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private UserMapper userMapper;

    public UserResponse addRoleToUser(Integer userId, String roleName) {
        // Buscar el usuario por ID
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        // Buscar el rol por nombre
        Role role = roleRepository.findByName(roleName)
                .orElseThrow(() -> new RuntimeException("Rol no encontrado"));

        // Verificar si el usuario ya tiene ese rol
        if (user.getRoles().contains(role)) {
            throw new RuntimeException("El usuario ya tiene este rol");
        }

        // Agregar el rol al usuario
        user.getRoles().add(role);
        userRepository.save(user); // Guardar los cambios en el usuario

        // Devolver la respuesta del usuario actualizado
        return new UserMapper().toUserResponse(user);
    }

    public UserResponse findById(Integer userId) {
        return userRepository.findById(userId)
                .map(userMapper::toUserResponse)
                .orElseThrow(() -> new EntityNotFoundException("No user found with ID:: " + userId));
    }


    public UserResponse getUserOwner(Authentication connectedUser) {
        User user = ((User) connectedUser.getPrincipal());

        return userRepository.findById(user.getId())
                .map(userMapper::toUserResponse)
                .orElseThrow(() -> new EntityNotFoundException("No user found with ID:: " + user.getId()));

    }

    @Transactional
    public UserResponse updateUser(Integer idUser, UserRequest userRequest) {
        // Obtener el usuario existente
        User existingUser = userRepository.findById(idUser)
                .orElseThrow(() -> new EntityNotFoundException("No user found with ID:: " + idUser));

        // Actualizar el usuario existente con los valores del request
        userMapper.updateUserFromRequest(existingUser, userRequest);

        // Guardar los cambios
        User updatedUser = userRepository.save(existingUser);

        // Devolver la respuesta convertida
        return userMapper.toUserResponse(updatedUser);
    }

    @Transactional(readOnly = true)
    public List<UserResponse> getUsersWithEnvioFormularioTrue() {
        List<User> users = userRepository.findByEnvioFormularioTrue(); // Llama al repositorio
        return users.stream()
                .map(userMapper::toUserResponse)
                .toList(); // Convierte a UserResponse
    }

}
