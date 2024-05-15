package rs.ac.singidunum;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.UUID;
import java.util.Vector;

public class Main {

    public static Vector<ClientHandler> clients = new Vector<>();


    public static void main(String[] args) {

        System.out.println("Server started.");
        try {
            ServerSocket ss = new ServerSocket(6000);
            Socket s;
            while (true) {
                System.out.println("Waiting for clients to connect");
                s = ss.accept();
                System.out.println("New client: " + s);
                String name = UUID.randomUUID().toString();
                ClientHandler handler = new ClientHandler(s, name);
                Thread t = new Thread(handler);
                clients.add(handler);
                t.start();
                System.out.println("Client " + name + " connected.");
            }
        } catch (IOException ex) {
            System.out.println("An error occured.");
        }
    }
}