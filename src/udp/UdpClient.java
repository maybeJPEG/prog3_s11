package udp;

import java.io.IOException;
import java.net.*;

public class UdpClient {
    public static void main(String[] args) {
        try(DatagramSocket datagramSocket=new DatagramSocket(5001)) {
            InetAddress address = InetAddress.getByName("localhost");
            String message="hello";
            System.out.println("send : "+message);
            byte[] bytes=message.getBytes();
            DatagramPacket packetOut = new DatagramPacket(bytes,bytes.length,address,5000);
            datagramSocket.send(packetOut);
            byte[] buffer=new byte[bytes.length];
            DatagramPacket packetIn = new DatagramPacket(buffer,buffer.length);
            datagramSocket.receive(packetIn);
            String received=new String(packetIn.getData(),0,packetIn.getLength());
            System.out.println("received : "+received);
        } catch (SocketException e) {
            e.printStackTrace();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}