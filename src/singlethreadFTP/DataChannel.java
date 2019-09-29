package singlethreadFTP;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * @auther Steven J
 * @createDate 2019-09-27  14:39
 */
public class DataChannel {
    private ServerSocket lisener;
    Socket client001;

    public DataChannel() throws IOException {
        this.lisener = new ServerSocket(8888);
        System.out.println("创建了监听段,默认端口8888");
        DataRead();
    }

    public DataChannel(int port) throws IOException {
        this.lisener = new ServerSocket(port);
        System.out.println("建立了新的监听端口" + port);
        DataRead();
    }

    public void acceptClient() {
        try {
            System.out.println("等待接收链接");
            client001 = lisener.accept();
            System.out.println("一个客户端建立了连接");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void DataRead() {
        acceptClient();
        try {
            InputStream is = client001.getInputStream();
            /*int len=0;
            byte[] car = new byte[1024*64];
            while((len=is.read(car))!=-1){
                String msg = new String(car,0,len);
                System.out.println(msg);
            }*/
            // 试试用reader：
            BufferedReader br = new BufferedReader(new InputStreamReader(is, "utf8"));
            String receiveMsg;
            while ((receiveMsg = br.readLine()) != null) {
                System.out.println(receiveMsg);
            }
            is.close();
            br.close();
            client001.close();
            lisener.close();
            System.gc();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void recieveFile() {

    }
}
