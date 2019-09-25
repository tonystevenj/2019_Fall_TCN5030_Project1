package hongjing.wang;

import java.io.*;
import java.net.InetAddress;
import java.net.Socket;

/**
 * @auther Steven J
 * @createDate 2019-09-24  11:01
 */
public class MyFTP {


    public void run() throws Exception {
        /**获得键盘输入 String keyboardInput*/
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        System.out.print("MyFTP> ");
        String userin = br.readLine();
        /**分隔看看指令*/
        String[] userins = userin.split(" ");
        if(!userins[0].equalsIgnoreCase("open")){
            System.out.println("指令不对");
            return;
        }
        InetAddress inetAdde;
        try{
//            InetAddress inetAdde= InetAddress.getByName("speedtest.tele2.net");
            inetAdde= InetAddress.getByName(userins[1]);
        }catch (Exception e){
            System.out.println("请输入有效服务器地址");
            return;
        }

//        String keyboardInput = userin;
//        /**判断是输入的ip地址还是URL地址
//         * //        InetAddress a = InetAddress.getByName("speedtest.tele2.net");
//         * //        System.out.println(a);
//         * 得到InetAddress inetAdde
//         * */


        /**
         * 创建Socket,建立连接
         * */
        Socket client01=null;
        try {
//            System.out.println("断点0.1");
//            client01 = new Socket(inetAdde,21);
            client01 = new Socket(inetAdde,21);
        } catch (IOException e) {
            e.printStackTrace();
        }
//        System.out.println("断点0.2");
//        DataOutputStream os = new DataOutputStream(client01.getOutputStream());
        OutputStream os = client01.getOutputStream();
        String msg = "OPTS UTF8 ON\r\n";
//        System.out.println("断点0.3");
        os.write(msg.getBytes());
        os.flush();

        InputStream is1 = client01.getInputStream();
        BufferedReader is = new BufferedReader(new InputStreamReader(is1,"utf8"));
//        System.out.println("断点2");
//        byte[] car = new byte[5];
        //用ArrayOutputStream来存可变字符串
//        ByteArrayOutputStream nbnbnb =new ByteArrayOutputStream();
//        int len;
//        System.out.println("断点2.1");
        String feedBack;
//        while((len=is.read(car))!=-1) {
        while((feedBack=is.readLine())!=null) {
//            System.out.println("断点2.2");
//            String str=new String(car,0,len,"UTF8");
//            System.out.println(str);
//            System.out.println("断点2.3");
            System.out.println(feedBack);
        }
//        System.out.println("断点3");
//        String nbnbnbMsg=nbnbnb.toString("UTF8");
//        System.out.println(nbnbnbMsg);
//        System.out.println("断点4");
//		 * 3.释放资源
        os.close();
        is.close();
        client01.close();
    }
    public static void main(String[] args) throws Exception{
        MyFTP ftp1 = new MyFTP();
        ftp1.run();
    }
}
