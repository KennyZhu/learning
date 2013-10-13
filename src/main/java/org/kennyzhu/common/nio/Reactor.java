package org.kennyzhu.common.nio;

import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

/**
 * Created with IntelliJ IDEA.
 * User: Kenny
 * Date: 13-10-13
 * Time: 上午11:59
 */
public class Reactor implements Runnable {
    final Selector selector;
    final ServerSocketChannel serverSocketChannel;

    public Reactor(int port) throws Exception {
        selector = Selector.open();
        serverSocketChannel = ServerSocketChannel.open();
        serverSocketChannel.socket().bind(new InetSocketAddress(port));
        serverSocketChannel.configureBlocking(false);
        //注册到selector选择器上，SelectionKey.OP_ACCEPT为serverSocketChannel关心的事件
        SelectionKey sk = serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
        sk.attach(new Acceptor());
    }


    public void dispatch(SelectionKey key) {
        Runnable r = (Runnable) key.attachment();
        if (r != null) {
            r.run();
        }
    }

    @Override
    public void run() {
        try {
            while (!Thread.interrupted()) {
                selector.select();
                //就绪的Channel
                Set<SelectionKey> selected = selector.selectedKeys();
                Iterator<SelectionKey> iterator = selected.iterator();
                while (iterator.hasNext()) {
                    //分发
                    dispatch((SelectionKey) iterator.next());
                }
                selected.clear();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * 接收请求，处理请求
     */
    class Acceptor implements Runnable {
        @Override
        public void run() {
            try {
                SocketChannel socket = serverSocketChannel.accept();
                if (socket != null) {
                    new SocketReadHandler(selector, socket);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
