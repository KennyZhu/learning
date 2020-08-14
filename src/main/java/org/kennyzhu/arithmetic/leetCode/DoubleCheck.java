package org.kennyzhu.arithmetic.leetCode;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * desc:双重检查线程安全
 * author: yanlongzhu
 * date:2020/8/14.
 */
public class DoubleCheck {
    private static volatile DoubleCheck instance;
    private static final Logger LOGGER = LoggerFactory.getLogger(DoubleCheck.class);

    public static DoubleCheck getInstance() {
        if (instance == null) {
            synchronized (DoubleCheck.class) {
                if (instance == null) {
                    instance = new DoubleCheck();
                }
            }
        }
        LOGGER.info(instance + "");
        return instance;
    }

    private DoubleCheck() {

    }

    public static void main(String[] args) {
        ExecutorService executorService = Executors.newFixedThreadPool(10);
        for (int i = 0; i < 20; i++)
            executorService.submit(DoubleCheck::getInstance);
    }
}
