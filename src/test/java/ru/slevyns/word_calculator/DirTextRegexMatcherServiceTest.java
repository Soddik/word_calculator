package ru.slevyns.word_calculator;

import org.junit.jupiter.api.Test;
import ru.slevyns.word_calculator.dto.Word;
import ru.slevyns.word_calculator.service.matcher.TextRegexMatcherService;
import ru.slevyns.word_calculator.service.matcher.impl.TextRegexMatcherImpl;

import java.util.Objects;
import java.util.Set;
import java.util.function.Predicate;

import static org.junit.jupiter.api.Assertions.*;
import static ru.slevyns.word_calculator.DirTestMeta.TEXT_CYR;
import static ru.slevyns.word_calculator.DirTestMeta.TEXT_ENG;

class DirTextRegexMatcherServiceTest {

    private TextRegexMatcherService textRegexMatcher;

    @Test
    void findMatchingWords_DefaultLength_Collect() {
        textRegexMatcher = new TextRegexMatcherImpl();
        var words = textRegexMatcher.findMatches(TEXT_ENG);

        assertEquals(8, words.size());

        var isMoreThanThree = words.stream()
                .allMatch(w -> w.word().length() >= 4);
        assertTrue(isMoreThanThree);

        var containsCarlson = anyMatch(words, w -> "Carlson".equalsIgnoreCase(w.word()));
        assertTrue(containsCarlson);

        var containsScatter = anyMatch(words, w -> Objects.equals(w.word(), "scatter"));
        assertTrue(containsScatter);

        var containsSplatter = anyMatch(words, w -> Objects.equals(w.word(), "splatter"));
        assertTrue(containsSplatter);

        var containsSplitter = anyMatch(words, w -> Objects.equals(w.word(), "splitter"));
        assertTrue(containsSplitter);
    }

    @Test
    void findMatchingWords_ChangedLength_Collect() {
        textRegexMatcher = new TextRegexMatcherImpl(8);
        var words = textRegexMatcher.findMatches(TEXT_ENG);

        assertEquals(2, words.size());
        var isMoreThanSeven = words.stream()
                .allMatch(w -> w.word().length() > 7);
        assertTrue(isMoreThanSeven);

        var containsSplatter = anyMatch(words, w -> Objects.equals(w.word(), "splatter"));
        assertTrue(containsSplatter);

        var containsSplitter = anyMatch(words, w -> Objects.equals(w.word(), "splitter"));
        assertTrue(containsSplitter);
    }

    @Test
    void findMatchingWordsWithDuplicates_ChangedLength_Collect() {
        var text = "cat cat son one two";
        textRegexMatcher = new TextRegexMatcherImpl(3);
        var words = textRegexMatcher.findMatches(text);

        assertEquals(4, words.size());
        words.stream()
                .filter(w -> Objects.equals(w.word(), "cat"))
                .findFirst()
                .ifPresentOrElse(
                        w -> assertEquals(2, w.count()),
                        () -> fail("Should have found 2 words"));
    }

    @Test
    void findMatchingWordsCyrillic_ChangedLength_Collect() {
        textRegexMatcher = new TextRegexMatcherImpl(4);
        var words = textRegexMatcher.findMatches(TEXT_CYR);

        assertEquals(1, words.size());
        assertTrue(words.stream()
                .allMatch(w -> Objects.equals(w.word(), "один")));
    }

    private boolean anyMatch(Set<Word> words, Predicate<Word> predicate) {
        return words.stream()
                .anyMatch(predicate);
    }
}