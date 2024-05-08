package ru.slevyns.word_calculator.service.matcher;

import ru.slevyns.word_calculator.dto.Word;

import java.util.Set;

public interface TextRegexMatcherService {
    int DEFAULT_MIN_WORD_LENGTH = 4;

    /**
     * Find words that matches regEx in input file.
     *
     * @param text input file text
     * @return a Set of all words in file
     */
    Set<Word> findMatches(String text);

    /**
     * Changes minLength from default.
     *
     * @param newMinLength new minLength value
     */
    void setMinLength(int newMinLength);
}
