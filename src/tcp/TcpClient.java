package tcp;

import java.io.*;
import java.net.*;

public class TcpClient {
    public static void main(String[] args) {
        try (Socket socket = new Socket("localhost", 7000);
             DataInputStream in=new DataInputStream(socket.getInputStream());
             DataOutputStream out=new DataOutputStream(socket.getOutputStream());) {
            int cnt=0;
            while (cnt<10){
                System.out.println("send: " + cnt);
                out.writeInt(cnt++);
                System.out.println("received: " + in.readInt());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}