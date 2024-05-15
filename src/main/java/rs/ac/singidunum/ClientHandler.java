package rs.ac.singidunum;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.SocketException;

public class ClientHandler implements Runnable {

    private String name;
    private boolean online;
    private DataInputStream dis;
    private DataOutputStream dos;

    public ClientHandler(Socket socket, String name) throws IOException {
        this.name = name;
        this.online = true;
        dos.writeUTF("Welcome, " + name + "!");
        try {
            this.dis = new DataInputStream(socket.getInputStream());
            this.dos = new DataOutputStream(socket.getOutputStream());
        } catch (IOException e) {
            online = false;
            System.out.println("Client " + name + " disconnected during the setup process.");
        }
    }

    @Override
    public void run() {
        String received;
        while (online) {
            try {
                received = dis.readUTF();
                if (received.equals("/logout")) {
                    disconnect();
                    break;
                }
                // Send message to everyone
                send(name + ": " + received);

            } catch (SocketException se) {
                System.out.println(se.getClass().getName() + ": " + se.getMessage());
                disconnect();
                break;
            } catch (IOException ex) {
                System.out.println("Communication error: " + ex.getMessage());
            }
        }
    }

    private void disconnect() {
        Main.clients.remove(this);
        try {
            dos.close();
            dis.close();
        } catch (IOException ex) {
            System.out.println("Error while closing connection with client " + name);
        }
        online = false;
    }

    private void send(String message) {
        System.out.println(message);
        for (ClientHandler ch : Main.clients) {
            try {
                ch.dos.writeUTF(message);
            } catch (IOException ex) {
                ch.disconnect();
            }
        }
    }
}
