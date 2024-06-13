package Client;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class ServerResponse implements Runnable {
    DataInputStream respond;

    public ServerResponse(Socket user) throws IOException {
        respond = new DataInputStream(user.getInputStream());
    }

    @Override
    public void run() {
        try {
            while(true) {
                String r = respond.readUTF();
                if(r.equals("Download"))
                {
                    try {
                        int count = respond.readInt();
                        for (int i = 0; i < count; i++) {
                            System.out.println(respond.readUTF());
                        }

                        String fileName = respond.readUTF();

                        int bytes;
                        FileOutputStream fileOutputStream = new FileOutputStream(fileName);

                        int size = respond.readInt();
                        byte[] buffer = new byte[4 * 1024];
                        while (size > 0 && (bytes = respond.read(buffer, 0, Math.min(buffer.length, size))) != -1) {
                            fileOutputStream.write(buffer, 0, bytes);
                            size -= bytes;
                        }

                        fileOutputStream.close();
                    } catch (IOException e) {
                        closeAll();
                    }
                }
                else
                    System.out.println(r);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void closeAll() {
        try {
            if (respond != null) {
                respond.close();
            }
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

}
