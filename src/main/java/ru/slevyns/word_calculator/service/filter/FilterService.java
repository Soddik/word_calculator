package ru.slevyns.word_calculator.service.filter;

import ru.slevyns.word_calculator.dto.Word;

import java.util.Set;

public interface FilterService {
    int DEFAULT_TOP_NUM = 10;

    /**
     * Filters all words
     *
     * @param words all words from files that match regEx
     * @return a Set of filtered words
     */
    Set<Word> filter(Set<Word> words);

    /**
     * Changes topN quantity
     *
     * @param topWordsNum new topN quantity value     *
     */
    void changeTopWordsNum(int topWordsNum);
}
