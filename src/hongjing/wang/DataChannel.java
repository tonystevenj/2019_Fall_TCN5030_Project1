package hongjing.wang;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * @auther Steven J
 * @createDate 2019-09-27  14:39
 */
public class DataChannel implements Runnable{
    private ServerSocket client01;

    public DataChannel() throws IOException {
        client01 = new ServerSocket(8888);
        System.out.println("创建了监听段");
    }
    public DataChannel(int port) throws IOException {
        client01 = new ServerSocket(port);
    }

    @Override
    public void run() {
        Socket client001 = null;
        try {
            System.out.println("等待接收链接");
            client001 = client01.accept();
            System.out.println("一个客户端建立了连接");
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            ReceiveFromServer rf1 = new ReceiveFromServer(client001.getInputStream());
            rf1.run();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void recieveFile(){

    }
}
