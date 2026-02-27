package com.example.app.core.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = MatchValueValidator.class)
@Documented
public @interface MatchValue {
  String message() default "{match.value}";

  Class<?>[] groups() default {};

  Class<? extends Payload>[] payload() default {};

  String first();

  String second();

  boolean ignoreNull() default true;

  boolean ignoreCase() default false;
}
