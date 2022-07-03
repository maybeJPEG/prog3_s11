package udp;

import java.io.IOException;
import java.net.*;

public class UdpServer {
    public static void main(String[] args) {
        try(DatagramSocket datagramSocket=new DatagramSocket(5000)) {
            byte[] buffer=new byte[1024];
            DatagramPacket packetIn = new DatagramPacket(buffer,buffer.length);
            datagramSocket.receive(packetIn);
            String received=new String(packetIn.getData(),0,packetIn.getLength());
            System.out.println("received: "+received+" from: "+packetIn.getAddress()+":"+packetIn.getPort());
            byte[] response=new StringBuilder(received).reverse().toString().getBytes();
            DatagramPacket packetOut = new DatagramPacket(response,response.length,packetIn.getAddress(),packetIn.getPort());
            datagramSocket.send(packetOut);
        } catch (SocketException e) {
            e.printStackTrace();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}