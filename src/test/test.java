package test;
import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * @auther Steven J
 * @createDate 2019-09-28  18:16
 */
public class test {
    public static void main(String[] args) throws UnknownHostException {
        String a;
//        if(a==null){
//        }

        String b = "asd";
        b.toUpperCase();
        System.out.println(b);

        System.out.println("***************");
        InetAddress aa = InetAddress.getByName("localhost");
        for(byte aaa : aa.getAddress()){
            System.out.println(aaa);
        }

        System.out.println("*******2222222222********");
        byte[] addr="10.2.4.5".getBytes();
        for(byte aaa : addr){
            System.out.println(aaa);
        }
        InetAddress inetAddress= InetAddress.getByAddress(addr);
        for(byte aaa : inetAddress.getAddress()){
            System.out.println(aaa);
        }


    }
}
