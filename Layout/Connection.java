import java.io.IOException;
import java.net.*;
import javax.net.ssl.*;
import java.nio.*;
import java.nio.channels.*;

public class Connection {

    static String Receiver_Name, Sender_Name;

    static ServerSocketChannel ssc;
    static SocketChannel sc;

    public static boolean Request_Accept() {
        try {
            SSLServerSocketFactory.getDefault().createServerSocket().getChannel();
            ssc = ServerSocketChannel.open();
            ssc.bind(new InetSocketAddress("localhost", 1234));

            System.out.println("Waiting for Connection...");
            sc = ssc.accept();
            sc.setOption(StandardSocketOptions.SO_KEEPALIVE, true);
            System.out.println(sc.isOpen());
            System.out.println(sc.isConnected());
            {
                InetSocketAddress isa = (InetSocketAddress) sc.getRemoteAddress();
                Sender_Name = isa.getHostName();
            }
            sc.write(ByteBuffer.wrap("Thank You for Connecting to Server.".getBytes()));
        } catch (Exception e) {
            System.out.println(e);
        }
        return sc.isConnected();
    }

    public static boolean Request_Send(String ip) {
        try {
            SSLSocketFactory.getDefault().createSocket().getChannel();
            sc = SocketChannel.open();
            sc.connect(new InetSocketAddress(ip, 1234));
            System.out.println(sc.isConnected());
            System.out.println(sc.isConnectionPending());
            System.out.println(sc.finishConnect());
            {
                InetSocketAddress isa = (InetSocketAddress) sc.getRemoteAddress();
                Receiver_Name = isa.getHostName();
            }
            ByteBuffer bb = ByteBuffer.allocate(100);
            sc.read(bb);
            bb.flip();
            System.out.println(new String(bb.array()).trim());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return sc.isConnected();
    }
}
