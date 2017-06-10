import de.looksgood.ani.Ani;
import processing.core.PApplet;
import processing.core.PImage;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Random;
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
    public ArrayList<Item> items = new ArrayList<>();
    public ArrayList<PImage> itemsImg =  new ArrayList<>();
    // Socket
    private Socket socket;
    private Scanner socketReader;
    private PrintWriter socketWriter;

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
        mazemap = new Mazemap(this);
        // players
        num = 3;
        for(int i = 1; i <= 4; i++) playersImg.add(loadImage("res/paper" + i + ".png"));
        for(int i = 1; i <= 4; i++) playersImg.add(loadImage("res/rock" + i + ".png"));
        for(int i = 1; i <= 4; i++) playersImg.add(loadImage("res/scissors" + i + ".png"));
        for (int i = 0; i < num; i++) {
            int x = (i % 5 * 120) + 60;
            int y = i / 5 * 200 + 100;
            println("gen players: " + x + "," + y);
            int radius = 18;
            Player c = new Player(this, i, playersImg.get(i*4), playersImg.get(i*4 + 1), playersImg.get(i*4 + 2), playersImg.get(i*4 + 3),x, y, radius);
            players.add(c);
        }
        // Items
        itemsImg.add(loadImage("res/Mushroom.png"));
        itemsImg.add(loadImage("res/pill.png"));
        itemsImg.add(loadImage("res/spiral.png"));
        itemsImg.add(loadImage("res/wing.png"));
        itemsImg.add(loadImage("res/question mark.png"));
        for (int i = 0; i < 5; i++) {
            int x = (i % 5 * 120) + 60;
            int y = i / 5 * 200 + 300;
            println("gen items: " + x + "," + y);
            int radius = 18;
            int mRandom = random.nextInt(4);
            Item t = new Item(this, itemsImg.get(4), itemsImg.get(mRandom), mRandom,x, y, radius);
            items.add(t);
        }
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
            image(startBackground,0,0,830, 700);
            image(startTitle, (float)(830/9),(float)(300/3),(float)(920/1.4),(float)(80/1.4));
            state = button1.display();
        }//if state 0

        else if (state == 1) {
            mazemap.display();
            for (Player c : players) {
                //physics calculation
                c.update();
                //draw player
                c.display();
            }
            for (Item t : items) {
                t.display();
            }
            int i, j;
            for (i = 0; i < players.size(); i++) {
                for (j = 0; j < items.size(); j++) {
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
        } //if state == 1
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
                case 'a':
                    players.get(0).keyReleased(moveKey.DOWN);
                    break;
                case 's':
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

