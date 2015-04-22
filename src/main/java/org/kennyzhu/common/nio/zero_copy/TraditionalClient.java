package org.kennyzhu.common.nio.zero_copy;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.net.Socket;

/**
 * Desc:
 * <p/>Date: 2015/4/22
 * <br/>Time: 10:37
 * <br/>User: KennyZhu
 */
public class TraditionalClient {

    public static void main(String[] args) {
        String server = "localhost";
        int port = 7000;
        FileInputStream fileInputStream = null;
        DataOutputStream dataOutputStream = null;
        Socket socket = null;
        try {
            socket = new Socket(server, port);
            fileInputStream = new FileInputStream(new File("file"));
            dataOutputStream = new DataOutputStream(socket.getOutputStream());
            byte[] buffer = new byte[4096];
            long read = 0;
            long total = 0;
            while ((read = fileInputStream.read(buffer)) > 0) {
                total = total + read;
                dataOutputStream.write(buffer);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                fileInputStream.close();
                dataOutputStream.close();
                socket.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
