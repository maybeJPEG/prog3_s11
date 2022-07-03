package library.udp;

import java.io.*;
import java.net.*;

public class LibraryUdpClient {
    private DatagramSocket socket;
    private InetAddress serverAddress;
    private int serverPort;
    private int messageId=0;
    public LibraryUdpClient(DatagramSocket datagramSocket, InetAddress serverAddress, int serverPort){
        this.socket=datagramSocket;
        this.serverAddress=serverAddress;
        this.serverPort=serverPort;
    }
    public int init(int bookId,int line) throws IOException {
        ByteArrayOutputStream bos=new ByteArrayOutputStream(14);
        DataOutputStream dos=new DataOutputStream(bos);
        dos.writeChar('I');
        dos.writeInt(++messageId);
        dos.writeInt(bookId);
        dos.writeInt(line);
        dos.close();
        bos.close();
        this.sendMessage(bos.toByteArray());
        return messageId;
    }
    public int send(char command,int sessionId) throws IOException {
        ByteArrayOutputStream bos=new ByteArrayOutputStream(10);
        DataOutputStream dos=new DataOutputStream(bos);
        dos.writeChar(command);
        dos.writeInt(++messageId);
        dos.writeInt(sessionId);
        dos.close();
        bos.close();
        this.sendMessage(bos.toByteArray());
        return messageId;
    }
    private void sendMessage(byte[] message){
        DatagramPacket packetOut = new DatagramPacket(message,message.length,this.serverAddress,this.serverPort);
        try {
            this.socket.send(packetOut);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public DataInputStream receive() throws IOException {
        byte[] buffer=new byte[1024];
        DatagramPacket packetIn = new DatagramPacket(buffer,buffer.length);
        this.socket.receive(packetIn);
        DataInputStream dis=new DataInputStream(new ByteArrayInputStream(packetIn.getData()));
        return dis;
    }
    public static void main(String[] args) {
        try (DatagramSocket datagramSocket = new DatagramSocket(9010)) {
            LibraryUdpClient client=new LibraryUdpClient(datagramSocket, InetAddress.getByName("localhost"),9009);
            int messageId=client.init(9,2);
            DataInputStream dis=client.receive();
            char command=dis.readChar();
            int returnMessageId=dis.readInt();
            if(returnMessageId!=messageId) System.out.println("wrong message id returned: "+returnMessageId);
            int sessionId=dis.readInt();
            switch (command){
                case 'L':
                    System.out.println(dis.readUTF());
                    break;
                case 'E':
                    System.out.println("<end>");
                    break;
                default:
                    System.out.println(command+" - "+dis.readUTF());
            }
            client.send('S',sessionId);
        } catch (SocketException e) {
            e.printStackTrace();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
