package com.example.app.user.application;

import org.springframework.stereotype.Service;

import com.example.app.user.domain.User;
import com.example.app.user.domain.UserRepository;
import com.example.app.user.domain.exception.UserNotFoundException;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService {
  private final UserRepository userRepository;

  public User getUserById(Long id) {
    return userRepository.findById(id).orElseThrow(() -> new UserNotFoundException(id));
  }
}
