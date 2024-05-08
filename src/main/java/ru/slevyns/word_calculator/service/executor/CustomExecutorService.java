package ru.slevyns.word_calculator.service.executor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.concurrent.*;

@Component
public class CustomExecutorService {
    private static final int DEFAULT_THREADS_NUM = Runtime.getRuntime().availableProcessors() - 1;
    private final BlockingQueue<FutureTask<?>> queue = new LinkedBlockingQueue<>();
    private final Phaser phaser = new Phaser(1);
    private final Logger log = LoggerFactory.getLogger(CustomExecutorService.class);
    private boolean isRunning;
    private int threadsNum = DEFAULT_THREADS_NUM;

    public void start() {
        isRunning = true;
        for (var i = 0; i < threadsNum; i++) {
            var thread = new Thread(() -> {
                while (isRunning) {
                    var task = queue.poll();
                    if (task != null) {
                        try {
                            task.run();
                            phaser.arriveAndDeregister();
                        } catch (Exception e) {
                            log.error("Error executing task", e);
                            throw new RuntimeException(e);
                        }
                    }
                }
            });
            thread.start();
        }
    }

    public <T> Future<T> submitTask(Callable<T> task) {
        var futureTask = new FutureTask<>(task);
        var offered = queue.offer(futureTask);
        if (offered) {
            phaser.register();
        } else {
            log.error("Task queue is full");
            throw new UnsupportedOperationException("Task queue is full");
        }
        return futureTask;
    }

    public void awaitCompletion() throws InterruptedException, TimeoutException {
        awaitCompletion(300, TimeUnit.SECONDS);
    }

    public void awaitCompletion(long timeout, TimeUnit unit) throws InterruptedException, TimeoutException {
        try {
            phaser.awaitAdvanceInterruptibly(phaser.arrive(), timeout, unit);
        } finally {
            close();
        }
    }

    public void close() {
        isRunning = false;
        threadsNum = DEFAULT_THREADS_NUM;
    }

    public void setThreadsNum(int threadsNum) {
        if (threadsNum > 0 && threadsNum < this.threadsNum) {
            this.threadsNum = threadsNum;
        } else {
            throw new RuntimeException("Number of threads must be greater than 0");
        }
    }
}

