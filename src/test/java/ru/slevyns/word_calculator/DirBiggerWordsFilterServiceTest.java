package ru.slevyns.word_calculator;

import org.junit.jupiter.api.Test;
import ru.slevyns.word_calculator.dto.Word;
import ru.slevyns.word_calculator.service.filter.FilterService;
import ru.slevyns.word_calculator.service.filter.impl.BiggerWordsFilterService;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;

class DirBiggerWordsFilterServiceTest {
    private final FilterService filterService = new BiggerWordsFilterService();

    @Test
    void filterWords_DependsOnChangedTopNum_returnTopNumWords() {
        filterService.changeTopWordsNum(2);
        var inputWords = fillWordSet();

        var result = filterService.filter(inputWords);

        assertEquals(2, result.size());
    }

    @Test
    void filterWords_DependsOnDefaultTopNum_returnTopNumWords() {
        var inputWords = fillWordSet();

        var result = filterService.filter(inputWords);

        assertEquals(10, result.size());
    }

    private Set<Word> fillWordSet() {
        var word1 = new Word("one", 3);
        var word2 = new Word("two", 2);
        var word3 = new Word("three", 4);
        var word4 = new Word("four", 2);
        var word5 = new Word("fifth", 5);
        var word6 = new Word("six", 3);
        var word7 = new Word("seven", 4);
        var word8 = new Word("eight", 7);
        var word9 = new Word("nine", 8);
        var word10 = new Word("ok", 10);
        var word11 = new Word("here", 1);

        return Set.of(
                word1,
                word2,
                word3,
                word4,
                word5,
                word6,
                word7,
                word8,
                word9,
                word10,
                word11
                );
    }
}