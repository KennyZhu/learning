package org.kennyzhu.common.nio.zero_copy;

import java.io.DataInputStream;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Desc:Traditional Server
 * <p/>Date: 2015/4/22
 * <br/>Time: 10:40
 * <br/>User: KennyZhu
 */
public class TraditionalServer {

    static void main(String[] args) {
        int port = 7000;
        ServerSocket serverSocket = null;
        DataInputStream inputStream = null;
        try {
            serverSocket = new ServerSocket(port);
            while (true) {
                Socket socket = serverSocket.accept();
                inputStream = new DataInputStream(socket.getInputStream());
                byte[] buffer = new byte[4096];
                while (true) {
                    int read = inputStream.read(buffer, 0, 4096);
                    if (0 == read) {
                        break;
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                inputStream.close();
                serverSocket.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
