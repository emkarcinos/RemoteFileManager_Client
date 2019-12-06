import java.io.*;
import java.net.Socket;

public class FileTransfer {
    private FileOutputStream out;
    private long fileSize;

    public FileTransfer(String fileName, long fileSize) {
        try {
            String name = "D:/" + fileName;
            System.out.println(name);
            out=new FileOutputStream(name);
            this.fileSize = fileSize;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    void startDownload(SocketMessages socketMessages) throws IOException {
        System.out.println("Starting file transfer...");
        socketMessages.getMessageLen(); // Just to move the buffer to the beginning of the file
        socketMessages.getIn().read(new byte[10], 0, 1);
        long totalRead = 0;
        long remaining = fileSize;
        while(totalRead < fileSize) {
            byte[] byteArr = new byte[(int) Math.min(Protocol.BUF_SIZE, remaining)];
            int temp = socketMessages.getIn().read(byteArr, 0, (int) Math.min(Protocol.BUF_SIZE, remaining));
            if(temp <= 0)
                break;
            else
                totalRead+=temp;
            remaining -= temp;
            out.write(byteArr);
            printProgressBar(totalRead, fileSize);
        }
        System.out.println("\nDone!");
        out.close();
    }

    void printProgressBar(long current, long total){
        System.out.print("\r");
        System.out.print("|");
        float precentage = (float)current / total * 100;
        int blockCount = Math.round(precentage / 10);
        for(int i = 0 ; i<10; i++){
            if (i < blockCount)
                System.out.print("=");
            else
                System.out.print(" ");
        }
        System.out.print(" " + Math.round(precentage) + "% " + current + "B / " + total );
    }
}
