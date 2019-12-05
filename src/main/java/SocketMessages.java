import java.io.*;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.charset.StandardCharsets;

class SocketMessages {
    private Socket socket;
    DataInputStream in;
    DataOutputStream out;
    long lastFileSize = 0;
    String lastStr = "";

    SocketMessages(Socket socket) {
        this.socket = socket;
        try {
            this.in = new DataInputStream(new BufferedInputStream(this.socket.getInputStream()));
            this.out = new DataOutputStream(new BufferedOutputStream(this.socket.getOutputStream()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private int byteToInt(byte[] bytes) {
        return bytes[0] << 24 | (bytes[1] & 0xFF) << 16 | (bytes[2] & 0xFF) << 8 | (bytes[3] & 0xFF);
    }

    private long byteToLong(byte[] bytes) {
        return bytes[0] << 56 | (bytes[1] & 0xFF) << 48 | (bytes[2] & 0xFF) << 40 | (bytes[3] & 0xFF) << 32 | (bytes[4] & 0xFF) << 24 | (bytes[5] & 0xFF) << 16 | (bytes[6] & 0xFF) << 8 | (bytes[7] & 0xFF);
    }

    public char getMessageType() throws IOException {
        byte[] firstByte = new byte[1];
        in.read(firstByte, 0, 1);
        return (char) firstByte[0];
    }

    public int getMessageLen() throws IOException {
        byte[] pLen = new byte[4];
        in.read(pLen, 0, 4);
        return (byteToInt(pLen));
    }

    private long getLong() throws IOException {
        byte[] pLen = new byte[8];
        int len = getMessageLen();
        in.read(pLen, 0, len);
        return (byteToLong(pLen));
    }

    byte[] getFileBytes(int length) throws IOException {
        byte[] messageByte = new byte[length];
        in.read(messageByte, 0, length);
        return messageByte;
    }

    int recieveMessage() throws IOException {
        char type = getMessageType();
        if (type == Protocol.T_STR) {
            lastStr = readString();
            System.out.println(lastStr);
        } else if (type == Protocol.T_DIR) {
            do {
                System.out.println(readString());
            } while (getMessageType() == Protocol.T_DIR);
        } else if (type == Protocol.DONEFOR) {
            return -1;
        } else if (type == Protocol.USRIN) {
            return 1;
        } else if (type == Protocol.T_LL) {
            lastFileSize = getLong();
            System.out.println("File size: " + lastFileSize);
        } else if (type == Protocol.T_FILE) {
            FileTransfer fileTransfer = new FileTransfer(lastStr, lastFileSize);
            fileTransfer.startDownload(this);
        }
        return 0;
    }

    private String readString() throws IOException {
        int length = getMessageLen();
        StringBuilder dataString;
        byte[] messageByte = new byte[length];
        boolean end = false;
        dataString = new StringBuilder(length);
        int totalBytesRead = 0;
        while (!end) {
            int currentBytesRead = in.read(messageByte);
            totalBytesRead = currentBytesRead + totalBytesRead;
            if (totalBytesRead <= length) {
                dataString
                        .append(new String(messageByte, 0, currentBytesRead, StandardCharsets.UTF_8));
            } else {
                dataString
                        .append(new String(messageByte, 0, length - totalBytesRead + currentBytesRead,
                                StandardCharsets.UTF_8));
            }
            if (dataString.length() >= length) {
                end = true;
            }
        }
        return dataString.toString();
    }

    int sendInt(int num) {
        try {
            ByteBuffer buff = ByteBuffer.allocate(4);
            byte[] b = buff.order(ByteOrder.LITTLE_ENDIAN).putInt(num).array();
            out.write(b);
            out.flush();
        } catch (IOException e) {
            e.printStackTrace();
            return -1;
        }
        return 0;
    }
}
