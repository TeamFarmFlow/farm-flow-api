package com.example.app.core.guard;

import org.springframework.stereotype.Component;

@Component
public class UserTypeChecker {

  //  public boolean check(Authentication authentication, UserType[] userTypes) {
  //    if (userTypes == null || userTypes.length == 0) {
  //      return false;
  //    }
  //
  //    if (authentication == null || !authentication.isAuthenticated()) {
  //      return false;
  //    }
  //
  //    CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
  //    return Arrays.asList(userTypes).contains(userDetails.getType());
  //  }
}
