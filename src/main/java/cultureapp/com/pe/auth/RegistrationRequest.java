package cultureapp.com.pe.auth;

import jakarta.validation.constraints.*;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class RegistrationRequest {

    @NotEmpty(message = "name is mandatory")
    @NotNull(message = "name is mandatory")
    private String name;

    @NotEmpty(message = "FatherSurname is mandatory")
    @NotNull(message = "FatherSurname is mandatory")
    private String fatherSurname;

    @NotEmpty(message = "MotherSurname is mandatory")
    @NotNull(message = "MotherSurname is mandatory")
    private String matherSurname;


    private Integer age;

    @NotEmpty(message = "gender is mandatory")
    @NotNull(message = "gender is mandatory")
    private String gender;

    @Email(message = "Email is not well formatted")
    @NotEmpty(message = "Email is mandatory")
    @NotNull(message = "Email is mandatory")
    private String email;

    @NotEmpty(message = "Password is mandatory")
    @NotNull(message = "Password is mandatory")
    @Size(min = 8, message = "Password should be 8 characters long minimum")
    private String password;
}
