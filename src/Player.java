import processing.core.PImage;
import processing.core.PVector;

import java.util.ArrayList;

/**
 * Created by W.C.Lin on 2017/5/25.
 */
public class Player {
    Main parent;
    int id;
    public ArrayList<Item> matchItems = new ArrayList<>();
    public ArrayList<Player> playersImg = new ArrayList<>();
    PImage UpImg;
    PImage DownImg;
    PImage LeftImg;
    PImage RightImg;
    PVector position;
    PVector velocity = new PVector(0, 0);
    PVector pvelocity = new PVector(0, -2);
    PVector multiplier = new PVector(1, 1);
    float radius, m;
    public Player(Main parent, int Id, PImage UpImage, PImage DownImage, PImage LeftImage, PImage RightImage, float x, float y, float r_) {
        this.parent = parent;
        position = new PVector(x, y);
        id =Id;
        UpImg = UpImage;
        DownImg = DownImage;
        LeftImg = LeftImage;
        RightImg = RightImage;
        radius = r_;
        m = radius*0.1f;
    }
    public void update() {
        // pre-calculate
        PVector tmp = PVector.add(position, velocity);
//        if (parent.mazemap.checkBall(tmp, radius)){
            position = tmp;
//        }        
        for(Item t : matchItems)
            t.update();
        if(!matchItems.isEmpty() && matchItems.get(0).isDisappearing() == true)
            matchItems.remove(0);
    }
    int imgSize = 45;
    int r = 102,g=104,b=234;
    public void display() {

        parent.noStroke();
        parent.fill(r, g, b);
        if (!(velocity.x == 0 && velocity.y == 0))
        {
            pvelocity.x = velocity.x;
            pvelocity.y = velocity.y;
        }
        if (pvelocity.y < 0) parent.image(UpImg, position.x-radius, position.y-radius, radius * 2, radius * 2);
        if (pvelocity.y > 0) parent.image(DownImg, position.x-radius, position.y-radius, radius * 2, radius * 2);
        if (pvelocity.x < 0) parent.image(LeftImg, position.x-radius, position.y-radius, radius * 2, radius * 2);
        if (pvelocity.x > 0) parent.image(RightImg, position.x-radius, position.y-radius, radius * 2, radius * 2);
    }

    public boolean checkCollisionItem(Item t) {
        PVector toolpos = new PVector(t.getPosititon().x, t.getPosititon().y);
        toolpos.sub(position);
        float rr = toolpos.x*toolpos.x + toolpos.y*toolpos.y;
        if(rr <= (radius + t.radius)*(radius + t.radius)) {
            if(t.isEffective())
                return true;
        }
        return false;
    }
    public boolean checkCollisionPlayer(Player c) {
        PVector p = new PVector(c.getPosition().x, c.getPosition().y);
        p.sub(position);
        float r1 = (radius + c.radius)*(radius + c.radius);
        if(r1 == p.x*p.x + p.y*p.y) {
            return true;
        }
        return false;
    }
    public void setVelocity(int i, int j) {
        velocity.x = i*multiplier.x;
        velocity.y = j*multiplier.y;
    }
    public void addItem(Item T) {
        matchItems.add(T);
        T.update();
        PVector v = new PVector(1, 1);
        for(Item t : matchItems)
            v = t.useItem(this, v);
        multiplier.x = v.x;
        multiplier.y = v.y;
    }
    public int getId() {return id;}
    public void setId(int Id) { id = Id;}
}

