package ru.slevyns.word_calculator;

public interface DirTestMeta {
    //controller data
    String CONTROLLER_TEST_DIR_PATH = "src/test/resources/words/controller/";
    String SUCCESS_REDIRECT = "/directory/find/result";
    String ERROR_REDIRECT = "/directory/find/validation_error";
    String FIND_WORDS_PAGE = "directory/find/words";

    //counter data
    String COUNTER_TEST_DIR_PATH = "src/test/resources/words/count/";
    int CHANGED_MIN_TOP = 3;

    //file service
    String EMPTY_DIR_PATH = "src/test/resources/words/empty/";

    //common data
    int MIN_LENGTH = 4;
    int TOP_NUM = 10;

    String WORD_1 = "heremorethensix";
    String WORD_2 = "abracadabra";

    //regex matcher
    String TEXT_ENG = "car cars Carlson cat scatter splatter splitter son one two three four five";
    String TEXT_CYR = "один два три";
}
