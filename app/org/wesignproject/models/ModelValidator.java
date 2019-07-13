package org.wesignproject.models;

import java.util.List;
import java.util.stream.Collectors;
import javax.inject.Singleton;
import javax.validation.Validation;
import javax.validation.Validator;


/**
 * Wrapper for {@link Validator}
 */
@Singleton
public class ModelValidator {
  private static final Validator VALIDATOR = Validation.buildDefaultValidatorFactory().getValidator();

  public <T> List<String> validateAndGetViolations(T entity) {
    return VALIDATOR.validate(entity).stream()
        .map(violation -> String.format("%s %s", violation.getPropertyPath(), violation.getMessage()))
        .collect(Collectors.toList());
  }
}
