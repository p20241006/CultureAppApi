package cultureapp.com.pe.user;

import cultureapp.com.pe.role.Role;
import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserResponse {

    private Integer id;
    private String fullName;
    private String email;
    private Integer age;
    private String phone;
    private String city;
    private String gender;
    private String photo;
    private Boolean accountLocked;
    private Boolean enabled;
    private String nombreComercial;
    private String dni;
    private String email2;
    private String empresa;
    private String comentario;
    private Boolean envioFormulario;
    private List<String> roles;

}
