import de.looksgood.ani.Ani;
import processing.core.PApplet;
import processing.core.PImage;
import java.util.ArrayList;
import java.util.Random;

public class Main extends PApplet
{
    int num;
    Random random = new Random();
    public int curMaze = 1;
    Mazemap mazemap;
    ArrayList<Player> players = new ArrayList<>();
    public ArrayList<PImage> playersImg =  new ArrayList<>();
    public ArrayList<Item> items = new ArrayList<>();
    public ArrayList<PImage> itemsImg =  new ArrayList<>();
    

    @Override
    public void settings() {
        super.settings();
        size(700, 720);
    }

    @Override
    public void setup() {
        // initialization...
        mazemap = new Mazemap(this);
        // players
        num = 3;
        for(int i = 1; i <= 4; i++) playersImg.add(loadImage("bin/paper" + i + ".png"));
        for(int i = 1; i <= 4; i++) playersImg.add(loadImage("bin/rock" + i + ".png"));
        for(int i = 1; i <= 4; i++) playersImg.add(loadImage("bin/scissors" + i + ".png"));
        for (int i = 0; i < num; i++) {
            int x = (i % 5 * 120) + 60;
            int y = i / 5 * 200 + 100;
            println("" + x + "," + y);
            int radius = 40;
            Player c = new Player(this, i, playersImg.get(i*4), playersImg.get(i*4 + 1), playersImg.get(i*4 + 2), playersImg.get(i*4 + 3),x, y, radius);
            players.add(c);
         }
         // Items
         itemsImg.add(loadImage("bin/Mushroom.png"));
         itemsImg.add(loadImage("bin/pill.png"));
         itemsImg.add(loadImage("bin/spiral.png"));
         itemsImg.add(loadImage("bin/wing.png"));
         itemsImg.add(loadImage("bin/question mark.png"));
         for (int i = 0; i < 5; i++) {
            int x = (i % 5 * 120) + 60;
            int y = i / 5 * 200 + 300;
            println("" + x + "," + y);
            int radius = 30;
            int mRandom = random.nextInt(4);
            Item t = new Item(this, itemsImg.get(4), itemsImg.get(mRandom), mRandom,x, y, radius);
            items.add(t);
         }
    }

    @Override
    public void draw() {
        background(52);
        mazemap.display();
        for (Player c : players) {
            //physics calculation
            c.update();
            c.checkBoundaryCollision();
            //draw player
            c.display();
        }
        for (Item t : items) {
            t.display();
        }
        int i, j;
        for (i = 0; i < players.size(); i++) {
            for (j = 0; j < items.size(); j++) {
                if(players.get(i).checkCollisionItem(items.get(j))) {
                    int n = random.nextInt(2) + 1;
                    if(items.get(j).getToolid() == 1)
                        players.get((i + n)%3).addItem(items.get(j));
                    else players.get(i).addItem(items.get(j));
                }
             }
         }
    }

    @Override
    public void keyPressed(){
        System.out.println("key pressed");
        if (key == CODED){
            switch (keyCode){
                case UP:
                    players.get(2).setVelocity(0, -2);
                    break;
                case DOWN:
                    players.get(2).setVelocity(0, 2);
                    break;
                case LEFT:
                    players.get(2).setVelocity(-2, 0);
                    break;
                case RIGHT:
                    players.get(2).setVelocity(2, 0);
                    break;
            }
        }else {
            switch (key){
                case 'w':
                    players.get(0).setVelocity(0, -2);
                    break;
                case 's':
                    players.get(0).setVelocity(0, 2);
                    break;
                case 'a':
                    players.get(0).setVelocity(-2, 0);
                    break;
                case 'd':
                    players.get(0).setVelocity(2, 0);
                    break;
                //(second player)
                case 'i':
                    players.get(1).setVelocity(0, -2);
                    break;
                case 'j':
                    players.get(1).setVelocity(-2, 0);
                    break;
                case 'k':
                    players.get(1).setVelocity(0, 2);
                    break;
                case 'l':
                    players.get(1).setVelocity(2, 0);
                    break;
            }
        }
    }
    @Override
    public void keyReleased() {
        if (key == CODED){
            switch (keyCode) {
                case UP:
                    players.get(2).setVelocity(0, 0);
                    break;
                case LEFT:
                players.get(2).setVelocity(0, 0);
                break;
                case DOWN: 
                    players.get(2).setVelocity(0, 0);
                    break;
                case RIGHT: 
                    players.get(2).setVelocity(0, 0);
                break;
            }
        } 
        else
        {
            switch (key) {
                case 'w': 
                    players.get(0).setVelocity(0, 0); 
                    break;
                case 'a':
                    players.get(0).setVelocity(0, 0);
                    break;
                case 's':
                    players.get(0).setVelocity(0, 0);
                    break;
                case 'd':
                    players.get(0).setVelocity(0, 0);
                    break;
                case 'i':
                    players.get(1).setVelocity(0, 0);
                    break;
                case 'j':
                    players.get(1).setVelocity(0, 0);
                    break;
                case 'k':
                    players.get(1).setVelocity(0, 0);
                    break;
                case 'l':
                    players.get(1).setVelocity(0, 0);
                    break;
              }
         }
    }
    @Override
    public void mousePressed() {
        System.out.println("mouse pressed");
        System.out.println(mazemap.checkBound(mouseX, mouseY));
    }

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

