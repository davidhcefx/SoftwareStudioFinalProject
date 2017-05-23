import de.looksgood.ani.Ani;
import processing.core.PApplet;
import processing.core.PImage;
import java.util.ArrayList;

public class Main extends PApplet
{
    public int curMaze = 1;
    Mazemap mazemap;
//    ArrayList<Player> players;

    @Override
    public void settings() {
        super.settings();
        size(700, 720);
    }

    @Override
    public void setup() {
        // initialization...
        mazemap = new Mazemap(this);
    }

    @Override
    public void draw() {
        background(52);
        mazemap.display();
    }

    @Override
    public void keyPressed(){
        System.out.println("key pressed");
        if (key == CODED){
            switch (keyCode){
                case UP:
                    break;
                case DOWN:
                    break;
                case LEFT:
                    break;
                case RIGHT:
                    break;
            }
        }else {
            switch (key){
                case 'w':
                    break;
                case 's':
                    break;
                case 'a':
                    break;
                case 'd':
                    break;
                // case 'i': (third player)
            }
        }
    }

    @Override
    public void mousePressed() {
        System.out.println("mouse pressed");
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

