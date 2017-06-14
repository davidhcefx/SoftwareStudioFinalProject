package server;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

/**
 * Created by davidhcefx on 2017/6/1.
 */
public class Server {
    private ServerSocket serverSocket;
    private ArrayList<ClientThread> connections;
    private int port;

    public Server(int port) {
        // initialize
        try {
            // ServerSocket isn't bound to particular IP, so will be bound to default loopback address: 127.0.0.1
            String ExternalIPAddr = "127.0.0.1";
            serverSocket = new ServerSocket(port, 0, InetAddress.getByName(ExternalIPAddr));
        }catch (IOException e){
            e.printStackTrace();
        }
        this.port = port;
        this.connections = new ArrayList<>();
    }

    public void start(){
        System.out.println("Server starts listening on port: " + this.port);
        // Use a loop to accept clients
        try {
            while (true){
                Socket s = serverSocket.accept();  // blocking
                System.out.println("New connection established.");
                ClientThread clientThread = new ClientThread(this, s);
                connections.add(clientThread);
                clientThread.start();  // start client thread
            }
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    public void broadcast(String message){
        System.out.println("Broadcasting message: "+message);
        for (ClientThread c : connections){
            c.sendMessage(message);
        }
    }

    public void removeConnection(ClientThread client){
        connections.remove(client);
    }

    public static void main(String[] args){
        Server server = new Server(8000);
        server.start();
    }
}
