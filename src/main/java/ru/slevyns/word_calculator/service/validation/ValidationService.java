package ru.slevyns.word_calculator.service.validation;

import ru.slevyns.word_calculator.dto.ValidationResult;

import java.util.Set;

/**
 * Validation service for requests
 */
public interface ValidationService<R> {
    Set<ValidationResult> validate(R r);
}
