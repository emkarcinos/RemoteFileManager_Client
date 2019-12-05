import java.io.*;
import java.net.Socket;

public class FileTransfer {
    private FileOutputStream out;
    private long fileSize;

    public FileTransfer(String fileName, long fileSize) {
        try {
            String name = "/home/marcin/" + fileName;
            System.out.println(name);
            out=new FileOutputStream(name);
            this.fileSize = fileSize;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    void startDownload(SocketMessages socketMessages) throws IOException {
        System.out.println("Starting file transfer...");
        long totalRead = 0;
        while(totalRead != fileSize){
            int packetLen=socketMessages.getMessageLen();
            out.write(socketMessages.getFileBytes(packetLen));
            totalRead += packetLen;
            float precentage = (totalRead / fileSize) * 100;
            printProgressBar(precentage, totalRead, fileSize);
            if(socketMessages.getMessageType()==Protocol.DONEFOR)
                break;
        }
        System.out.println("\nDone!");
        out.close();
    }

    void printProgressBar(float precentage, long current, long total){
        System.out.print("\r");
        System.out.print("|");
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
