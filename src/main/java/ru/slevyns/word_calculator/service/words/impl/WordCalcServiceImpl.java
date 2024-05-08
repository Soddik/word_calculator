package ru.slevyns.word_calculator.service.words.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import ru.slevyns.word_calculator.dto.DirRequest;
import ru.slevyns.word_calculator.dto.DirResponse;
import ru.slevyns.word_calculator.mapper.DirMapper;
import ru.slevyns.word_calculator.service.file.FileService;
import ru.slevyns.word_calculator.service.filter.FilterService;
import ru.slevyns.word_calculator.service.validation.ValidationService;
import ru.slevyns.word_calculator.service.words.WordCalcService;

import java.util.HashSet;

import static ru.slevyns.word_calculator.service.filter.FilterService.DEFAULT_TOP_NUM;

@Service
public class WordCalcServiceImpl implements WordCalcService {
    private static final Logger log = LoggerFactory.getLogger(WordCalcServiceImpl.class);
    private final FileService fileService;
    private final FilterService filterService;
    private final ValidationService<DirRequest> validationService;
    private final DirMapper mapper;

    public WordCalcServiceImpl(FileService fileService, FilterService filterService, ValidationService<DirRequest> validationService, DirMapper mapper) {
        this.fileService = fileService;
        this.filterService = filterService;
        this.validationService = validationService;
        this.mapper = mapper;
    }

    @Override
    public DirResponse countWords(DirRequest request) {
        var validationResults = validationService.validate(request);
        if (!validationResults.isEmpty()) {
            return mapper.toResponse(new HashSet<>(), validationResults);
        }

        log.info("Parse directory path");
        var path = mapper.toDirectoryPath(request);
        var wordMinLength = request.minLength();

        fileService.changeMinWordLength(wordMinLength);
        log.info("Word minimum length: {}", wordMinLength);

        log.info("Files processing start...");
        var wordCollection = fileService.processFiles(path);
        log.info("Files processing complete.");

        var topN = request.topN();
        if (topN != DEFAULT_TOP_NUM) {
            filterService.changeTopWordsNum(topN);
            log.info("Top words number changed on: {}", topN);
        }

        var filtered = filterService.filter(wordCollection);
        log.info("Words filtered.");

        return mapper.toResponse(filtered, new HashSet<>());
    }
}
