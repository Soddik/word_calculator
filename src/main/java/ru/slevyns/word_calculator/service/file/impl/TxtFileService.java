package ru.slevyns.word_calculator.service.file.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import ru.slevyns.word_calculator.dto.AllowedExtension;
import ru.slevyns.word_calculator.dto.Word;
import ru.slevyns.word_calculator.service.executor.CustomExecutorService;
import ru.slevyns.word_calculator.service.file.BaseFileService;
import ru.slevyns.word_calculator.service.matcher.TextRegexMatcherService;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collection;
import java.util.Set;
import java.util.concurrent.Future;
import java.util.stream.Collectors;

import static ru.slevyns.word_calculator.dto.AllowedExtension.TXT;

@Service
public class TxtFileService extends BaseFileService {
    private final TextRegexMatcherService textRegexMatcherService;
    private final CustomExecutorService executorService;
    private final Logger log = LoggerFactory.getLogger(TxtFileService.class);
    private final AllowedExtension txt = TXT;

    public TxtFileService(TextRegexMatcherService textRegexMatcherService, CustomExecutorService executorService) {
        this.textRegexMatcherService = textRegexMatcherService;
        this.executorService = executorService;
    }

    @Override
    public Set<Word> processFiles(Path directoryPath) {
        log.info("Preparing files in directory {}", directoryPath);
        var filesQueue = prepareFileQueue(directoryPath);
        log.info("Files prepared: {}", filesQueue.size());

        executorService.setThreadsNum(filesQueue.size());

        var allWords = filesQueue.stream()
                .map(f -> executorService.submitTask(() -> processFile(f)))
                .collect(Collectors.toUnmodifiableSet());

        executorService.start();
        log.info("Executor service started");

        try {
            executorService.awaitCompletion();
            log.info("All words processed");
        } catch (Exception e) {
            log.error("Error while processing files", e);
            throw new RuntimeException(e);
        }
        log.info("Starting merge");
        return merge(allWords);
    }

    @Override
    public void changeMinWordLength(int minWordLength) {
        this.textRegexMatcherService.setMinLength(minWordLength);
    }

    @Override
    protected Set<Word> processFile(File file) {
        try (var reader = Files.newBufferedReader(Paths.get(file.getPath()))) {
            log.info("Processing file {}", file);
            return reader.lines()
                    .map(textRegexMatcherService::findMatches)
                    .flatMap(Collection::stream)
                    .collect(Collectors.toUnmodifiableSet());
        } catch (Exception e) {
            log.error("Error while processing file {}", file.getPath(), e);
            throw new RuntimeException(e);
        }
    }

    @Override
    protected Set<Word> merge(Set<Future<Set<Word>>> futures) {
        return futures.stream()
                .flatMap(f -> {
                    try {
                        return f.get().stream();
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                })
                .collect(Collectors.groupingBy(Word::word, Collectors.summingInt(Word::count)))
                .entrySet().stream()
                .map(e -> new Word(e.getKey(), e.getValue()))
                .collect(Collectors.toUnmodifiableSet());
    }

    protected boolean isValidExtension(File file) {
        var fileName = file.getName();
        var fileExtension = fileName.substring(fileName.indexOf(DELIMITER) + 1);
        return txt.isEqual(fileExtension);
    }
}

