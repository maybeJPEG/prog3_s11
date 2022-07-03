package udp;

import java.io.IOException;
import java.net.*;

public class MulticastServer {
    public static void main(String[] args) {
        try(DatagramSocket datagramSocket=new DatagramSocket(6000)) {
            InetAddress group=InetAddress.getByName("234.0.0.1");
            byte cnt=0;
            while (true){
                cnt++;
                DatagramPacket packet = new DatagramPacket(new byte[]{cnt},1,group,6001);
                datagramSocket.send(packet);
                try{Thread.sleep(500);}catch(InterruptedException e){e.printStackTrace();}
            }
        } catch (SocketException e) {
            e.printStackTrace();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
