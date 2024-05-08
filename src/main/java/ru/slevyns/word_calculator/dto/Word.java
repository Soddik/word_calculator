package ru.slevyns.word_calculator.dto;

import java.util.Objects;

public record Word(String word, int count) {
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Word word1)) return false;

        return count == word1.count && Objects.equals(word, word1.word);
    }

    @Override
    public int hashCode() {
        int result = Objects.hashCode(word);
        result = 31 * result + count;
        return result;
    }

    @Override
    public String toString() {
        return "Word{" +
                "word='" + word + '\'' +
                ", count=" + count +
                '}';
    }
}

