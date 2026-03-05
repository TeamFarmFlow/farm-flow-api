package com.example.app.core.guard;

import lombok.RequiredArgsConstructor;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Aspect
@Component
@RequiredArgsConstructor
public class UserTypeGuardAspect {
  //
  //  private final UserTypeChecker userTypeChecker;
  //
  //  @Around("@annotation(guard)")
  //  public Object guard(ProceedingJoinPoint pjp, UserTypeGuard guard) throws Throwable {
  //    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
  //
  //    if (!userTypeChecker.check(authentication, guard.value())) {
  //      throw new ForbiddenException();
  //    }
  //
  //    return pjp.proceed();
  //  }
}
