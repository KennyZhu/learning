package org.kennyzhu.juc;

import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * desc:
 * author: yanlongzhu
 * date:2019-02-22.
 */
public class BusyLock {
    private int count;
    // 自旋锁状态
    private AtomicInteger state;
    //最大退让时间 10 毫秒
    private static final int max_backoff_msg = 10;

    private BusyLock() {
        this.count = 0;
        this.state = new AtomicInteger(0);
    }

    /**
     * 利用 CAS 实现自旋锁,增加退让策略，减少锁竞争
     */
    private void lock() {
        //其实退让时间 1 毫秒。
        int backoff = 1;
        while (!state.compareAndSet(0, 1)) {
            //对于未能取得锁所有权的线程，在内层循环上等待
            //因为获取了 state 一份共享的高速缓存副本，
            //不会再进一步产生总线通信量
            while (state.get() == 1)
                ;

            //不是第一次，退让一下
            if (backoff > 1) {
                try {
                    Thread.sleep(backoff);
                } catch (Exception InterruptedException) {
                    Thread.currentThread().interrupt();
                }
            }
            //还没有超过最大退让时间，指数增加退让时间。
            if (backoff < max_backoff_msg) {
                backoff *= 2;
            }
        }
    }

    /**
     * 解锁
     */
    private void unlock() {
        state.set(0);
    }

    /**
     * 递增 count，使用自旋锁保护 count
     */
    public void incr() {
        lock();
        count = count + 1;
        unlock();
    }

    /**
     * 测试，开启 50 个线程递增 count，每个线程循环 10 万次。
     *
     * @param warnUp
     * @throws Exception
     */
    public static void runTests(boolean warnUp) throws Exception {
        int threads = 50;
        int n = 100000;
        BusyLock bl = new BusyLock();
        Thread[] ts = new Thread[threads];
        long start = System.nanoTime();
        CyclicBarrier barrier = new CyclicBarrier(threads + 1);
        for (int i = 0; i < threads; i++) {
            ts[i] = new Thread(new Runnable() {

                @Override
                public void run() {
                    try {
                        barrier.await();
                        for (int j = 0; j < n; j++)
                            bl.incr();
                        barrier.await();
                    } catch (Exception e) {

                    }
                }
            });
            ts[i].start();
        }
        barrier.await();
        barrier.await();
        for (Thread t : ts) {
            t.join();
        }
        long duration = (System.nanoTime() - start) / 1000000;
        if (!warnUp)
            System.out.println("count= " + bl.count + ",cost:" + duration
                    + "ms");

    }

    public static void main(String[] args) throws Exception {
        // 测试，先 warm up
        runTests(true);
        // 实际测试
        runTests(false);
    }
}
