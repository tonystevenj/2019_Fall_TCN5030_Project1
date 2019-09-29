package singlethreadFTP;

import java.io.*;
import java.net.InetAddress;
import java.net.Socket;

/**
 * @auther Steven J
 * @createDate 2019-09-24  11:01
 * open speedtest.tele2.net
 * open inet.cis.fiu.edu
 * user demo
 * pass demopass
 * port 10,0,0,133,34,184
 * port 10,108,5,181,34,184
 */
public class MyFTP_Single {
    private Socket client01;
    private InetAddress inetAddress;
    private OutputStream os;
    private InputStream is;
    private BufferedReader brFromServer;
    private BufferedReader brFromKeyboard;
    private String feedBack = "";
    private InetAddress localAddress;


    public void start() {
        this.brFromKeyboard = new BufferedReader(new InputStreamReader(System.in));
//        getServerInfo();
        TestConnect();
        readFromServer();
        /**一旦建立链接就先发一个设定字符集的数据*/
        send("OPTS UTF8 ON");
        readFromServer();
        while (true) {
            keyboardToServer();
            readFromServer();
        }

    }

    /**
     * 要求用户输入要链接的地址，这个方法就是用来获得inetAddress的
     * 并没有实例化对象，实例化对象在initilize()里面
     */
    private void getServerInfo() {
        while (true) {
            System.out.print("MyFTP> ");
            String userin = null;
            try {
                userin = brFromKeyboard.readLine();
            } catch (IOException e) {
                System.out.println("从用户输入读取数据失败");
                e.printStackTrace();
            }
            /**分隔键盘输入，查看指令 String[] userins*/
            String[] userins = userin.split(" ");
            if (!userins[0].equalsIgnoreCase("open")) {
                System.out.println("指令不对");
                continue;
            }
            try {
                this.inetAddress = InetAddress.getByName(userins[1]);
            } catch (Exception e) {
                System.out.println("请输入有效服务器地址");
                continue;
            }
            initialize();
            break;
        }
    }

    /**
     * 实例化要用到的对象类，链接服务器之后调用
     */
    private void initialize() {
        try {
            this.client01 = new Socket(this.inetAddress, 21);

            System.out.println("Successful connected to server");
            this.os = client01.getOutputStream();
            this.is = client01.getInputStream();
            this.brFromServer = new BufferedReader(new InputStreamReader(this.is, "utf8"));
            this.localAddress = client01.getInetAddress();
            System.out.println("哈哈" + localAddress);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void TestConnect() {
        try {
            this.inetAddress = InetAddress.getByName("inet.cis.fiu.edu");
            this.client01 = new Socket(this.inetAddress, 21);
            System.out.println("Successful connected to server");
            this.os = client01.getOutputStream();
            this.is = client01.getInputStream();
            this.brFromServer = new BufferedReader(new InputStreamReader(this.is, "utf8"));
            this.localAddress = client01.getLocalAddress();
            System.out.println("哈哈" + localAddress);
            send("user demo");
            readFromServer();
            send("pass demopass");
            readFromServer();
            send(getPortInfo());
            readFromServer();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private void readFromServer() {
        /**从服务器读取数据
         * open speedtest.tele2.net
         * */
        try {
            feedBack = brFromServer.readLine();
            System.out.println(feedBack);

        } catch (IOException e) {
            e.printStackTrace();
        }

    }


    public void keyboardToServer() {
        String msg = "";
        /**获得键盘输入 msg*/
        try {
            System.out.print("MyFTP> ");
            msg = brFromKeyboard.readLine();
        } catch (Exception e) {
            e.printStackTrace();
        }

//        if(msg.toUpperCase().startsWith("PORT")){
//
//        }
        /**发送数据*/
        send(msg);
    }

    private void send(String msg) {
        msg = msg + "\r\n";
        try {
            byte[] sends = msg.getBytes("utf8");
            os.write(sends);
        } catch (IOException e) {
            System.out.println("os.write失败");
            e.printStackTrace();
        }
    }

    private String getPortInfo() {
        String[] msgs = this.localAddress.toString().split("\\.");
        StringBuilder msg = new StringBuilder("PORT "+msgs[0].substring(1,msgs[0].length()));
        msg.append(",").append(msgs[1])
                .append(",").append(msgs[2])
                .append(",").append(msgs[3])
                .append(",").append("34")
                .append(",").append("184");
        return msg.toString();
    }

    public static void main(String[] args) throws Exception {
        MyFTP_Single ftp1 = new MyFTP_Single();
        ftp1.start();
    }
}


