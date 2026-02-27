package com.example.app.core.guard;

import com.example.app.core.exception.ForbiddenException;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Aspect
@Component
@RequiredArgsConstructor
public class UserTypeGuardAspect {

  private final UserTypeChecker userTypeChecker;

  @Around("@annotation(guard)")
  public Object guard(ProceedingJoinPoint pjp, UserTypeGuard guard) throws Throwable {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

    if (!userTypeChecker.check(authentication, guard.value())) {
      throw new ForbiddenException();
    }

    return pjp.proceed();
  }
}
