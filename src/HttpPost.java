import java.io.*;
import java.net.*;

public class HttpPost {
    public static void main(String args[]){
        try {
            String body = "param1=" + URLEncoder.encode( "(Ü)", "UTF-8" )+"&"+"param2=" + URLEncoder.encode( "value2", "UTF-8" );
            URL url = new URL( "http://httpbin.org/post" );
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod( "POST" );
            connection.setDoInput( true );
            connection.setDoOutput( true );
            connection.setUseCaches( false );
            connection.setRequestProperty( "Content-Type","application/x-www-form-urlencoded" );
            String contentLength=String.valueOf(body.length());
            connection.setRequestProperty( "Content-Length",contentLength);
            OutputStreamWriter writer = new OutputStreamWriter( connection.getOutputStream() );
            writer.write( body );
            writer.flush();
            System.out.println( "send Content-Length=" + contentLength );
            System.out.println(connection.getResponseMessage());

            System.out.println( "Content type    : " + connection.getContentType() );
            System.out.println( "Content length  : " + connection.getContentLength() );
            try(InputStream is=connection.getInputStream(); FileOutputStream fos=new FileOutputStream("result.json");){
                int b;
                int c=0;
                while (-1!=(b=is.read())){
                    fos.write(b);
                    c++;
                }
                System.out.println( "read bytes  : " + c );
            }
        } catch (ProtocolException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
