import java.io.*;
import java.net.*;

public class HttpGet {
    public static void main(String args[]){
        try {
            String suchText="   Änderungen Übersicht   ";
            String encText=URLEncoder.encode( suchText.trim(), "UTF-8" );
            System.out.println(encText);
            URL url = new URL( "https://www.htw-berlin.de:443/suche/?domain=www.htw-berlin.de&query=" + encText );
            try(
                    InputStream is=url.openStream();
                    //InputStream is=url.openConnection().getInputStream();
                    //InputStream is=(InputStream)url.openConnection().getContent();
                    FileOutputStream fos=new FileOutputStream("result.html");
            ) {
                int b;
                while (-1 != (b = is.read())) fos.write(b);
            }catch (FileNotFoundException e) {
                e.printStackTrace();
            }catch (IOException e) {
                e.printStackTrace();
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }  catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }
}
