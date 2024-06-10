package Server;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;

public class ClientHandler implements Runnable {
    private User user;
    private DataInputStream in;
    private DataOutputStream out;


    public ClientHandler(User user) throws IOException {
        this.user = user;
        this.in = new DataInputStream(user.getSocket().getInputStream());
        this.out = new DataOutputStream(user.getSocket().getOutputStream());
    }

    @Override
    public void run() {
        try {
            String request;
            while (true) {
                request = this.in.readUTF();
                if (request.equals("1")) {
                    System.out.println(user.getUsername()+" joined.");
                    Server.AddUser(user);
                    for(String message: Server.getMessages()) {
                        out.writeUTF(message);
                        out.flush();
                    }
                    String message = "";
                    while(true) {
                        message = this.in.readUTF();
                        sendToAll(user.getUsername() + ": " + message);
                        if (message.equals("quit")){
                            break;
                        }
                    }
                    sendToAll(user.getUsername() + " left the chat.");
                }
                else if(request.equals("2")) {
                    
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                in.close();
                out.close();
                user.getSocket().close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void sendToAll(String msg) throws IOException {
        Server.getMessages().add(msg);
        System.out.println(msg);
        for (User user : Server.getUsers()) {
            DataOutputStream out = new DataOutputStream(user.getSocket().getOutputStream());
            out.writeUTF(msg);
            out.flush();
        }
    }
}
