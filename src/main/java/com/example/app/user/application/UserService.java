package com.example.app.user.application;

import com.example.app.user.domain.enums.UserType;
import com.example.app.auth.domain.exception.DuplicateEmailException;
import com.example.app.auth.presentation.dto.request.SignUpRequest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.app.user.domain.User;
import com.example.app.user.domain.UserRepository;
import com.example.app.user.domain.exception.UserNotFoundException;

import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserService {
  private final UserRepository userRepository;
  private final PasswordEncoder passwordEncoder;

  public User getUserById(Long id) {
    return userRepository.findById(id).orElseThrow(() -> new UserNotFoundException(id));
  }

  public Boolean hasUserByEmail(String email) {
    return userRepository.existsByEmail(email);
  }

  @Transactional
  public User saveUser(UserType type, String email, String name, String password) {
    return userRepository.save(User.builder()
            .type(type)
            .email(email)
            .name(name)
            .password(passwordEncoder.encode(password))
            .build());
  }
}
