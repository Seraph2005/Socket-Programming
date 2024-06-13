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
                    out.writeUTF("Download");
                    out.flush();
                    File[] files = new File("data").listFiles();
                    if (files == null) {
                        System.out.println("no files found");
                        return;
                    }
                    try {
                        out.writeInt(files.length);
                    } catch (IOException e) {
                        closeAll();
                    }
                    int i = 1;
                    for (File file : files) {
                        try {
                            out.writeUTF(i + "- " + file.getName());
                            out.flush();
                            i++;
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }

                    int index;
                    try {
                        index = in.readInt();
                        System.out.println("File no." + index + " downloading.");

                        int bytes;
                        File file = files[index - 1];
                        FileInputStream fileInputStream = new FileInputStream(file);

                        out.writeUTF(file.getName());
                        out.writeInt((int) file.length());
                        out.flush();

                        byte[] buffer = new byte[4 * 1024];
                        while ((bytes = fileInputStream.read(buffer)) != -1) {
                            out.write(buffer, 0, bytes);
                            out.flush();
                        }
                        fileInputStream.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                        closeAll();
                        break;
                    }
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

    public void closeAll() {
        try {
            if (in != null) {
                in.close();
            }
            if (out != null) {
                out.close();
            }
            if (user.getSocket() != null) {
                user.getSocket().close();
            }
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }


}
