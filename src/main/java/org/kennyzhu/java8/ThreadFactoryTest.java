package org.kennyzhu.java8;

import lombok.extern.slf4j.Slf4j;

import java.util.Date;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Slf4j
public class ThreadFactoryTest {

    private ScheduledExecutorService service = Executors.newScheduledThreadPool(5);


    public void test() {
        service.scheduleAtFixedRate(() -> {
            log.info("#CurrentTimei is {}", new Date());
        }, 1, 5, TimeUnit.SECONDS);
    }

    public static void main(String[] args) {
        ThreadFactoryTest obj = new ThreadFactoryTest();
        obj.test();
    }


}
