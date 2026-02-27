package com.example.app.user.application;

import com.example.app.user.domain.User;
import com.example.app.user.domain.UserRepository;
import com.example.app.user.domain.enums.UserType;
import com.example.app.user.domain.exception.UserNotFoundException;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserService {
  private final UserRepository userRepository;
  private final PasswordEncoder passwordEncoder;

  public boolean isMatchPassword(User user, String password) {
    return passwordEncoder.matches(password, user.getPassword());
  }

  public User getUserById(Long id) {
    return userRepository.findById(id).orElseThrow(() -> new UserNotFoundException(id));
  }

  public Optional<User> getUserByEmail(String email) {
    return userRepository.findByEmail(email);
  }

  public Boolean hasUserByEmail(String email) {
    return userRepository.existsByEmail(email);
  }

  @Transactional
  public User saveUser(UserType type, String email, String name, String password) {
    return userRepository.save(
        User.builder()
            .type(type)
            .email(email)
            .name(name)
            .password(passwordEncoder.encode(password))
            .build());
  }
}
