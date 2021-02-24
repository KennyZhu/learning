package org.kennyzhu.juc;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.util.Date;
import java.util.concurrent.DelayQueue;
import java.util.concurrent.Delayed;
import java.util.concurrent.TimeUnit;

/**
 *
 */
@Slf4j
public class DelayQueueTest {


    public DelayQueueTest() {
    }

    public static void main(String[] args) {
        DelayQueue<DelayedTask> delayQueue = new DelayQueue<DelayedTask>();
        DelayedTask task = new DelayedTask(5L);
        log.info("#Before putCurrentTime is " + new Date());
        delayQueue.put(task);
        try {
            delayQueue.take().print();
        } catch (Exception e) {
            log.error("Exception e", e);
        }
    }


    @Data
    @Slf4j
    static class DelayedTask implements Delayed {
        private Long runAt;//延迟时间，单位s


        DelayedTask(Long runAt) {
            this.runAt = runAt;
        }

        public void print() {
            log.info(new StringBuilder().append("#After CurrentTime is ").append(new Date()).toString());
        }

        @Override
        public long getDelay(TimeUnit unit) {
            return runAt;
        }

        @Override
        public int compareTo(Delayed o) {
            DelayedTask object = (DelayedTask) o;
            return this.runAt.compareTo(object.getRunAt());
        }
    }
}
