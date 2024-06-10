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
                System.out.println(respond.readUTF());
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
