package singlethreadFTP;

import java.io.*;
import java.net.InetAddress;
import java.net.Socket;

/**
 * @auther Steven J
 * @createDate 2019-09-24  11:01
 * open speedtest.tele2.net
 */
public class MyFTP_singleThread {
    private Socket client01;
    private InetAddress inetAdde;
    private BufferedReader br;
    public MyFTP_singleThread() {
        this.br = new BufferedReader(new InputStreamReader(System.in));
        while(true){
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
            System.out.println("链接成功");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void start(){
        SendToServer st = new SendToServer(client01,inetAdde,br);
        ReceiveFromServer rf = new ReceiveFromServer(client01);
        while (true){
            /**从服务器读取数据*/
            rf.run();
            /**给服务器发送数据*/
            st.run();

        }

    }


    public static void main(String[] args) throws Exception {
        MyFTP_singleThread ftp1 = new MyFTP_singleThread();
        ftp1.start();
    }
}

class SendToServer implements Runnable{
    private Socket client01;
    private InetAddress inetAdde;
    private OutputStream os;
    private BufferedReader br;
    private String msg;
    public SendToServer(Socket client01,InetAddress inetAdde,BufferedReader br) {
        this.client01=client01;
        this.inetAdde=inetAdde;
        this.br=br;
        try {
            os = client01.getOutputStream();
        } catch (IOException e) {
            System.out.println("创建outputstream失败");
            e.printStackTrace();
        }
        /**一旦建立链接就先发一个设定字符集的数据*/
        msg = "OPTS UTF8 ON\r\n";
        try {
            os.write(msg.getBytes());
            os.flush();
        } catch (IOException e) {
            System.out.println("os.write失败");
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
            System.out.print("MyFTP> ");
            /**获得键盘输入 msg*/
            try {
                msg = br.readLine()+"\r\n";
            } catch (IOException e) {
                e.printStackTrace();
            }
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
            String tail="";
            while (!tail.equals("\r\n")) {
                String feedBack = is.readLine();
                tail = feedBack.substring(feedBack.length()-4);
                System.out.println("这是tail信息："+tail);
                System.out.println(feedBack);
            }

           /* //换个stream
            DataInputStream di = new DataInputStream(is1);
            while(true){
                System.out.println("a");
                String aa = di.readUTF();
                System.out.println(aa);
            }*/

            /*//换个stream - inputstream
            byte[] car = new byte[1024];
            int len;
            String tail="";
            while (tail!="\r\n"){
                len= is1.read(car);
                String feedBack=new String(car,0,len,"UTF8");
                tail = feedBack.substring(feedBack.length()-2);
                System.out.println("这是tail信息："+tail);
                System.out.println(feedBack);
            }*/
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
