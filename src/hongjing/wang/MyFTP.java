package hongjing.wang;

import java.io.*;
import java.net.InetAddress;
import java.net.Socket;

/**
 * @auther Steven J
 * @createDate 2019-09-24  11:01
 * open speedtest.tele2.net
 */
public class MyFTP {
    private Socket client01;
    private InetAddress inetAdde;
    private BufferedReader br;

    public MyFTP() {
        this.br = new BufferedReader(new InputStreamReader(System.in));
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
                inetAdde = InetAddress.getByName(userins[1]);
            } catch (Exception e) {
                System.out.println("请输入有效服务器地址");
                continue;
            }
            break;
        }

        /**创建Socket,建立连接* */
        try {
            client01 = new Socket(inetAdde, 21);
//            System.out.println("链接成功");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void start() {
        ReceiveFromServer rf = new ReceiveFromServer(client01);
        SendToServer st = new SendToServer(client01, inetAdde, br);
        /**从服务器读取数据*/
        new Thread(rf).start();
        /**给服务器发送数据*/
        new Thread(st).start();

    }


    public static void main(String[] args) throws Exception {
        MyFTP ftp1 = new MyFTP();
        ftp1.start();
    }
}

class ReceiveFromServer implements Runnable {
    private Socket client01;
    InputStream is1;

    public ReceiveFromServer(Socket c) {
        this.client01 = c;
    }

    @Override
    public void run() {
        /**从服务器读取数据
         * open speedtest.tele2.net
         * */

        try {
            is1 = client01.getInputStream();
            BufferedReader is = new BufferedReader(new InputStreamReader(is1, "utf8"));
            String feedBack;

            while ((feedBack = is.readLine()) != null) {
                synchronized (client01) {
                    System.out.println(feedBack);
                }
            }


        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

class SendToServer implements Runnable {
    private Socket client01;
    private InetAddress inetAdde;
    private OutputStream os;
    private BufferedReader br;

    public SendToServer(Socket client01, InetAddress inetAdde, BufferedReader br) {
        this.client01 = client01;
        this.inetAdde = inetAdde;
        this.br = br;
        try {
            os = client01.getOutputStream();
        } catch (IOException e) {
            System.out.println("创建outputstream失败");
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        /**一旦建立链接就先发一个设定字符集的数据*/
        String msg = "OPTS UTF8 ON\r\n";
        try {

            os.write(msg.getBytes());
            os.flush();
        } catch (IOException e) {
            System.out.println("os.write失败");
            e.printStackTrace();
        }
        while (true) {

            /**获得键盘输入 msg*/
            // open speedtest.tele2.net
            try {
//                Thread.yield();
                Thread.sleep(800);
                synchronized (inetAdde) {
                    System.out.print("MyFTP> ");
                    msg = br.readLine() + "\r\n";
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
            Thread.yield();
            /**发送数据*/
            try {
                os.write(msg.getBytes());
                os.flush();
                System.out.println("数据发送成功");
            } catch (IOException e) {
                System.out.println("os.write失败");
                e.printStackTrace();
            }
        }
    }
}
