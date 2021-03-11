package org.kennyzhu.juc;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * 线程池的四种拒绝策略：
 * 1、CallerRunsPolicy：直接使用提交任务的线程执行，如直接使用主线程执行
 * 2、AbortPolicy：中断策略，直接拒绝执行并抛出异常
 * 3、DiscardPolicy：丢弃策略，丢弃任务，并不会有任何异常抛出
 * 4、DiscardOldestPolicy：丢弃队列头最老的任务，然后提交当前任务
 */
@Slf4j
public class ThreadFactoryRejectPolicyTest {

    private final ThreadPoolExecutor executor = new ThreadPoolExecutor(1, 1, 1000, TimeUnit.SECONDS, new ArrayBlockingQueue<>(2), r -> null, new ThreadPoolExecutor.DiscardOldestPolicy());

    public static void main(String[] args) {
        ThreadFactoryRejectPolicyTest rejectPolicyTest = new ThreadFactoryRejectPolicyTest();
        log.info("#Main thread is {}", Thread.currentThread().getName());
        rejectPolicyTest.executor.submit(() -> {
            log.info("##Thread 1 is {}", Thread.currentThread().getName());
            try {
                Thread.sleep(1000);
            } catch (Exception e) {
                log.error("", e);
            }
        });
        rejectPolicyTest.executor.submit(() -> {
            log.info("Thread 2 is {}", Thread.currentThread().getName());
        });
        try {
            Thread.sleep(90000);
        } catch (Exception e) {

        }
    }

}
