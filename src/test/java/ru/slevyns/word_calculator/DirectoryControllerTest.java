package ru.slevyns.word_calculator;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.ui.ConcurrentModel;
import ru.slevyns.word_calculator.controller.DirController;
import ru.slevyns.word_calculator.dto.DirRequest;
import ru.slevyns.word_calculator.dto.Word;
import ru.slevyns.word_calculator.dto.DirResponse;
import ru.slevyns.word_calculator.dto.ValidationResult;
import ru.slevyns.word_calculator.service.words.WordCalcService;

import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;
import static ru.slevyns.word_calculator.DirTestMeta.*;
import static ru.slevyns.word_calculator.util.DirValidationMeta.*;

@ExtendWith(MockitoExtension.class)
class DirectoryControllerTest {
    @Mock
    private WordCalcService wordCalcService;

    @InjectMocks
    private DirController dc;

    @Test
    void showWordsPage() {
        var result = dc.findWords();

        assertEquals(FIND_WORDS_PAGE, result);
    }

    @Test
    void findTopNWordsInDirectory_RequestIsValid_ReturnsRedirectionToResultPage() {
        var model = new ConcurrentModel();
        var request = new DirRequest(CONTROLLER_TEST_DIR_PATH, MIN_LENGTH, TOP_NUM);

        var words = new HashSet<>(Set.of(new Word(WORD_1, 1), new Word(WORD_2, 2)));
        var response = new DirResponse(words, new HashSet<>());
        doReturn(response)
                .when(wordCalcService)
                .countWords(request);

        var result = dc.findWords(model, request);

        assertEquals(SUCCESS_REDIRECT, result);

        verify(wordCalcService).countWords(request);
        verifyNoMoreInteractions(wordCalcService);
    }

    @Test
    void findTopNWordsInDirectory_RequestIsInvalid_ReturnsRedirectionToErrorPage() {
        var model = new ConcurrentModel();
        var request = new DirRequest("", -1, -1);

        var errors = fillErrors();

        var response = new DirResponse(null, errors);

        doReturn(response)
                .when(wordCalcService)
                .countWords(request);

        var result = dc.findWords(model, request);
        assertEquals(ERROR_REDIRECT, result);

        verify(wordCalcService).countWords(request);
        verifyNoMoreInteractions(wordCalcService);
    }

    private Set<ValidationResult> fillErrors() {
        return Set.of(
                new ValidationResult(DIR_PATH_ERROR, DIR_PATH_PARAM),
                new ValidationResult(
                        MUST_BE_GREATER_THAN_ERROR.formatted(MIN_WORD_PARAM),
                        MIN_WORD_PARAM),
                new ValidationResult(
                        MUST_BE_GREATER_THAN_ERROR.formatted(TOP_N_PARAM),
                        TOP_N_PARAM)
        );
    }
}