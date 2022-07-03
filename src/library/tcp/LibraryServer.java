package library.tcp;

import library.Library;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class LibraryServer implements Runnable {
    private Socket socket;
    private Library library;
    public LibraryServer(Socket socket,Library library){
        this.socket=socket;
        this.library=library;
    }
    @Override public void run() {
        try (DataOutputStream out=new DataOutputStream(socket.getOutputStream())) {
            try (DataInputStream in = new DataInputStream(socket.getInputStream())) {
                System.out.println("client@"+socket.getInetAddress()+":"+socket.getPort()+" connected");
                String error = this.executeSession(in, out);
                if(null!=error){
                    out.writeChar('F');
                    out.writeUTF(error);
                    System.out.println("client@"+socket.getInetAddress()+":"+socket.getPort()+" error="+error);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("client@"+socket.getInetAddress()+":"+socket.getPort()+" disconnected");
    }
    private String executeSession(DataInputStream in,DataOutputStream out) throws IOException {
        char command=in.readChar();
        if('I'!=command) return "unknown command: "+command;
        int bookId=in.readInt();
        //if(!library.bookExists(bookId)) return "unknown book: "+bookId;
        int lineNumber=in.readInt();
        if(!library.bookExists(bookId)) return "unknown book: "+bookId;
        do{
            if(!library.lineExists(bookId,lineNumber)) {
                out.writeChar('E');
                return null;
            }
            out.writeChar('L');
            out.writeUTF(this.library.getLine(bookId,lineNumber++));
            char response=in.readChar();
            switch (response){
                case 'N':
                    break;
                case 'S':
                    return null;
                default:
                    return "unknown response: "+response;
            }
        }while (true);
    }
    public static void main(String[] args) {
        try(ServerSocket serverSocket=new ServerSocket(9000);) {
            while(true){
                Socket socket=serverSocket.accept();
                LibraryServer s=new LibraryServer(socket,new Library());
                System.out.println("new client@"+socket.getInetAddress()+":"+socket.getPort());
                new Thread(s).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
