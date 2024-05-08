package ru.slevyns.word_calculator.service.words;

import ru.slevyns.word_calculator.dto.DirRequest;
import ru.slevyns.word_calculator.dto.DirResponse;

public interface WordCalcService {
    /**
     * Handle request and count words
     *
     * @param request params for directory
     * @return a Set of filtered words from files
     */
    DirResponse countWords(DirRequest request);
}
