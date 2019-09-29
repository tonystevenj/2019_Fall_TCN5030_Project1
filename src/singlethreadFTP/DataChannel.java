package singlethreadFTP;

import java.io.*;
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
    }

    public DataChannel(int port) throws IOException {
        this.lisener = new ServerSocket(port);
        System.out.println("建立了新的监听端口" + port);
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

    public void dataRead() {
        acceptClient();
        try {
            InputStream is = client001.getInputStream();
            // 试试用reader：
            BufferedReader br = new BufferedReader(new InputStreamReader(is, "utf8"));
            String receiveMsg;
            while ((receiveMsg = br.readLine()) != null) {
                System.out.println(receiveMsg);
            }
            is.close();
            br.close();
            garbageCollection();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void getFile(String filename){
        acceptClient();
        try {
            InputStream is = client001.getInputStream();
            FileOutputStream op = new FileOutputStream(new File("F:/"+filename));
            byte[] car = new byte[1024];
            int len;
            while((len=is.read(car))!=-1) {
                op.write(car, 0, len);
            }
//		 * 4.释放资源
            is.close();
            op.close();
            garbageCollection();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void uploadFile(File file){
        acceptClient();
        try {
            OutputStream os = client001.getOutputStream();
            FileInputStream fi = new FileInputStream(file);
            byte[] car=new byte[1024];
            int len;
            while((len=fi.read(car))!=-1) {
                os.write(car, 0, len);
            }
//		 * 4.释放资源
            os.close();
            fi.close();
            garbageCollection();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }



    private void garbageCollection(){
        try {
            client001.close();
            lisener.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        System.gc();
    }
    public void recieveFile() {

    }
}
