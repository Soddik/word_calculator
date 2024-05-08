package ru.slevyns.word_calculator;

import org.junit.jupiter.api.Test;
import ru.slevyns.word_calculator.service.executor.CustomExecutorService;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

import static org.junit.jupiter.api.Assertions.*;

class CustomExecutorServiceTest {
    private final CustomExecutorService executorService = new CustomExecutorService();

    @Test
    void submitTask_startService_awaitCompletion() throws InterruptedException, TimeoutException, ExecutionException {
        Callable<Boolean> booleanCallable = () -> {
            Thread.sleep(2000);
            return true;
        };

        var booleanFuture = executorService.submitTask(booleanCallable);

        executorService.start();

        executorService.awaitCompletion();

        var bool = booleanFuture.get();
        assertTrue(bool);
    }

    @Test
    void submitMultipleTasks_startService_awaitCompletion() throws InterruptedException, TimeoutException, ExecutionException {
        Callable<Boolean> callableTrue = () -> {
            Thread.sleep(1000);
            return true;
        };

        Callable<Boolean> callableFalse = () -> {
            Thread.sleep(1000);
            return false;
        };

        var trueFuture = executorService.submitTask(callableTrue);
        var falseFuture = executorService.submitTask(callableFalse);

        executorService.start();

        executorService.awaitCompletion();

        var trueResult = trueFuture.get();
        var falseResult = falseFuture.get();

        assertTrue(trueResult);
        assertFalse(falseResult);
    }

    @Test
    void submitTask_startServiceWithValidThreadsNum_awaitSuccessCompletion() throws InterruptedException, TimeoutException, ExecutionException {
        Callable<Boolean> booleanCallable = () -> {
            Thread.sleep(2000);
            return true;
        };



        var booleanFuture = executorService.submitTask(booleanCallable);

        executorService.setThreadsNum(2);
        executorService.start();
        executorService.awaitCompletion();

        var bool = booleanFuture.get();
        assertTrue(bool);
    }

    @Test
    void submitTaskWithException_startService_exceptionThrown() {
        Callable<Boolean> booleanCallable = () -> {
            Thread.sleep(2000);
            throw new RuntimeException("fail");
        };

        var booleanFuture = executorService.submitTask(booleanCallable);

        executorService.start();

        assertThrows(ExecutionException.class, booleanFuture::get);
    }

    @Test
    void setServiceInvalidThreadNum_throwsException() {
        assertThrows(RuntimeException.class, () -> executorService.setThreadsNum(-1));
    }
}