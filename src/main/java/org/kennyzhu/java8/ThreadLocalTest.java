package org.kennyzhu.java8;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by yanlongzhu on 2016/11/17.
 */
public class ThreadLocalTest {
    private ThreadLocal<Integer> threadCount = ThreadLocal.withInitial(() -> 0);
    private AtomicInteger count = new AtomicInteger(0);


    public ThreadLocal<Integer> getThreadCount() {
        return threadCount;
    }

    public AtomicInteger getCount() {
        return count;
    }

    public static void main(String[] args) {
        ThreadLocalTest test = new ThreadLocalTest();
        for (int num = 0; num < 5; num++) {
            Thread t1 = new Thread(() -> {
                for (int i = 0; i < 10; i++) {
                    test.getCount().addAndGet(1);
                    test.getThreadCount().set(test.getThreadCount().get() + 1);
                    System.out.println(Thread.currentThread().getName() + " count is " + test.getCount().get() + " threadCount is " + test.getThreadCount().get());
                }

            }, "Thread===" + num);
            t1.start();
        }

    }

}
