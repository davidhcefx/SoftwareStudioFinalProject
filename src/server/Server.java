package server;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Created by davidhcefx on 2017/6/1.
 *
 * 連線問題之一：用鍵盤、滑鼠指令來同步操控，若是時間久了，失去同步的話怎麼辦？
 *              而且 loop 速度會依據 CPU 用量動態調整，非常可能會 off-synchronize !
 */
public class Server {
    private ServerSocket serverSocket;
    private ArrayList<ClientThread> connections;
    private int port;
    long seed;
    int readyCount = 0;

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
        // seed generation
        seed = seedUniquifier() ^ System.nanoTime();
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

    private static final AtomicLong seedUniquifier = new AtomicLong(8682522807148012L);
    private static long seedUniquifier() {
        // L'Ecuyer, "Tables of Linear Congruential Generators of
        // Different Sizes and Good Lattice Structure", 1999
        for (;;) {
            long current = seedUniquifier.get();
            long next = current * 181783497276652981L;
            if (seedUniquifier.compareAndSet(current, next))
                return next;
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