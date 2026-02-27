package com.example.app.core.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.util.Objects;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.beans.BeansException;

public class MatchValueValidator implements ConstraintValidator<MatchValue, Object> {

  private String first;
  private String second;
  private boolean ignoreNull;
  private boolean ignoreCase;
  private String message;

  @Override
  public void initialize(MatchValue annotation) {
    this.first = annotation.first();
    this.second = annotation.second();
    this.ignoreNull = annotation.ignoreNull();
    this.ignoreCase = annotation.ignoreCase();
    this.message = annotation.message();
  }

  @Override
  public boolean isValid(Object target, ConstraintValidatorContext context) {
    if (target == null) return true;

    BeanWrapperImpl wrapper = new BeanWrapperImpl(target);

    Object firstValue;
    Object secondValue;
    try {
      firstValue = wrapper.getPropertyValue(first);
      secondValue = wrapper.getPropertyValue(second);
    } catch (BeansException e) {
      return false;
    }

    if (ignoreNull && (firstValue == null || secondValue == null)) {
      return true;
    }

    boolean matched;
    if (ignoreCase && firstValue instanceof String && secondValue instanceof String) {
      matched = ((String) firstValue).equalsIgnoreCase((String) secondValue);
    } else {
      matched = Objects.equals(firstValue, secondValue);
    }

    if (matched) {
      return true;
    }

    context.disableDefaultConstraintViolation();
    context
        .buildConstraintViolationWithTemplate(message)
        .addPropertyNode(second)
        .addConstraintViolation();

    return false;
  }
}
