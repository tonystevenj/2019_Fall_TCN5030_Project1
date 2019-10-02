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
    }

    public DataChannel(int port) throws IOException {
        this.lisener = new ServerSocket(port);
    }

    public void acceptClient() {
        try {
            client001 = lisener.accept();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void dataRead() {
        acceptClient();
        try {
            InputStream is = client001.getInputStream();
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

}
