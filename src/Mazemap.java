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

        System.out.println(""+map[0][0]+map[0][1]+map[0][2]);
    }

    private void loadmap(int index){

        // read from file (1000110110101...)

//        for ()
//            for ()
//        if (.nextInt() == 1){
//            map[i][j] = true;
//        }

        map = new boolean[][]{
                {true, true, false, true},
                {false, false, true, false}
        };
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
