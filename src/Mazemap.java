import org.omg.PortableInterceptor.SYSTEM_EXCEPTION;
import processing.core.PImage;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by davidhcefx on 2017/5/21.
 */
public class Mazemap {
    private Main parent;
    boolean[][] map;
    private PImage maze1;
    private PImage maze2;
    private PImage maze3;

    public Mazemap(Main par){
        parent = par;
        maze1 = parent.loadImage("map.jpg");
        loadmap(parent.curMaze);
        map = new boolean[21][21];
    }

    private void loadmap(int index){
        String filename;
        switch (index){
            case 1:
                filename = "mapgrid.txt";
                break;
            case 2:
                filename = "second map...";
        }
        try {
            Scanner s1 = new Scanner(new File(filename));
            for (int i = 0; i<21; i++){
                for (int j = 0; j<21; j++){
                 if (s1.nextInt() == 1) map[i][j] = true;
                }//for j
            }//for i
        }//try
        catch (FileNotFoundException e) {
            e.printStackTrace();
        }//catch
    }

    public boolean checkBound(int x, int y) {
        int smallx, smally;
        switch (parent.curMaze) {
            case 1:
                smallx = x/71;
                smally = y/71;
            case 2:
                // maze2 ...
            default:
                smallx = 0;
                smally = 0;
        }
        if (smallx >= map.length) return false;
        if (smally >= map[smallx].length) return false;
        return map[smallx][smally];
    }

    public void display(){
        switch (parent.curMaze){
            case 1:
                parent.image(maze1,0,20,700, 700);
                break;
            case 2:
                // do something
        }
    }

}
