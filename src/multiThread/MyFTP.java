package multiThread;

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
public class MyFTP {
    private Socket client01;
    private InetAddress inetAdde;
    private BufferedReader br;
    private OutputStream os;
    private InputStream is;
    private ReceiveFromServer rf;
    private SendToServer st;


    public void start() {
        this.br = new BufferedReader(new InputStreamReader(System.in));
        logIn();
        this.rf = new ReceiveFromServer(is);
        this.st = new SendToServer(br, os);
        /**从服务器读取数据*/
        new Thread(rf).start();
        /**给服务器发送数据*/
        new Thread(st).start();

    }


    private void logIn() {
        while (true) {
            System.out.print("MyFTP> ");
            String userin = null;
            try {
                userin = br.readLine();
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
                this.inetAdde = InetAddress.getByName(userins[1]);
            } catch (Exception e) {
                System.out.println("请输入有效服务器地址");
                continue;
            }
            break;
        }

        /**创建Socket,建立连接* */
        try {
            this.client01 = new Socket(this.inetAdde, 21);
            System.out.println("链接成功");
        } catch (IOException e) {
            e.printStackTrace();
        }

        /*初始化io*/
        try {
            this.os = client01.getOutputStream();
            this.is = client01.getInputStream();
        } catch (IOException e) {
            e.printStackTrace();
        }
        //指引登陆：
        /*System.out.println("Input your user name:");
        StringBuilder userin= new StringBuilder("USER ");
        try {
            userin.append(br.readLine());
        } catch (IOException e) {
            System.out.println("从用户输入读取数据失败");
            e.printStackTrace();
        }
        st.send(userin.toString());*/

    }


    public static void main(String[] args) throws Exception {
        MyFTP ftp1 = new MyFTP();
        ftp1.start();
    }
}

class ReceiveFromServer implements Runnable {
    private InputStream is1;

    public ReceiveFromServer(InputStream is1) {
        this.is1 = is1;
    }

    @Override
    public void run() {
        /**从服务器读取数据
         * open speedtest.tele2.net
         * */
        try {
            BufferedReader is = new BufferedReader(new InputStreamReader(this.is1, "utf8"));
            String feedBack;
            while ((feedBack = is.readLine()) != null) {
                    System.out.println(feedBack);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

class SendToServer implements Runnable {
    private OutputStream os;
    private BufferedReader br;

    public SendToServer(BufferedReader br, OutputStream os) {
        this.os = os;
        this.br = br;
    }

    public void send(String msg) {
        msg = msg + "\r\n";
        try {
            byte[] sends= msg.getBytes();
            os.write(sends);
        } catch (IOException e) {
            System.out.println("os.write失败");
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        /**一旦建立链接就先发一个设定字符集的数据*/
        String msg = "OPTS UTF8 ON";
        send(msg);
        while (true) {
            /**获得键盘输入 msg*/
            // open speedtest.tele2.net
            try {
//                Thread.yield();
                Thread.sleep(800);
                System.out.print("MyFTP> ");
                msg = br.readLine();

            } catch (Exception e) {
                e.printStackTrace();
            }
            if(msg.startsWith("port")){
                try {
                    DataChannel dc = new DataChannel();
                    new Thread(dc).start();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            Thread.yield();

            /*将键盘输入转化为指令*/
            switch (msg) {

            }
            /**发送数据*/
            send(msg);
        }
    }
}
