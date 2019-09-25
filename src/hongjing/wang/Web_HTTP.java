package hongjing.wang;

/**
 * @auther Steven J
 * @createDate 2019-09-24  22:07
 */
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;
import java.net.UnknownHostException;

public class Web_HTTP {
    private final String URL = "www.baidu.com";
    private final String CONTENT = "GET / HTTP/1.1\r\nHost: www.baidu.com\r\n\r\n" ;
    String msg ="GET / HTTP/1.1\r\n" +
                "Host: www.baidu.com\r\n\r\n";
//                "Connection: keep-alive\r\n" +
//                "Content-Length: 53\r\n" +
//                "User-Agent: Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/75.0.3770.142 Safari/537.36\r\n" +
//                "Accept: text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3\r\n" +
//                "Accept-Encoding: gzip, deflate, br\r\n" +
//                "Accept-Language: zh-CN,zh;q=0.9,en;q=0.8\r\n\r\n"
//                ;
    private final int PORT = 80 ;

    public void sendHTTP(){
        try {

            Socket socket = new Socket(URL,PORT); //建立TCP/IP链接
            OutputStream out = socket.getOutputStream() ;
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream(),"UTF-8"));
            out.write(CONTENT.getBytes());  //发送数据
            int d = -1 ;
            while((d=in.read())!=-1){       //接收
                System.out.print((char)d);  //输出到控制台
            }

        } catch (UnknownHostException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public static void main(String[] args){
        new Web_HTTP().sendHTTP();
    }
}

