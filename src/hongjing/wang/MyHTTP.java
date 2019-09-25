package hongjing.wang;
import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
/**
 * @auther Steven J
 * @createDate 2019-09-24  17:12
 */
public class MyHTTP {
    public static void main(String[] args) throws Exception{

        MyHTTP http1 = new MyHTTP();
        http1.run();
    }

    public void run() throws Exception {
        /**获得键盘输入 String keyboardInput*/
        String keyboardInput;
        /**判断是输入的ip地址还是URL地址
         * //        InetAddress a = InetAddress.getByName("speedtest.tele2.net");
         * //        System.out.println(a);
         * 得到InetAddress inetAdde
         * */
//        InetAddress inetAdde= InetAddress.getByName("speedtest.tele2.net");
        InetAddress inetAdde= InetAddress.getByName("www.baidu.com");

        /**
         * 创建Socket,建立连接
         * */
        Socket client01=null;
        try {
            System.out.println("断点0.1");
//            client01 = new Socket(inetAdde,21);
            client01 = new Socket(inetAdde,80);
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("断点0.2");
        OutputStream os = client01.getOutputStream();
//        DataOutputStream os = new DataOutputStream(client01.getOutputStream());
        String msg ="GET / HTTP/1.1\r\n" +
                "Host: www.baidu.com\r\n\r\n";
        System.out.println("断点0.3");
//        os.writeUTF(msg);
        os.write(msg.getBytes());


        InputStream  is= client01.getInputStream();
//        BufferedReader is = new BufferedReader(new InputStreamReader(is1,"UTF-8"));
        System.out.println("断点2");
        byte[] car = new byte[5];
        //用ArrayOutputStream来存可变字符串
//        ByteArrayOutputStream nbnbnb =new ByteArrayOutputStream();
        int len;
        System.out.println("断点2.1");
        while((len=is.read(car))!=-1) {
//            System.out.println("断点2.2");
            String str=new String(car,0,len,"UTF8");
            System.out.println(str);
//            System.out.println("断点2.3");
        }
        System.out.println("断点3");
//        String nbnbnbMsg=nbnbnb.toString("UTF8");
//        System.out.println(nbnbnbMsg);
        System.out.println("断点4");
//		 * 3.释放资源
        os.close();
        is.close();
        client01.close();


    }
}
