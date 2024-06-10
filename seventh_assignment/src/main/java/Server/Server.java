package Server;

import java.io.DataInputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Server {
    private static final int PORT = 3456;
    private static ArrayList<User> users = new ArrayList<>();
    private static ExecutorService pool = Executors.newFixedThreadPool(4);
    private static ArrayList<String> messages = new ArrayList<>();

    public static ArrayList<String> getMessages() {
        return messages;
    }

    public static ArrayList<User> getUsers() {
        return users;
    }

    public static void AddUser(User user){
        users.add(user);
    }

    public static void main(String[] args) {
        ServerSocket listener = null;
        Socket user;

        try {
            listener = new ServerSocket(PORT);
            System.out.println("Waiting for connection...");

            while (true) {
                user = listener.accept();
                System.out.println(user.getInetAddress() + "connected to server.");
                DataInputStream in = new DataInputStream(user.getInputStream());
                String username = in.readUTF();

                User user1 = new User(user, username);

                ClientHandler clientTask = new ClientHandler(user1);
                Thread clientThread = new Thread(clientTask);
                pool.execute(clientThread);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (listener != null) {
                try {
                    listener.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            pool.shutdown();
        }
    }
}