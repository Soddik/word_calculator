package ru.slevyns.word_calculator.service.file;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.slevyns.word_calculator.dto.Word;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Set;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Future;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.stream.Collectors;

public abstract class BaseFileService implements FileService {
    private final Logger log = LoggerFactory.getLogger(BaseFileService.class);
    protected static final String DELIMITER = ".";

    /**
     * Process one file
     *
     * @param file input file
     * @return a Set of words from file
     */
    protected abstract Set<Word> processFile(File file);

    /**
     * Merge sets of words from all files
     *
     * @param futures set of futures from all files
     * @return result of merge as Set
     */
    protected abstract Set<Word> merge(Set<Future<Set<Word>>> futures);

    /**
     * Validate file extension
     *
     * @param file input file
     * @return true if file have supported extension
     */
    protected abstract boolean isValidExtension(File file);

    /**
     * Validate file
     *
     * @param file input file
     * @return true if file is file and file have supported extension
     */
    protected boolean isValidFile(File file) {
        return file.isFile() && isValidExtension(file);
    }

    /**
     * Prepares files queue
     *
     * @param directoryPath path for files directory
     * @return a queue of files
     */
    protected BlockingQueue<File> prepareFileQueue(Path directoryPath) {
        try (var stream = Files.list(directoryPath)) {
            return stream
                    .map(Path::toFile)
                    .filter(this::isValidFile)
                    .collect(Collectors.toCollection(LinkedBlockingQueue::new));
        } catch (Exception e) {
            log.error("Error while preparing files in folder \"{}\" occurred", directoryPath);
            throw new RuntimeException(e);
        }
    }
}

