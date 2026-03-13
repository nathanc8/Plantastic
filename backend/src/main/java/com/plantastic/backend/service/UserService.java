package com.plantastic.backend.service;

import com.plantastic.backend.dto.auth.RegisterRequest;
import com.plantastic.backend.event.UserLoginSuccessEvent;
import com.plantastic.backend.models.entity.User;
import com.plantastic.backend.models.types.NotificationsPreferences;
import com.plantastic.backend.models.types.UserRole;
import com.plantastic.backend.repository.UserRepository;
import com.plantastic.backend.util.UserUtil;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @EventListener
    public void handleUserLoginSuccess(UserLoginSuccessEvent event) {
        updateLastLogin(event.username());
    }

    /**
     * When a user logs in, we update the last_log_in field in db
     * @param usernameOrEmail string
     */
    public void updateLastLogin(String usernameOrEmail) {

        Optional<User> userOpt;
        userOpt = findUserByUsernameOrEmail(usernameOrEmail);

        if (userOpt.isPresent()) {
            User user = userOpt.get();
            user.setLastLoginAt(LocalDate.now());
            userRepository.save(user);
        }
    }

    public Optional<User> findUserByUsernameOrEmail(String usernameOrEmail) {
        return UserUtil.findByUsernameOrEmail(userRepository, usernameOrEmail);
    }

    @Transactional
    public void createUser(RegisterRequest registerRequest) {
        if (UserUtil.isValidEmail(registerRequest.getEmail())) {
            User user = new User();
            user.setEmail(registerRequest.getEmail());
            user.setUsername(registerRequest.getUsername());
            user.setPassword(passwordEncoder.encode(registerRequest.getPassword()));
            user.setCreatedAt(LocalDateTime.now());
            user.setUpdatedAt(LocalDateTime.now());
            user.setRole(UserRole.ROLE_USER);
            user.setNotificationsConsent(false);
            user.setNotificationsPreferences(NotificationsPreferences.STANDARD);
            user.setCameraConsent(false);
            userRepository.save(user);
        }
    }

    public boolean emailExists(String email) {
        return userRepository.findByEmail(email).isPresent();
    }

    public boolean usernameExists(String username) {
        return userRepository.findByUsername(username).isPresent();
    }

    @Transactional
    public void deleteUserById(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User " + userId + " not found"));
        userRepository.delete(user);
    }

}
