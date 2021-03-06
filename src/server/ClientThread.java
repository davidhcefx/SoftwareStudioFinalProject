package server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

/**
 * Created by davidhcefx on 2017/6/1.
 */
public class ClientThread extends Thread {
    private Server parent;
    private Socket socket;
    private BufferedReader reader;
    private PrintWriter writer;

    public ClientThread(Server server, Socket socket){
        // initialize
        this.parent = server;
        this.socket = socket;
        try {
            reader = new BufferedReader( new InputStreamReader(socket.getInputStream()));
            writer = new PrintWriter(socket.getOutputStream());
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    @Override
    public void run(){
        try {
            while (true){
                // My own method for checking if Socket is closed
                if (writer.checkError()){
                    System.out.println("Closing this socket...");
                    parent.removeConnection(this);
                    break;
                }
                String line = reader.readLine();  // blocking: wait for client's message
                switch (SocketComm.toSocketComm(line.charAt(0)-'0')){
                    case seedReq:
                        sendMessage(""+SocketComm.seedReply.getVal()+" "+parent.seed);
                        break;
                    case ready:
                        parent.readyCount++;
                        System.out.println("One client is ready.");
                        if (parent.readyCount == 3){
                            parent.broadcast(""+SocketComm.start.getVal());
                        }
                        break;
                    default:
                        // non-private message = public message
                        parent.broadcast(line);
                        break;
                }
            }
        }catch (IOException e){
            e.printStackTrace();
        }
    }
    /*
    * Note:
    *   My own method, in run() function, was inspired from Stackoverflow:
    *   "the only way to check if the socket was closed is by writing to it (return -1 if closed)"
    * */

    // send message to client
    public void sendMessage(String message){
        writer.println(message);
        writer.flush();
    }
}
