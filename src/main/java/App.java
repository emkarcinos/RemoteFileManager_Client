import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.Scanner;

public class App {
    public static Scanner userInput = new Scanner(System.in);

    public static void main(String[] args) throws InterruptedException {
        System.out.print("Server address: ");
        String ip = userInput.nextLine();
        System.out.print("Server port: ");
        int port = userInput.nextInt();
        TCPConnector socket = new TCPConnector(ip, port);
        socket.connect();
        System.out.println(new String(socket.getMessage()));
        System.out.println(new String(socket.getMessage()));
        String out = new String(socket.getMessage());
        while (!out.equals(Protocol.DIR_DONE)) {
            out = new String(socket.getMessage());
            System.out.println(out);
        }
        System.out.print("File no: ");
        int choice = userInput.nextInt();
        socket.sendMessage(Protocol.C_FILE);
        Thread.sleep(100);
        socket.sendMessage(Integer.toString(choice));
        ByteBuffer buffer = ByteBuffer.wrap(socket.getMessage());
        System.out.println("File size: " + buffer.getLong());
    }
}
