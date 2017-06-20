import ddf.minim.AudioPlayer;
import ddf.minim.Minim;
import de.looksgood.ani.Ani;
import processing.core.PApplet;
import processing.core.PFont;
import processing.core.PImage;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import processing.core.PVector;

import java.util.Random;
import java.util.Scanner;

import server.*;

public class Main extends PApplet
{
    Random random;
    boolean randomSetted = false;
    // Mazemap
    public int curMaze = 1;
    Mazemap mazemap;
    // Players
    ArrayList<Player> players = new ArrayList<>();
    int num;
    public ArrayList<PImage> playersImg =  new ArrayList<>();
    // Items
    float positionconst = 700/21;
    public ArrayList<Item> items = new ArrayList<>();
    public ArrayList<PImage> itemsImg =  new ArrayList<>();
    PVector[] itemposition;  // pre-stored item initialization spots
    // Scoreboard
    PImage scoreboard;
    PImage relationship;
    // Socket
    private Socket socket;
    private Scanner socketReader;
    private PrintWriter socketWriter;
    // Start Button
    Button button1;
    PImage startBackground;
    PImage startTitle;
    PImage buttonState[];
    int state;
    
//    Minim minim;
//    AudioPlayer player;

    @Override
    public void settings() {
        super.settings();
        size(830, 700);
    }

    @Override
    public void setup() {
        frameRate(55);
        Ani.init(this);
        mazemap = new Mazemap(this);

//        // Socket
//        try {
//            String IPAddr = "127.0.0.1";
//            socket = new Socket(IPAddr, 8000);
//            socketReader = new Scanner(socket.getInputStream());
//            socketWriter = new PrintWriter(socket.getOutputStream());
//        } catch (IOException e) {
//            System.out.println("Error while establishing socket connection!");
//            System.out.println("Please check if the Server was running, and check if internet connection is valid.");
//            System.out.println("Closing the program...");
//            e.printStackTrace();
//            exit();
//        }
//        // use thread to read message
//        Thread serverThread = new Thread(new Runnable() {
//            @Override
//            public void run() {
//                // then use socket to control players
//                while (! socket.isClosed()) {
////                    String line = socketReader.nextLine();
////                    System.out.println("line: "+line);
//                    int id, dir;
//                    int tmpCmd = socketReader.nextInt();
//                    SocketComm cmd = SocketComm.toSocketComm(tmpCmd);
//                    switch (cmd){
//                        case keyPressed:
//                            id = socketReader.nextInt();
//                            dir = socketReader.nextInt();
//                            players.get(id).keyPressed(moveKey.toMoveKey(dir));
//                            break;
//                        case keyReleased:
//                            id = socketReader.nextInt();
//                            dir = socketReader.nextInt();
//                            players.get(id).keyReleased(moveKey.toMoveKey(dir));
//                            break;
//                        case seedReply:
//                            random = new Random(socketReader.nextLong());
//                            randomSetted = true;
//                            break;
//                        case start:
//                            state = 2;
//                            break;
//                        case error:
//                            System.out.println("error occurred in Parsing Socket Message!");
//                            System.out.println("tmpCmd: "+tmpCmd);
//                            break;
//                    }
//                }
//            }
//        });
//        serverThread.start();
//        sendtoSocket(""+SocketComm.seedReq.getVal());
//        while (! randomSetted){
//            try {
//                Thread.sleep(10);
//            }catch (InterruptedException e){
//                e.printStackTrace();
//            }
//        }
        random = new Random(); // should be removed

        // players
        System.out.println("Generating players...");
        for(int i = 1; i <= 4; i++) playersImg.add(loadImage("res/paper" + i + ".png"));
        for(int i = 1; i <= 4; i++) playersImg.add(loadImage("res/rock" + i + ".png"));
        for(int i = 1; i <= 4; i++) playersImg.add(loadImage("res/scissors" + i + ".png"));
        int Pradius = 11;
        Player p0 = new Player(this, 0, playersImg, 50, 50, Pradius);
        Player p1 = new Player(this, 1, playersImg, 650, 50, Pradius);
        Player p2 = new Player(this, 2, playersImg, 650, 650, Pradius);
        players.add(p0);
        players.add(p1);
        players.add(p2);

        // Items
        System.out.println("Generating items...");
        itemposition = new PVector[]{
                new PVector(1,18), new PVector(2,19), new PVector(17,8), new PVector(19,3), new PVector(19,12),
                new PVector(3,1), new PVector(15,16), new PVector(9,7), new PVector(17,1),new PVector(3,14),
                new PVector(1,16), new PVector(5,1), new PVector(3,9), new PVector(7,15), new PVector(18,17),
                new PVector(11,1), new PVector(7,3), new PVector(11,9), new PVector(8,17), new PVector(17,15),
                new PVector(6,5), new PVector(11,5), new PVector(14,5), new PVector(12,13), new PVector(3,4),
                new PVector(1,9), new PVector(13,18), new PVector(5,15), new PVector(5,8), new PVector(19,10),
                new PVector(7,6), new PVector(2,7), new PVector(9,1), new PVector(19,6), new PVector(15,7),
                new PVector(1,10), new PVector(16,19), new PVector(9,2), new PVector(11,11), new PVector(15,13),
                new PVector(1,3), new PVector(9,14), new PVector(4,11), new PVector(13,3), new PVector(10,19),
                new PVector(1,12), new PVector(6,9), new PVector(4,13), new PVector(17,5), new PVector(15,11)
        };
        itemsImg.add(loadImage("res/Mushroom.png"));
        itemsImg.add(loadImage("res/pill.png"));
        itemsImg.add(loadImage("res/spiral.png"));
        itemsImg.add(loadImage("res/wing.png"));
        itemsImg.add(loadImage("res/question mark.png"));
        int iRandom = random.nextInt(5);
        int Iradius = 15;
        for (int i = 0; i < 10; i++) {
            int x = (int) (itemposition[i+iRandom*10].x * positionconst + Iradius);
            int y = (int) (itemposition[i+iRandom*10].y * positionconst + Iradius);
            int mRandom = random.nextInt(4);
            Item t = new Item(this, itemsImg.get(4), itemsImg.get(mRandom), mRandom,x, y, Iradius);
            items.add(t);
        }

        //Scoreboard
        scoreboard = loadImage("res/original/wood.png");
        relationship = loadImage("res/icon.png");
        //StartMenu
        state = 0;
        button1 = new Button(this);
        buttonState = new PImage[2];
        buttonState[0] = loadImage("res/start_button1.png");
        buttonState[1] = loadImage("res/start_button2.png");
        startTitle = loadImage("res/startmenu_title.png");
        startBackground = loadImage("res/menu_background_small.png");
//        minim = new Minim(this);
//        player = minim.loadFile("res/Puzzle-Game_Looping.mp3");
//        player.play();
    }
 
    boolean readySent = false;
    int winner = -1;
    boolean control = true;
    @Override
    public void draw() {
        background(52);
        switch (state) {
            case 0:
                // Start Scene
                image(startBackground,0,0,830, 700);
                image(startTitle, (float)(830/9),(float)(300/3),(float)(920/1.4),(float)(80/1.4));
                state = button1.display();

                break;
            case 1:
                // Blocking, wait until all clients are synchronized
                // imgae() connection.. please wait ?
                if (! readySent){
                    sendtoSocket(""+SocketComm.ready.getVal());
                    readySent = true;
                }
                state = 2; // should be removed

                break;
            case 2: {
                // Game Scene
                mazemap.display();
                image(scoreboard, 700, 0, 130, 700);
                image(relationship, 320, 320, 60,60);
                for (Player c : players) {
                    //physics calculation
                    c.update();
                    //draw player
                    c.display();
                }
                for (Item t : items) {
                    t.display();
                }
                // check if Player collide with Item
                for (int i = 0; i < players.size(); i++) {
                    for (int j = 0; j < items.size(); j++) {
                        if (players.get(i).checkCollisionItem(items.get(j))) {
                            int n = random.nextInt(2) + 1;
                            if (items.get(j).getToolid() == 1) {
                                // pill: send it to others randomly
                                players.get((i + n) % 3).addItem(items.get(j));
                            } else if (items.get(j).getToolid() == 2) {
                                // spiral: change ID
                                int Id1 = players.get((i + 1) % 3).getId();
                                int Id2 = players.get((i + 2) % 3).getId();
                                players.get((i + 1) % 3).setId(Id2);
                                players.get((i + 2) % 3).setId(Id1);
                                players.get(i).addItem(items.get(j));
                            } else {
                                players.get(i).addItem(items.get(j));
                            }
                        }
                    }
                }
                // 0 > 1 > 2
                for(int i = 0; i < 3; i++) {
                    int j = (i+1)%3;
                    if (players.get(i).checkCollisionPlayer(players.get(j))) {
                        if ((players.get(i).getId()+1)%3 == (players.get(j).getId())) {
                            // i win
                            players.get(j).shrink();
                            winner = i;
                            System.out.println("Player "+players.get(i).getId()+" wins!");

                        }else {
                            // i loose
                            players.get(i).shrink();
                            winner = j;
                            System.out.println("Player "+players.get(j).getId()+" wins!");
                        }
                        Thread changeState = new Thread(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    Thread.sleep(5000);
                                } catch (InterruptedException e){
                                    e.printStackTrace();
                                }
                                control = false;
                            }
                        });
                        changeState.start();
                    }
                }
                if (! control){
                    // End scene
                    fill(0);
                    textFont(createFont("Times New Roman Bold", 56));
                    text("Winner", 200, 400);
                    image(playersImg.get(winner*4), 400, 350, 60, 60);
                }
                
                break;
            }
        }
    }

    @Override
    public void keyPressed(){
        if (control) {
            if (key == CODED){
                switch (keyCode){
                    // Player 2
                    case UP:
                        players.get(2).keyPressed(moveKey.UP);
                        break;
                    case DOWN:
                        players.get(2).keyPressed(moveKey.DOWN);
                        break;
                    case LEFT:
                        players.get(2).keyPressed(moveKey.LEFT);
                        break;
                    case RIGHT:
                        players.get(2).keyPressed(moveKey.RIGHT);
                        break;
                }
            }else {
                switch (key){
                    // Player 0
                    case 'w':
                        players.get(0).keyPressed(moveKey.UP);
                        break;
                    case 's':
                        players.get(0).keyPressed(moveKey.DOWN);
                        break;
                    case 'a':
                        players.get(0).keyPressed(moveKey.LEFT);
                        break;
                    case 'd':
                        players.get(0).keyPressed(moveKey.RIGHT);
                        break;
                    // Player 1
                    case 'i':
                        players.get(1).keyPressed(moveKey.UP);
                        break;
                    case 'k':
                        players.get(1).keyPressed(moveKey.DOWN);
                        break;
                    case 'j':
                        players.get(1).keyPressed(moveKey.LEFT);
                        break;
                    case 'l':
                        players.get(1).keyPressed(moveKey.RIGHT);
                        break;
                }
            }
        }
    }

    @Override
    public void keyReleased() {
        if (control){
            if (key == CODED){
                switch (keyCode){
                    // Player 2
                    case UP:
                        players.get(2).keyReleased(moveKey.UP);
                        break;
                    case DOWN:
                        players.get(2).keyReleased(moveKey.DOWN);
                        break;
                    case LEFT:
                        players.get(2).keyReleased(moveKey.LEFT);
                        break;
                    case RIGHT:
                        players.get(2).keyReleased(moveKey.RIGHT);
                        break;
                }
            } else {
                switch (key) {
                    // Player 0
                    case 'w':
                        players.get(0).keyReleased(moveKey.UP);
                        break;
                    case 's':
                        players.get(0).keyReleased(moveKey.DOWN);
                        break;
                    case 'a':
                        players.get(0).keyReleased(moveKey.LEFT);
                        break;
                    case 'd':
                        players.get(0).keyReleased(moveKey.RIGHT);
                        break;
                    // Player 1
                    case 'i':
                        players.get(1).keyReleased(moveKey.UP);
                        break;
                    case 'k':
                        players.get(1).keyReleased(moveKey.DOWN);
                        break;
                    case 'j':
                        players.get(1).keyReleased(moveKey.LEFT);
                        break;
                    case 'l':
                        players.get(1).keyReleased(moveKey.RIGHT);
                        break;
                }
            }
        }
    }

    @Override
    public void mousePressed() {
        System.out.println("mouse pressed: "+mouseX+","+mouseY);
        System.out.println(mazemap.checkBound(mouseX, mouseY));
    }

    /* ============================================================================================= */
//    @Override
    public void keyPressed2(){
        StringBuffer msg = new StringBuffer(SocketComm.keyPressed.getVal()+" ");
        if (key == CODED){
            switch (keyCode){
                // Player 2
                case UP:
                    msg.append("2 ").append(moveKey.UP.getVal());
                    break;
                case DOWN:
                    msg.append("2 ").append(moveKey.DOWN.getVal());
                    break;
                case LEFT:
                    msg.append("2 ").append(moveKey.LEFT.getVal());
                    break;
                case RIGHT:
                    msg.append("2 ").append(moveKey.RIGHT.getVal());
                    break;
            }
        }else {
            switch (key){
                // Player 0
                case 'w':
                    msg.append("0 ").append(moveKey.UP.getVal());
                    break;
                case 's':
                    msg.append("0 ").append(moveKey.DOWN.getVal());
                    break;
                case 'a':
                    msg.append("0 ").append(moveKey.LEFT.getVal());
                    break;
                case 'd':
                    msg.append("0 ").append(moveKey.RIGHT.getVal());
                    break;
                // Player 1
                case 'i':
                    msg.append("1 ").append(moveKey.UP.getVal());
                    break;
                case 'k':
                    msg.append("1 ").append(moveKey.DOWN.getVal());
                    break;
                case 'j':
                    msg.append("1 ").append(moveKey.LEFT.getVal());
                    break;
                case 'l':
                    msg.append("1 ").append(moveKey.RIGHT.getVal());
                    break;
            }
        }
        sendtoSocket(msg.toString());
    }

//    @Override
    public void keyReleased2() {
        StringBuffer msg = new StringBuffer(SocketComm.keyReleased.getVal()+" ");
        if (key == CODED){
            switch (keyCode){
                // Player 2
                case UP:
                    msg.append("2 ").append(moveKey.UP.getVal());
                    break;
                case DOWN:
                    msg.append("2 ").append(moveKey.DOWN.getVal());
                    break;
                case LEFT:
                    msg.append("2 ").append(moveKey.LEFT.getVal());
                    break;
                case RIGHT:
                    msg.append("2 ").append(moveKey.RIGHT.getVal());
                    break;
            }
        } else {
            switch (key) {
                // Player 0
                case 'w':
                    msg.append("0 ").append(moveKey.UP.getVal());
                    break;
                case 's':
                    msg.append("0 ").append(moveKey.DOWN.getVal());
                    break;
                case 'a':
                    msg.append("0 ").append(moveKey.LEFT.getVal());
                    break;
                case 'd':
                    msg.append("0 ").append(moveKey.RIGHT.getVal());
                    break;
                // Player 1
                case 'i':
                    msg.append("1 ").append(moveKey.UP.getVal());
                    break;
                case 'k':
                    msg.append("1 ").append(moveKey.DOWN.getVal());
                    break;
                case 'j':
                    msg.append("1 ").append(moveKey.LEFT.getVal());
                    break;
                case 'l':
                    msg.append("1 ").append(moveKey.RIGHT.getVal());
                    break;
            }
        }
        sendtoSocket(msg.toString());
    }
    /* ===================================================================================================== */

    private void sendtoSocket(String msg){
//        socketWriter.println(msg);
//        socketWriter.flush();
    }

    public static void main(String[] args) {
        PApplet.main("Main", args);
    }
}
