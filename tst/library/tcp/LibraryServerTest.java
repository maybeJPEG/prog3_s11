package library.tcp;

import library.Library;
import org.junit.jupiter.api.Test;
import org.mockito.InOrder;

import java.io.*;
import java.net.Socket;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class LibraryServerTest {
    private final Library library=new Library();
    @Test public void secondLineOfFirstBook() throws IOException {
        Socket mockSocket=mock(Socket.class);
        LibraryServer server=new LibraryServer(mockSocket,library);
        ByteArrayOutputStream bos=new ByteArrayOutputStream(14);
        DataOutputStream dos=new DataOutputStream(bos);
        dos.writeChar('I');
        dos.writeInt(1);
        dos.writeInt(2);
        dos.writeChar('S');
        when(mockSocket.getInputStream()).thenReturn(new ByteArrayInputStream(bos.toByteArray()));
        ByteArrayOutputStream out=new ByteArrayOutputStream(1024);
        when(mockSocket.getOutputStream()).thenReturn(out);

        server.run();

        DataInputStream dis=new DataInputStream(new ByteArrayInputStream(out.toByteArray()));
        assertEquals('L',dis.readChar());
        assertEquals(library.getLine(1,2),dis.readUTF());
    }
    @Test public void secondLineOfFirstBookClosed() throws IOException {
        Socket mockSocket=mock(Socket.class);
        LibraryServer server=new LibraryServer(mockSocket,library);
        ByteArrayOutputStream bos=new ByteArrayOutputStream(140);
        try(DataOutputStream dos=new DataOutputStream(bos)) {
            dos.writeChar('I');
            dos.writeInt(1);
            dos.writeInt(2);
            dos.writeChar('S');
        }
        when(mockSocket.getInputStream()).thenReturn(new ByteArrayInputStream(bos.toByteArray()));
        ByteArrayOutputStream out=new ByteArrayOutputStream(1024);
        when(mockSocket.getOutputStream()).thenReturn(out);

        server.run();

        DataInputStream dis=new DataInputStream(new ByteArrayInputStream(out.toByteArray()));
        assertEquals('L',dis.readChar());
        assertEquals(library.getLine(1,2),dis.readUTF());
    }
    @Test public void ninthLineOfFirstBook() throws IOException {
        Socket mockSocket=mock(Socket.class);
        LibraryServer server=new LibraryServer(mockSocket,library);
        InputStream mockInputStream=mock(InputStream.class);
        OutputStream mockOutputStream=mock(OutputStream.class);
        when(mockSocket.getInputStream()).thenReturn(mockInputStream);
        when(mockSocket.getOutputStream()).thenReturn(mockOutputStream);
        when(mockInputStream.read()).thenReturn(0).thenReturn(73)
                .thenReturn(0).thenReturn(0).thenReturn(0).thenReturn(2)
                .thenReturn(0).thenReturn(0).thenReturn(0).thenReturn(9);

        server.run();

        InOrder inOrder=inOrder(mockOutputStream);
        inOrder.verify(mockOutputStream).write((byte)0);
        inOrder.verify(mockOutputStream).write((byte)69);
    }
}