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

    public User addedRolePerUser(Integer userId, Integer roleId) {
        User user = userRepository.findById(userId)
                .orElseThrow(()-> new RuntimeException("User with id " + userId + " not found"));

        Role newRole = roleRepository.findById(roleId)
                .orElseThrow(()-> new RuntimeException("Role with id " + roleId + " not found"));

        user.getRoles().add(newRole);

        return userRepository.save(user);
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


}
