import java.io.IOException;
import java.util.Scanner;

public class App {
    public static Scanner userInput = new Scanner(System.in);

    public static void main(String[] args) {
        System.out.print("Server address: ");
        String ip = userInput.nextLine();
        System.out.print("Server port: ");
        int port = userInput.nextInt();

        TCPConnector socket = new TCPConnector(ip, port);
        socket.connect();

        SocketMessages reader = new SocketMessages(socket.getSocket());
        try {
            boolean running = true;
            while (running) {
                int msg = reader.recieveMessage();
                if (msg == -1)
                    running = false;
                else if (msg == 1) {
                    System.out.print("Choice: ");
                    int n = userInput.nextInt();
                    reader.sendInt(n);
                }

            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
