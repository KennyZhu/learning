package org.kennyzhu.common.nio.zero_copy;

import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.nio.ByteBuffer;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;

/**
 * Desc:
 * <p/>Date: 2015/4/22
 * <br/>Time: 10:57
 * <br/>User: ylzhu
 */
public class TransferToServer {
    ServerSocketChannel listener = null;

    public void setUp() {
        InetSocketAddress address = new InetSocketAddress(7000);
        try {
            listener = ServerSocketChannel.open();
            ServerSocket serverSocket = listener.socket();
            serverSocket.setReuseAddress(true);
            serverSocket.bind(address);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void readData() {
        ByteBuffer buffer = ByteBuffer.allocate(4096);
        try {
            while (true) {
                SocketChannel conn = listener.accept();
                conn.configureBlocking(true);
                int read = 0;
                while (read != -1) {
                    read = conn.read(buffer);
                    buffer.rewind();
                }
            }
        } catch (Exception e) {

        }
    }

    public static void main(String[] args) {
        TransferToServer server = new TransferToServer();
        server.setUp();
        server.readData();
    }
}
