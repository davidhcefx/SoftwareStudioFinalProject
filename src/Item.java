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
    float iconx = 700, icony = 20;
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
    int imgSize = 20;

    public void update() {
        if(state == ItemState.Show || state == ItemState.Hide) state = ItemState.Match;
        else if(state == ItemState.Match) time--;
        if(time == 0) state = ItemState.Disappear;
    }

    public PVector useItem(Player b, PVector v, PVector p, int i) {
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
                v.x = v.x * 1.5f;
                v.y = v.y * 1.5f;
                break;
        }
        if (toolid != 2) {
            Ani.to(this, 3f, "iconx", p.x);
            Ani.to(this, 3f, "icony", p.y + i * radius * 2);
        }
        return v;
    }

    public void display() {
//        parent.noStroke();
//        parent.imageMode(PConstants.CENTER);
        if(state == ItemState.Hide) parent.image(tmpImg ,position.x-radius, position.y-radius, radius*2, radius*2);
        if(state == ItemState.Show) parent.image(Img ,position.x-radius, position.y-radius, radius*2, radius*2);
        if(state == ItemState.Match) {
            if(time > 790) parent.image(Img ,position.x-radius, position.y-radius, imgSize*2, imgSize*2);
            if(time < 790 && toolid != 2){
                parent.image(Img ,iconx, icony, radius*2, radius*2);
                //show time - number
                /*parent.textSize(30);
                parent.fill(255);
                parent.text("" + time/20, iconx+40, icony+30);*/
                //show time
                parent.noStroke();
                parent.fill(0, 177, 30);
                parent.rect(iconx+40, icony+12,time/10,12, 3);
            }
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
    public void setIcon(PVector p){
        iconx = p.x;
        icony = p.y+230;
    }
}
enum ItemState{
    Hide,Show,Match,Disappear
}
