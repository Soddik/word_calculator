package ru.slevyns.word_calculator.dto;

public record ValidationResult(String errorMessage, String paramName) {
    @Override
    public String toString() {
        return "param: " + paramName + ", error: " + errorMessage;
    }
}