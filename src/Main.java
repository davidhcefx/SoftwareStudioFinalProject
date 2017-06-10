import de.looksgood.ani.Ani;
import processing.core.PApplet;
import processing.core.PImage;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Random;
import processing.core.PVector;
import java.util.Scanner;

public class Main extends PApplet
{
    Random random = new Random();
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

    @Override
    public void settings() {
        super.settings();
        size(830, 700);
    }

    @Override
    public void setup() {
        Ani.init(this);
        mazemap = new Mazemap(this);

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

        // Socket
//            try {
//                socket = new Socket("127.0.0.1", 8000);
//                socketReader = new Scanner(socket.getInputStream());
//                socketWriter = new PrintWriter(socket.getOutputStream());
//            } catch (IOException e) {
//                System.out.println("Error while establishing socket connection!");
//                System.out.println("Please check if the Server was running, and check if internet connection is valid.");
//                System.out.println("Closing the program...");
//                e.printStackTrace();
//                exit();
//            }
//            // use thread to read message
//            Thread serverThread = new Thread(new Runnable() {
//                @Override
//                public void run() {
//                    // TODO: can I use scanner?? yes you can
//                    // TODO: what about startup synchronize? random Item?
//
//                    // then use socket to control players
//                    while (!socket.isClosed()) {
//                        String command = socketReader.next();
//                        // keyPressed()
//                        if (command.equals("setVelocity")) {
//                            int id = socketReader.nextInt();
//                            int i = socketReader.nextInt();
//                            int j = socketReader.nextInt();
//                            System.out.println("Received: "+command+id+","+i+","+j);
//                            players.get(id).setVelocity(i, j);
//                        } // mousePressed()
//                        else if (command.equals("mousePressed")) {
//                            int x = socketReader.nextInt();
//                            int y = socketReader.nextInt();
//                            System.out.println("Received: "+command+x+","+y);
//                            System.out.println(mazemap.checkBound(x, y));
//                        }
//                    }
//                }
//            });
//            serverThread.start();

        //StartMenu
        state = 0;
        button1 = new Button(this);
        buttonState = new PImage[2];
        buttonState[0] = loadImage("res/start_button1.png");
        buttonState[1] = loadImage("res/start_button2.png");
        startTitle = loadImage("res/startmenu_title.png");
        startBackground = loadImage("res/menu_background_small.png");
    }

    @Override
    public void draw() {
        background(52);

        if (state == 0){
            // Start Scene
            image(startBackground,0,0,830, 700);
            image(startTitle, (float)(830/9),(float)(300/3),(float)(920/1.4),(float)(80/1.4));
            state = button1.display();
        }
        else if (state == 1) {
            // Game Scene
            mazemap.display();
            image(scoreboard, 700, 0, 130, 700);
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
                        if (items.get(j).getToolid() == 1) players.get((i + n) % 3).addItem(items.get(j));
                        else if (items.get(j).getToolid() == 2) {
                            int Id1 = players.get((i + 1) % 3).getId();
                            int Id2 = players.get((i + 2) % 3).getId();
                            players.get((i + 1) % 3).setId(Id2);
                            players.get((i + 2) % 3).setId(Id1);
                            players.get(i).addItem(items.get(j));
                        } else players.get(i).addItem(items.get(j));
                    }
                }
            }
        }
        else if (state == 2){
            // End scene
        }
    }

    @Override
    public void keyPressed(){
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

    @Override
    public void keyReleased() {
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

    @Override
    public void mousePressed() {
        System.out.println("mouse pressed: "+mouseX+","+mouseY);
        System.out.println(mazemap.checkBound(mouseX, mouseY));
    }

    /*
    * 1. setVelocity [player] [i] [j]
    * */
//    @Override
//    public void keyPressed(){
//        int speed = 8;
//        if (key == CODED) {
//            switch (keyCode) {
//                case UP:
//                    socketWriter.printf("setVelocity %d %d %d\n", 2, 0, -speed);
//                    break;
//                case DOWN:
//                    socketWriter.printf("setVelocity %d %d %d\n", 2, 0, speed);
//                    break;
//                case LEFT:
//                    socketWriter.printf("setVelocity %d %d %d\n", 2, -speed, 0);
//                    break;
//                case RIGHT:
//                    socketWriter.printf("setVelocity %d %d %d\n", 2, speed, 0);
//                    break;
//            }
//        } else {
//            switch (key) {
//                case 'w':
//                    socketWriter.printf("setVelocity %d %d %d\n", 0, 0, -speed);
//                    break;
//                case 's':
//                    socketWriter.printf("setVelocity %d %d %d\n", 0, 0, speed);
//                    break;
//                case 'a':
//                    socketWriter.printf("setVelocity %d %d %d\n", 0, -speed, 0);
//                    break;
//                case 'd':
//                    socketWriter.printf("setVelocity %d %d %d\n", 0, speed, 0);
//                    break;
//                // second player
//                case 'i':
//                    socketWriter.printf("setVelocity %d %d %d\n", 1, 0, -speed);
//                    break;
//                case 'j':
//                    socketWriter.printf("setVelocity %d %d %d\n", 1, -speed, 0);
//                    break;
//                case 'k':
//                    socketWriter.printf("setVelocity %d %d %d\n", 1, 0, speed);
//                    break;
//                case 'l':
//                    socketWriter.printf("setVelocity %d %d %d\n", 1, speed, 0);
//                    break;
//            }
//        }
//        socketWriter.flush();
//    }
//
//    @Override
//    public void keyReleased() {
//        // send command to Server
//        if (key == CODED){
//            socketWriter.printf("setVelocity %d %d %d\n", 2, 0, 0);
//        } else {
//            switch (key) {
//                case 'w':
//                case 'a':
//                case 's':
//                case 'd':
//                    socketWriter.printf("setVelocity %d %d %d\n", 0, 0, 0);
//                    break;
//                case 'i':
//                case 'j':
//                case 'k':
//                case 'l':
//                    socketWriter.printf("setVelocity %d %d %d\n", 1, 0, 0);
//                    break;
//            }
//        }
//        socketWriter.flush();
//    }

    public void switchRelation(){
//        Player tmp = players.get(0);
//        players.set(0, players.get(2));
//        players.set(2, tmp);
        // status bar ??
    }


    public static void main(String[] args) {
        PApplet.main("Main", args);
    }
}

