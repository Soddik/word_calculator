package ru.slevyns.word_calculator.dto;

import java.util.Set;

public record DirResponse(Set<Word> words, Set<ValidationResult> errors) {
}
