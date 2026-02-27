package com.example.app.core.guard;

import com.example.app.core.jwt.CustomUserDetails;
import com.example.app.user.domain.enums.UserType;
import java.util.Arrays;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

@Component
public class UserTypeChecker {

  public boolean check(Authentication authentication, UserType[] userTypes) {
    if (userTypes == null || userTypes.length == 0) {
      return false;
    }

    if (authentication == null || !authentication.isAuthenticated()) {
      return false;
    }

    CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
    return Arrays.asList(userTypes).contains(userDetails.getType());
  }
}
