package org.kennyzhu.common.nio;

import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;

/**
 * Created with IntelliJ IDEA.
 * User: Kenny
 * Date: 13-10-13
 * Time: 下午6:23
 */
public class SocketReadHandler implements Runnable {
    final SocketChannel socket;
    final SelectionKey selectionKey;

    public SocketReadHandler(Selector selector, SocketChannel c) throws Exception {
        socket = c;
        socket.configureBlocking(false);
        selectionKey = socket.register(selector, 0);
        selectionKey.attach(this);
        selectionKey.interestOps(SelectionKey.OP_READ);
        selector.wakeup();
    }

    @Override
    public void run() {
        //TODO 请求处理逻辑代码

    }
}
