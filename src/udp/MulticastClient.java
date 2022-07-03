package udp;

import java.io.IOException;
import java.net.*;

public class MulticastClient {
    public static void main(String[] args) {
        try(MulticastSocket multicastSocket=new MulticastSocket(6001)) {
            multicastSocket.setSoTimeout(2000);
            InetAddress ia=InetAddress.getByName("234.0.0.1");
            multicastSocket.joinGroup(ia);
            byte[] buffer=new byte[1];
            for (int i = 0; i < 100; i++) {
                DatagramPacket packet = new DatagramPacket(buffer,buffer.length);
                multicastSocket.receive(packet);
                System.out.println("received: "+packet.getData()[0]);
            }
            multicastSocket.leaveGroup(ia);
        } catch (SocketException e) {
            e.printStackTrace();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
