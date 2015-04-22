package org.kennyzhu.common.nio.zero_copy;

import java.io.FileInputStream;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.channels.FileChannel;
import java.nio.channels.SocketChannel;

/**
 * Desc:
 * <p/>Date: 2015/4/22
 * <br/>Time: 10:56
 * <br/>User: ylzhu
 */
public class TransferToClient {

    public void sendFile() {
        String host = "localhost";
        int port = 7000;
        try {


            SocketAddress address = new InetSocketAddress(host, port);
            SocketChannel channel = SocketChannel.open();
            channel.connect(address);
            channel.configureBlocking(true);
            FileChannel fc = new FileInputStream("sendfile").getChannel();
            long start = System.currentTimeMillis();
            long fsize = 183678375L, sendzise = 4094;
            long curnset = 0;
            curnset = fc.transferTo(0, fsize, channel);
            System.out.println("total bytes transferred--" + curnset + " and time taken in MS--" + (System.currentTimeMillis() - start));

        } catch (Exception e) {

        }
    }
}
