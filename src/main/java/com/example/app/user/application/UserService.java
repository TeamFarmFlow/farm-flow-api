package com.example.app.user.application;

import com.example.app.user.domain.enums.UserType;
import com.example.app.user.domain.exception.DuplicateEmailException;
import com.example.app.user.presentation.dto.request.UserSignUpRequest;
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

  /**
   * 회원 가입(농장주)
   */
  @Transactional
  public Long signUp(UserSignUpRequest request) {

    if (userRepository.existsByEmail(request.getEmail())) {
      throw new DuplicateEmailException(request.getEmail());
    }

    String encodedPassword = passwordEncoder.encode(request.getPassword());

    User user = User.builder()
            .email(request.getEmail())
            .password(encodedPassword)
            .name(request.getUserName())
            .type(UserType.EMPLOYEE)
            .build();
    userRepository.save(user);

    return user.getId();
  }
}
