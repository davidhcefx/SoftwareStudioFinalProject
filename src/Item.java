import de.looksgood.ani.Ani;
import processing.core.PConstants;
import processing.core.PImage;
import processing.core.PVector;

import java.util.Random;

/**
 * Created by W.C.Lin on 2017/5/18.
 */
public class Item {
    Main parent;
    PImage tmpImg;
    PImage Img;
    int toolid;
    int time = 800;
    PVector position;
    Random random = new Random();
    ItemState state ;
    float radius, m;

    public Item(Main parent, PImage tmpImage, PImage Image,  int Imageid, float x, float y, float r_) {
        this.parent = parent;
        position = new PVector(x, y);
        tmpImg = tmpImage;
        Img = Image;
        toolid = Imageid;
        int n = random.nextInt(4);
        if(n == 0) state = ItemState.Hide;
        else state = ItemState.Show;
        radius = r_;
        m = radius*0.1f;
    }
    int imgSize = 35;

    public void update() {
        if(state == ItemState.Show || state == ItemState.Hide) state = ItemState.Match;
        else if(state == ItemState.Match) time--;
        if(time == 0) state = ItemState.Disappear;
    }

    public PVector useItem(Player b, PVector v) {
        switch (toolid) {
           case 0://Mushroom
                v.x = v.x * (-1);
                v.y = v.y * (-1);
                break;
           case 1://Pill
                v.x = 0;
                v.y = 0;
                break;
           case 2://spiral
                break;
           case 3://wing
                v.x = v.x*2;
                v.y = v.y*2;
                break;
            }
        return v;
    }

    public void display() {

        parent.noStroke();
        parent.imageMode(PConstants.CENTER);
        if(state == ItemState.Hide) parent.image(tmpImg ,position.x, position.y, radius*2, radius*2);
        if(state == ItemState.Show) parent.image(Img ,position.x, position.y, radius*2, radius*2);
        if(state == ItemState.Match) {
            if(time > 790) parent.image(Img ,position.x, position.y, imgSize*2, imgSize*2);
        }
    }
    public PVector getPosititon(){
        return position;
    }
    public int getToolid() {return toolid;}
    public boolean isEffective() {return state == ItemState.Show || state == ItemState.Hide;}
    public boolean isDisappearing() {return state == ItemState.Disappear;}
    public int getTime() {
        if(state == ItemState.Match) return time;
        else return 0;
    }
}
enum ItemState{
    Hide,Show,Match,Disappear
}
