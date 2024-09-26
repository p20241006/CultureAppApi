package cultureapp.com.pe.user;

import cultureapp.com.pe.role.Role;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;


@Service
public class UserMapper {

    /*public User toUser(UserRequest userRequest) {
        return User.builder()
                .id(userRequest.id())
                .name(userRequest.name())
                .email(userRequest.email())
                .age(userRequest.age())
                .phone(userRequest.phone())
                .city(userRequest.city())
                .gender(userRequest.gender())
                .photo(userRequest.photo())
                .accountLocked(false)
                .enabled(true)
                .nombreComercial(userRequest.nombreComercial())
                .dni(userRequest.dni())
                .email2(userRequest.email2())
                .empresa(userRequest.empresa())
                .comentario(userRequest.comentario())
                .build();
    }*/

    public UserResponse toUserResponse(User user) {
        return UserResponse.builder()
                .id(user.getId())
                .fullName(user.getFullName())
                .email(user.getEmail())
                .age(user.getAge())
                .phone(user.getPhone())
                .city(user.getCity())
                .gender(user.getGender())
                .photo(user.getPhoto())
                .accountLocked(user.isAccountLocked())
                .enabled(user.isEnabled())
                .nombreComercial(user.getNombreComercial())
                .dni(user.getDni())
                .email2(user.getEmail2())
                .empresa(user.getEmpresa())
                .comentario(user.getComentario())
                .envioFormulario(user.getEnvioFormulario())
                .roles(user.getRoles().stream()
                        .map(Role::getName)  // Asume que Role tiene un método getName()
                        .collect(Collectors.toList()))
                .build();
    }

    public void updateUserFromRequest(User existingUser, UserRequest userRequest) {
        if (userRequest.name() != null) {
            existingUser.setName(userRequest.name());
        }
        if (userRequest.email() != null) {
            existingUser.setEmail(userRequest.email());
        }
        if (userRequest.age() != null) {
            existingUser.setAge(userRequest.age());
        }
        if (userRequest.phone() != null) {
            existingUser.setPhone(userRequest.phone());
        }
        if (userRequest.city() != null) {
            existingUser.setCity(userRequest.city());
        }
        if (userRequest.gender() != null) {
            existingUser.setGender(userRequest.gender());
        }
        if (userRequest.photo() != null) {
            existingUser.setPhoto(userRequest.photo());
        }
        if (userRequest.nombreComercial() != null) {
            existingUser.setNombreComercial(userRequest.nombreComercial());
        }
        if (userRequest.dni() != null) {
            existingUser.setDni(userRequest.dni());
        }
        if (userRequest.email2() != null) {
            existingUser.setEmail2(userRequest.email2());
        }
        if (userRequest.empresa() != null) {
            existingUser.setEmpresa(userRequest.empresa());
        }
        if (userRequest.comentario() != null) {
            existingUser.setComentario(userRequest.comentario());
        }

        if (userRequest.envioFormulario() == null){
            existingUser.setEnvioFormulario(true);
        }

        // No actualizamos el estado de la cuenta (accountLocked, enabled) ya que esto no viene en el request
    }

}
