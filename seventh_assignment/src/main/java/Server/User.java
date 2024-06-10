package Server;

import java.net.Socket;


public class User {
    private Socket socket;
    String username;

    public User(Socket socket, String username) {
        this.socket = socket;
        this.username = username;
    }

    public Socket getSocket() {
        return socket;
    }

    public String getUsername() {
        return username;
    }

}
