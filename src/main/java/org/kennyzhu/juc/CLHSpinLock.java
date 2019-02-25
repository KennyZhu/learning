package org.kennyzhu.juc;

import java.util.concurrent.atomic.AtomicReference;

/**
 * desc:CLH 自旋锁
 * author: yanlongzhu
 * date:2019-02-25.
 */
public class CLHSpinLock {
    private final ThreadLocal<Node> prev;
    private final ThreadLocal<Node> node;
    private final AtomicReference<Node> tail = new AtomicReference<>(new Node());

    public CLHSpinLock() {
        this.node = ThreadLocal.withInitial(Node::new);
        this.prev = ThreadLocal.withInitial(() -> null);
    }

    public void lock() {
        final Node node = this.node.get();
        node.locked = true;
        // 一个CAS操作即可将当前线程对应的节点加入到队列中，
        // 并且同时获得了前继节点的引用，然后就是等待前继释放锁
        Node pred = this.tail.getAndSet(node);
        this.prev.set(pred);
        while (pred.locked) {// 进入自旋
        }
    }

    public void unlock() {
        final Node node = this.node.get();
        node.locked = false;
        this.node.set(this.prev.get());
    }

    private static class Node {
        private volatile boolean locked;
    }

    public static void main(String[] args) throws Exception {
        final CLHSpinLock lock = new CLHSpinLock();
        lock.lock();

        for (int i = 0; i < 10; i++) {
            new Thread(() -> {
                lock.lock();
                System.out.println(Thread.currentThread().getId() + " acquired the lock!");
                lock.unlock();
            }).start();
            Thread.sleep(100);
        }

        System.out.println("main thread unlock!");
        lock.unlock();
    }
}
