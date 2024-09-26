package cultureapp.com.pe.user;

import cultureapp.com.pe.role.Role;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public record UserRequest(
        Integer id,
        String name,
        String motherSurname,
        String fatherSurname,
        String email,
        Integer age,
        @NotNull(message = "103")
        @NotEmpty(message = "103")
        String phone,
        String city,
        String gender,
        String photo,
        Boolean accountLocked,
        Boolean enabled,
        @NotNull(message = "104")
        @NotEmpty(message = "104")
        String nombreComercial,
        @NotNull(message = "105")
        @NotEmpty(message = "105")
        String dni,
        @NotNull(message = "106")
        @NotEmpty(message = "106")
        String email2,
        String empresa,
        String comentario,
        Boolean envioFormulario
) {
}