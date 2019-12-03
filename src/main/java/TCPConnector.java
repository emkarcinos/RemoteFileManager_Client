import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

public class TCPConnector {
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

    byte[] getMessage() {
        sendMessage(Protocol.C_ACK);
        try {
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            String messageFromServer = null;
            messageFromServer = in.readLine();
            return messageFromServer.getBytes();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public int sendMessage(String message) {
        try{
            OutputStream out = socket.getOutputStream();
            PrintWriter writer = new PrintWriter(out);
            writer.print(message);
            writer.flush();
        } catch (IOException e){
            e.printStackTrace();
        }
        return 0;
    }
}

