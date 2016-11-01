package org.kennyzhu.arithmetic;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.CountDownLatch;

/**
 * Created by yanlongzhu on 2016/11/1.
 */
public class DeadLock {
    private static final Logger LOGGER = LoggerFactory.getLogger(DeadLock.class);
    private Object lock1 = new Object();
    private Object lock2 = new Object();

    public DeadLock(Object obj1, Object obj2) {
        this.lock1 = obj1;
        this.lock2 = obj2;
    }

    public DeadLock() {

    }

    public void run1() {
        synchronized (lock1) {
            LOGGER.info("#Run1 get lock1.");
            try {
                Thread.sleep(1000);
            } catch (Exception e) {

            }
            synchronized (lock2) {
                LOGGER.info("#Run1 begin to run");
            }
        }
    }

    public void run2() {
        synchronized (lock2) {
            LOGGER.info("#Run2 get lock2.");
            try {
                Thread.sleep(1000);
            } catch (Exception e) {

            }
            synchronized (lock1) {
                LOGGER.info("#Run2 begin to run");
            }
        }
    }


    public static void main(String[] args) {

        DeadLock deadLock = new DeadLock();
        CountDownLatch countDownLatch = new CountDownLatch(2);
        new Thread(() -> {
            deadLock.run1();
            countDownLatch.countDown();
        }).start();

        new Thread(() -> {
            deadLock.run2();
            countDownLatch.countDown();
        }).start();
        try {
            countDownLatch.await();
        } catch (Exception e) {

        }
    }
}
