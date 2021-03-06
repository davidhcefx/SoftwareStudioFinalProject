import org.omg.PortableInterceptor.SYSTEM_EXCEPTION;
import processing.core.PImage;
import processing.core.PVector;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Created by davidhcefx on 2017/5/21.
 */
public class Mazemap {
    private Main parent;
    boolean[][] map;
    private PImage maze1;
    private PImage maze2;
    private PImage maze3;

    private double[] unitX;  // 用畫面大小換算unit size用的
    private double[] unitY;

    public Mazemap(Main par){
        parent = par;
        maze1 = parent.loadImage("res/map.jpg");
        map = new boolean[21][21];
        // initialize unitX, unitY, offsetY
        unitX = new double[]{ (double)700/21 };
        unitY = new double[]{ (double)700/21 };

        loadmap(parent.curMaze);
    }

    private void loadmap(int index){
        String filename = "";
        switch (index){
            case 1:
                filename = "bin/mapgrid.txt";
                break;
            case 2:
                filename = "second map...";
                break;
        }
        try {
            Scanner s1 = new Scanner(new File(filename));
            for (int i=0; i<21; i++){
                for (int j=0; j<21; j++){
                    if (s1.nextInt() == 1) {
                        map[i][j] = false;  // wall is false
                    }else {
                        map[i][j] = true;
                    }
                }//for j
            }//for i
        }//try
        catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    // wall = false, road = true
    public boolean checkBound(int x, int y) {
        int smallx, smally;
        switch (parent.curMaze) {
            case 1:
                smallx = (int)((double)x/unitX[0]);
                smally = (int)((double)y/unitY[0]);
                break;
            case 2:
                // maze2 ...
            default:
                smallx = 0;
                smally = 0;
        }
        if (smally >= map.length) return false;
        if (smallx >= map[smally].length) return false;
        return map[smally][smallx];
    }

    // check whether a Ball collides wall
    public boolean checkBall(PVector center, float radius){
        // check Right edge
        if (! checkBound((int)(center.x + radius), (int)center.y)) return false;
        // check Left edge
        if (! checkBound((int)(center.x - radius), (int)center.y)) return false;
        // check Bottom edge
        if (! checkBound((int)center.x, (int)(center.y + radius))) return false;
        // check Top edge
        if (! checkBound((int)center.x, (int)(center.y - radius))) return false;
        return true;
    }

    public void display(){
        switch (parent.curMaze){
            case 1:
                parent.image(maze1, 0, 0, 700, 700);
                break;
            case 2:
                // do something
        }
    }

}
