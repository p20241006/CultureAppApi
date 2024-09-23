package cultureapp.com.pe.user;

import cultureapp.com.pe.role.Role;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserResponse {

    private Integer id;
    private String email;
    private String fullName;
    private Role role;

}
