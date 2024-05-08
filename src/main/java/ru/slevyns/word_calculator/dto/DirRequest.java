package ru.slevyns.word_calculator.dto;

public record DirRequest(String dirPath, int minLength, int topN) {
}