package library.udp;

import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class LibraryUdpServerTest {
    @Test public void testWrongRequest() throws IOException {
        DatagramSocket mockSocket=mock(DatagramSocket.class);
        ArgumentCaptor<DatagramPacket> captor=ArgumentCaptor.forClass(DatagramPacket.class);
        LibraryUdpServer server=new LibraryUdpServer(mockSocket,null);
        InetAddress address=mock(InetAddress.class);
        int port=9000;
        byte[] data=new byte[]{00,65,00,00,00,01};
        doAnswer(new Answer<Void>() {
            public Void answer(InvocationOnMock invocation) {
                DatagramPacket d=invocation.getArgument(0);
                d.setAddress(address);
                d.setPort(port);
                d.setData(data);
                return null;
            }
        }).when(mockSocket).receive(any(DatagramPacket.class));

        server.processMessage();

        verify(mockSocket).send(captor.capture());
        assertEquals(address.getHostAddress(),captor.getValue().getAddress().getHostAddress());
        assertEquals(port,captor.getValue().getPort());
        try(DataInputStream dis=new DataInputStream(new ByteArrayInputStream(captor.getValue().getData()))) {
            assertEquals('F', dis.readChar());
            assertEquals(1, dis.readInt());
            assertEquals("unknown request: A", dis.readUTF());
        }
    }
}