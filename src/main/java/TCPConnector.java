import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

class TCPConnector {
    private Socket socket;
    InetAddress serverAddress;
    int port = 9999;

    TCPConnector(String serverAddress, int port) {
        try {
            this.serverAddress = InetAddress.getByName(serverAddress);
            this.port = port;
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
    }

    void connect() {
        try {
            socket = new Socket(serverAddress, port);
            System.out.println("Connection established.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    Socket getSocket() {
        return socket;
    }
}

