import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.*;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class HttpDemo {
    public static void main(String args[]) {
        try {
            java.net.URL url = new URL("https://www.htw-berlin.de:443/suche/?domain=www.htw-berlin.de&query=AI#nav");
            //url = new URL( "http://httpbin.org/#/Anything" );
            System.out.println(url.getProtocol());  // https
            System.out.println(url.getHost());      // www.htw-berlin.de
            System.out.println(url.getPort());      // 443
            System.out.println(url.getFile());      // /suche/?domain=www.htw-berlin.de&query=AI
            System.out.println(url.getPath());      // /suche/
            System.out.println(url.getQuery());     // domain=www.htw-berlin.de&query=AI
            System.out.println(url.getRef());       // nav

        } catch (java.net.MalformedURLException e) {
            e.printStackTrace();
        }

        System.out.println("---------------------------");
        try
        {
            URL url = new URL( "https://www.htw-berlin.de/suche/?domain=www.htw-berlin.de&query=AI#nav" );
            try(InputStream is=url.openStream();
                FileOutputStream fos=new FileOutputStream("result.html");){
                int b;
                while (-1!=(b=is.read())) fos.write(b);
            } catch (FileNotFoundException e) { e.printStackTrace();}
            catch (IOException e) { e.printStackTrace();}
        } catch (MalformedURLException e){ e.printStackTrace();}

        System.out.println("---------------------------");
        try {
            InetAddress address=InetAddress.getByName("google.de");
            System.out.println(address.getHostAddress());
            InetAddress ip=InetAddress.getByName(address.getHostAddress());
            System.out.println(ip.isReachable(1));
            System.out.println(ip.isReachable(1000));
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println("---------------------------");
        readConnection("https://www.htw-berlin.de:443/suche/?domain=www.htw-berlin.de&query=AI#nav");

        System.out.println("---------------------------");
        readConnection("https://www.htw-berlin.de/files/Presse/_tmp_/d/5/csm__DSC7464_Alexander_Rentsch_Banner_b5d13fe0d6.jpg");

        System.out.println("---------------------------");
        readConnection("ftp://ftp.rz.uni-wuerzburg.de/tex/latex2e/base.zip");

    }
    private static void readConnection(String urlString){
        URL url = null;
        try {
            url = new URL( urlString );
        } catch (MalformedURLException e){e.printStackTrace();}
        URLConnection connection = null;
        Object content=null;
        try {
            connection = url.openConnection();
            content=connection.getContent();
        } catch (IOException e) {e.printStackTrace();}
        System.out.println(url.getProtocol());
        System.out.println( connection.getClass().getName() );
        Map<String, List<String>> headerFields=connection.getHeaderFields();
        System.out.println( "Last Modified   : " + new Date(connection.getLastModified()) );
        System.out.println( "Content encoding: " + connection.getContentEncoding() );
        System.out.println( "Content type    : " + connection.getContentType() );
        System.out.println( "Content length  : " + connection.getContentLength() );
        System.out.println( content.getClass().getName() );
    }
}
