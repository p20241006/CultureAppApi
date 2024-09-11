package cultureapp.com.pe.user;

import org.springframework.stereotype.Service;


@Service
public class UserMapper {

    public UserResponse toUserResponse(User user) {
        return UserResponse.builder()
                .id(user.getId())
                .email(user.getEmail())
                .fullName(user.getFullName())
                .role(user.getRoles().get(0))
                .build();
    }
}
