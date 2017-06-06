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
    PImage UpImg;
    PImage DownImg;
    PImage LeftImg;
    PImage RightImg;

    PVector position;
    PVector velocity = new PVector(0, 0);
    PVector multiplier = new PVector(1, 1);
    PVector pvelocity = new PVector(0, -2);  // previous V for direction showing
    float baseVelocity = 6;
    float radius;

    public Player(Main parent, int Id, PImage UpImage, PImage DownImage, PImage LeftImage, PImage RightImage, float x, float y, float r_) {
        this.parent = parent;
        position = new PVector(x, y);
        id =Id;
        UpImg = UpImage;
        DownImg = DownImage;
        LeftImg = LeftImage;
        RightImg = RightImage;
        radius = r_;
    }

    public void update() {
        // pre-calculate
        PVector tmp = PVector.add(position, velocity);
//        if (parent.mazemap.checkBall(tmp, radius)){
            position = tmp;
//        }else {  // add failure
//            PVector
//            for (PVector v=velocity) {
//                tmp.sub(1, 1);  // try lesser one
//                if (parent.mazemap.checkBall(tmp, radius)) {
//                    position = tmp;
//                    break;
//                }
//            }
//        }

        for(Item t : matchItems)
            t.update();
        if(!matchItems.isEmpty() && matchItems.get(0).isDisappearing() == true)
            matchItems.remove(0);
    }

    public void display() {
        if (! (velocity.x == 0 && velocity.y == 0)) {
            pvelocity.x = velocity.x;
            pvelocity.y = velocity.y;
        } // else remain the same

        PImage tmp;
        if (pvelocity.y < 0){
            tmp = UpImg;
        }else if (pvelocity.y > 0){
            tmp = DownImg;
        }else if (pvelocity.x < 0){
            tmp = LeftImg;
        }else {
            tmp = RightImg;
        }
        parent.image(tmp, position.x-radius, position.y-radius, radius * 2, radius * 2);
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

    public void addItem(Item T) {
        matchItems.add(T);
        T.update();
        PVector m = new PVector(1, 1);
        for(Item t : matchItems)
            m = t.useItem(this, m);
        multiplier.x = m.x;
        multiplier.y = m.y;
    }

    moveState curState = moveState.STOP;
    boolean[] keydown = {false, false, false, false}; // record which keys are constantly down
    public void keyPressed(moveKey key){
        switch (key){
            case UP:
                keydown[0] = true;
                curState = moveState.UP;
                velocity = new PVector(0, -multiplier.y * baseVelocity);
                break;
            case DOWN:
                keydown[1] = true;
                curState = moveState.DOWN;
                velocity = new PVector(0, multiplier.y * baseVelocity);
                break;
            case LEFT:
                keydown[2] = true;
                curState = moveState.LEFT;
                velocity = new PVector(-multiplier.x * baseVelocity, 0);
                break;
            case RIGHT:
                keydown[3] = true;
                curState = moveState.RIGHT;
                velocity = new PVector(multiplier.x * baseVelocity, 0);
                break;
        }
    }

    public void keyReleased(moveKey key){
        switch (key){
            case UP:
                keydown[0] = false;
                break;
            case DOWN:
                keydown[1] = false;
                break;
            case LEFT:
                keydown[2] = false;
                break;
            case RIGHT:
                keydown[3] = false;
                break;
        }
        // stop if curState's key no longer down
        switch (curState){
            case UP:
                if (! keydown[0]){
                    curState = moveState.STOP;
                    velocity = new PVector(0,0);
                }
                break;
            case DOWN:
                if (! keydown[1]){
                    curState = moveState.STOP;
                    velocity = new PVector(0,0);
                }
                break;
            case LEFT:
                if (! keydown[2]){
                    curState = moveState.STOP;
                    velocity = new PVector(0,0);
                }
                break;
            case RIGHT:
                if (! keydown[3]){
                    curState = moveState.STOP;
                    velocity = new PVector(0,0);
                }
                break;
            case STOP:
                velocity = new PVector(0,0);
                break;
        }
    }

    public int getId() {return id;}
    public void setId(int Id) { id = Id;}
    public PVector getPosition(){ return position; }
    public float getBaseVelocity(){ return baseVelocity; }
    public void setBaseVelocity(float bv ){ baseVelocity = bv; }
}

enum moveKey {
    UP, DOWN, LEFT, RIGHT;
}
enum moveState {
    UP, DOWN, LEFT, RIGHT, STOP;
}