package com.plantastic.backend.util;

import com.plantastic.backend.models.entity.User;
import com.plantastic.backend.repository.UserRepository;

import java.util.Optional;
import java.util.regex.Pattern;

public class UserUtil {

    private UserUtil() {}

    private static final String EMAIL_REGEX = "^[\\w.-]+@(?:[\\w-]+\\.){1,10}[\\w-]{2,4}$";

    private static final Pattern EMAIL_PATTERN = Pattern.compile(EMAIL_REGEX, Pattern.CASE_INSENSITIVE);

  public static boolean isValidEmail(String input) {
    return input != null && EMAIL_PATTERN.matcher(input).matches();
  }

    public static Optional<User> findByUsernameOrEmail(UserRepository userRepository, String identifier) {
        if (isValidEmail(identifier)) {
            return userRepository.findByEmail(identifier);
        } else {
            return userRepository.findByUsername(identifier);
        }
    }

}
