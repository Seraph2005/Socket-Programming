package Client;

import java.util.Scanner;
import java.io.*;
import java.net.Socket;

import static java.lang.Thread.sleep;

// Client Class
public class Client {
    private static final String SERVER_IP = "localhost";
    private static final int SERVER_PORT = 3456;

    static Scanner input = new Scanner(System.in);
    static Scanner input1 = new Scanner(System.in);

    public static void main(String[] args) throws IOException, InterruptedException {
        Socket user = new Socket(SERVER_IP, SERVER_PORT);
        DataOutputStream out = new DataOutputStream(user.getOutputStream());
        DataInputStream reader = new DataInputStream(System.in);

        ServerResponse responseTask = new ServerResponse(user);
        Thread responseThread = new Thread(responseTask);
        responseThread.start();

        System.out.print("You're connected to server. Enter your username: ");
        String username = input.nextLine();

        out.writeUTF(username);
        out.flush();

        while(true){
            System.out.println("Enter 1 to join the messenger and 2 to download the files.");
            String task = input1.nextLine();
            out.writeUTF(task);
            out.flush();
            if(task.equals("2"))
            {
                int fChoice = input.nextInt();
                out.writeInt(fChoice);
                out.flush();
                sleep(100);
            }
        }

    }
}