package library.tcp;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;

public class LibraryClient {
    public static void main(String[] args) {
        try (Socket socket = new Socket("localhost", 9000);
             DataInputStream in=new DataInputStream(socket.getInputStream());
             DataOutputStream out=new DataOutputStream(socket.getOutputStream())) {
            out.writeChar('I');
            out.writeInt(7);
            out.writeInt(1);
            char r=in.readChar();
            while ('L'==r){
                System.out.println(in.readUTF());
                out.writeChar('S');
                r=in.readChar();
            }
            switch (r){
                case 'E':
                    System.out.println("<end>");
                    break;
                case 'F':
                    System.out.println("error: "+in.readUTF());
                    break;
                default:
                    System.out.println("unknown response");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
