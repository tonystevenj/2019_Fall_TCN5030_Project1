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
 * 家里的网址： port 10,0,0,133,34,184
 * 学校网址： port 10,108,5,181,34,184
 * testUpload-HongjingWang.txt
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
    private int portDataPort = 8888;


    public void start() {
        this.brFromKeyboard = new BufferedReader(new InputStreamReader(System.in));
//        TestConnect();
        getServerInfo();
//        readFromServer();
        /**一旦建立链接就先发一个设定字符集的数据*/
        send("OPTS UTF8 ON");
        readFromServer();
        while (true) {
            keyboardToServer();
//            readFromServer();
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
            if (!userins[0].equalsIgnoreCase("myftp")) {
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
        System.out.println("Please type in user name: ");
        String msg = "";
        /**获得键盘输入 msg*/
        try {
            msg = brFromKeyboard.readLine();
        } catch (Exception e) {
            e.printStackTrace();
        }
        readFromServer();
        send("USER "+msg);
        readFromServer();
        if(feedBack.startsWith("331")){
            System.out.println("pass: ");
            try {
                msg = brFromKeyboard.readLine();
            } catch (Exception e) {
                e.printStackTrace();
            }
            send("PASS "+msg);
            readFromServer();
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
            this.localAddress = client01.getLocalAddress();
//            this.dc = new DataChannel(portDataPort);
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
//            this.dc = new DataChannel(portDataPort);
            send("user demo");
            readFromServer();
            send("pass demopass");
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
        String[] msgs = msg.split(" ");
        String command = msgs[0];
        if(command.equalsIgnoreCase("ls")){
            send(getPortInfo());
            readFromServer();
            send("nlst");
            /*try {//证明了服务器有等待机制，并不是发一次指令然后发现门关着就回去了，它会等一哈儿
                Thread.sleep(1500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }*/
            try {
                new DataChannel(portDataPort).dataRead();
            } catch (IOException e) {
                e.printStackTrace();
            }
            readFromServer();
            readFromServer();
            portDataPort+=1;
        }else if(command.equalsIgnoreCase("get")){
            send(getPortInfo());
            readFromServer();
            if(msgs.length==2){
                msg="RETR "+msgs[1];
                send(msg);
                try {
                    DataChannel dc = new DataChannel(portDataPort);
                    readFromServer();
                    if(feedBack.startsWith("150")){
                        dc.getFile(msgs[1]);
                    }else {
                        System.out.println("File do not exist");
//                        send("testMsg");
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }else {
                send("Unknown user command");
            }
            readFromServer();
            portDataPort+=1;
        }else if(command.equalsIgnoreCase("put")){
            send(getPortInfo());
            readFromServer();
            if(msgs.length==2){
               File toUpload = new File(msgs[1]);
               String filename = toUpload.getName();
               msg="STOR "+filename;
               send(msg);
               try {
                   new DataChannel(portDataPort).uploadFile(toUpload);
               } catch (IOException e) {
                   e.printStackTrace();
               }
               readFromServer();
            }else {
                send("Unknown user command");
            }
            readFromServer();
            portDataPort+=1;
        }else if(command.equalsIgnoreCase("cd")){
            if(msgs.length==2){
                msg="CWD "+msgs[1];
                send(msg);
            }else {
                send("Unknown user command");
            }
            readFromServer();
        }else if(command.equalsIgnoreCase("delete")){
            if(msgs.length==2){
                msg="DELE "+msgs[1];
                send(msg);
            }else {
                send("Unknown user command");
            }
            readFromServer();
        }else if(command.equalsIgnoreCase("quit")){
            send(msg);
            readFromServer();
        }else if(command.equalsIgnoreCase("pwd")){
            send("XPWD");
            readFromServer();
        }else if(command.equalsIgnoreCase("user")){

            send(msg);
            readFromServer();
        }else if(command.equalsIgnoreCase("pass")){
            send(msg);
            readFromServer();
        }else {
            send("Unknown user command");
            readFromServer();
        }
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
        int num1 = portDataPort/256;
        int num2 = portDataPort%256;
        String[] msgs = this.localAddress.toString().split("\\.");
        StringBuilder msg = new StringBuilder("PORT "+msgs[0].substring(1,msgs[0].length()));
        msg.append(",").append(msgs[1])
                .append(",").append(msgs[2])
                .append(",").append(msgs[3])
                .append(",").append(num1)
                .append(",").append(num2);
        System.out.println("GET port 信息："+msg);
        return msg.toString();
    }
    public static void main(String[] args) throws Exception {
        MyFTP_Single ftp1 = new MyFTP_Single();
        ftp1.start();
    }
}


