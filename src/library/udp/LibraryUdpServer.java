package library.udp;

import library.Library;

import java.io.*;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.HashMap;

public class LibraryUdpServer {
    private class Session {
        public int id;
        public int bookId;
        public int line;
    }
    private DatagramSocket socket;
    private byte[] inBuffer=new byte[14];
    private HashMap<Integer, Session> sessions=new HashMap<>();
    private int sessionCounter=0;
    private Library library;
    public LibraryUdpServer(DatagramSocket socket,Library library){
        this.socket=socket;
        this.library=library;
    }

    public void processMessage() throws IOException {
        DatagramPacket packetIn = new DatagramPacket(this.inBuffer, this.inBuffer.length);
        this.socket.receive(packetIn);
        try(DataInputStream dis=new DataInputStream(new ByteArrayInputStream(packetIn.getData()))) {
            this.processMessage(dis,packetIn.getAddress(),packetIn.getPort());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private void processMessage(DataInputStream dis, InetAddress address, int port) throws IOException {
        char request=dis.readChar();
        int messageId=dis.readInt();
        switch (request){
            case 'I':
                Session session=new Session();
                session.id=++this.sessionCounter;
                session.bookId=dis.readInt();
                session.line=dis.readInt();
                if(!library.bookExists(session.bookId)){
                    this.sendResponse('F',messageId,session.id,"unknown book: "+session.bookId,address,port);
                    return;
                }
                this.sessions.put(session.id,session);
                System.out.println("open session "+session.id+" for "+address+":"+port);
                this.deliver(session,messageId,address,port);
                break;
            case 'N':
                this.deliver(this.sessions.get(dis.readInt()),messageId,address,port);
                break;
            case 'S':
                this.sessions.remove(dis.readInt());
                System.out.println("close session for "+address+":"+port);
                break;
            default:
                this.sendResponse('F',messageId,-1,"unknown request: "+request,address,port);
                break;
        }
    }
    private void deliver(Session session,int messageId,InetAddress address, int port){
        if(!library.lineExists(session.bookId,session.line))  this.sendResponse('E',messageId,session.id,null,address,port);
        else this.sendResponse('L',messageId,session.id,this.library.getLine(session.bookId,session.line++),address,port);
    }
    private void sendResponse(char response, int messageId, int sessionId, String payload, InetAddress address, int port) {
        try(ByteArrayOutputStream bos=new ByteArrayOutputStream(1024)) {//TODO from payload
            try (DataOutputStream dos = new DataOutputStream(bos)) {
                dos.writeChar(response);
                dos.writeInt(messageId);
                if(0<sessionId) dos.writeInt(sessionId);
                if(null!=payload) dos.writeUTF(payload);
            } catch (IOException e) {
                e.printStackTrace();
            }
            byte[] out=bos.toByteArray();
            DatagramPacket packetOut = new DatagramPacket(out,out.length,address,port);
            this.socket.send(packetOut);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static void main(String[] args) {
        try (DatagramSocket datagramSocket = new DatagramSocket(9009)) {
            LibraryUdpServer s=new LibraryUdpServer(datagramSocket,new Library());
            while (true) {
                try {
                    s.processMessage();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } catch (SocketException e) {
            e.printStackTrace();
        }
    }
}
