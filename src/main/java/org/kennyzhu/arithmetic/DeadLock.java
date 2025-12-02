package org.kennyzhu.arithmetic;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.CountDownLatch;

/**
 * 死锁演示代码
 * 
 * 死锁的四个必要条件：
 * 1. 互斥条件：资源不能被多个线程共享
 * 2. 持有并等待：线程持有资源的同时等待其他资源
 * 3. 不可剥夺：资源不能被强制从线程中夺走
 * 4. 循环等待：存在线程资源的循环等待链
 * 
 * 本示例演示：
 * - 线程1：先获取lock1，再获取lock2
 * - 线程2：先获取lock2，再获取lock1
 * - 结果：形成循环等待，导致死锁
 * 
 * Created by yanlongzhu on 2016/11/1.
 */
public class DeadLock {
    private static final Logger LOGGER = LoggerFactory.getLogger(DeadLock.class);
    
    // 锁对象1
    private Object lock1 = new Object();
    // 锁对象2
    private Object lock2 = new Object();

    /**
     * 构造函数：使用外部提供的锁对象
     */
    public DeadLock(Object obj1, Object obj2) {
        this.lock1 = obj1;
        this.lock2 = obj2;
    }

    /**
     * 默认构造函数：创建新的锁对象
     */
    public DeadLock() {

    }

    /**
     * 运行方法1：先获取lock1，再获取lock2
     * 
     * 执行流程：
     * 1. 获取lock1锁
     * 2. 休眠1秒（增加死锁概率）
     * 3. 尝试获取lock2锁（如果lock2被run2持有，则阻塞）
     */
    public void run1() {
        synchronized (lock1) {  // 第一步：获取lock1
            LOGGER.info("#Run1 get lock1.");
            try {
                // 休眠1秒，让线程2有机会获取lock2
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                // 恢复中断状态
                Thread.currentThread().interrupt();
                LOGGER.warn("#Run1 interrupted", e);
                return;
            }
            synchronized (lock2) {  // 第二步：尝试获取lock2（可能死锁）
                LOGGER.info("#Run1 begin to run");
            }
        }
    }

    /**
     * 运行方法2：先获取lock2，再获取lock1
     * 
     * 执行流程：
     * 1. 获取lock2锁
     * 2. 休眠1秒（增加死锁概率）
     * 3. 尝试获取lock1锁（如果lock1被run1持有，则阻塞）
     * 
     * 死锁发生：
     * - run1持有lock1，等待lock2
     * - run2持有lock2，等待lock1
     * - 形成循环等待，死锁！
     */
    public void run2() {
        synchronized (lock2) {  // 第一步：获取lock2
            LOGGER.info("#Run2 get lock2.");
            try {
                // 休眠1秒，让线程1有机会获取lock1
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                // 恢复中断状态
                Thread.currentThread().interrupt();
                LOGGER.warn("#Run2 interrupted", e);
                return;
            }
            synchronized (lock1) {  // 第二步：尝试获取lock1（可能死锁）
                LOGGER.info("#Run2 begin to run");
            }
        }
    }


    /**
     * 主方法：演示死锁的发生
     * 
     * 运行结果：
     * - 程序会卡住不结束
     * - 两个线程互相等待对方释放锁
     * 
     * 如何检测死锁：
     * 1. jps 查看进程ID
     * 2. jstack <pid> 查看线程堆栈
     * 3. 会看到 "Found 1 deadlock" 的提示
     * 
     * 解决方案：
     * 1. 统一加锁顺序（都先lock1后lock2）
     * 2. 使用tryLock()方法设置超时
     * 3. 使用Lock接口的更高级功能
     */
    public static void main(String[] args) {

        DeadLock deadLock = new DeadLock();
        
        // 使用CountDownLatch等待两个线程完成（实际上会死锁，无法完成）
        CountDownLatch countDownLatch = new CountDownLatch(2);
        
        // 启动线程1：执行run1()
        new Thread(() -> {
            deadLock.run1();
            countDownLatch.countDown();  // 永远不会执行到这里
        }).start();

        // 启动线程2：执行run2()
        new Thread(() -> {
            deadLock.run2();
            countDownLatch.countDown();  // 永远不会执行到这里
        }).start();
        
        try {
            // 等待两个线程完成（会一直等待，因为发生了死锁）
            // 设置超时时间，避免无限等待
            if (!countDownLatch.await(5, java.util.concurrent.TimeUnit.SECONDS)) {
                LOGGER.error("Deadlock detected! Threads are stuck after 5 seconds.");
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            LOGGER.error("Main thread interrupted", e);
        }
    }
}
