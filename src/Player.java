import processing.core.PImage;
import processing.core.PVector;
import de.looksgood.ani.Ani;

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

    PVector Iconpos;
    PVector position;
    PVector velocity = new PVector(0, 0);
    PVector multiplier = new PVector(1, 1);
    PVector pvelocity = new PVector(0, -2);  // previous V for direction showing
    float baseVelocity = 2.3f;
    float radius;

    public Player(Main parent, int Id, ArrayList<PImage> playersImg, float x, float y, float r_) {
        this.parent = parent;
        position = new PVector(x, y);
        Iconpos = new PVector(705, 235*Id);
        id = Id;
        switch (id){
            case 0:
                r = 255; g = 48; b = 48;
                break;
            case 1:
                r = 255; g = 215; b = 0;
                break;
            case 2:
                r = 50; g = 205; b = 50;
                break;
        }
        // setting PImages
        UpImg =  playersImg.get(Id*4 + 0);
        DownImg = playersImg.get(Id*4 + 1);
        LeftImg = playersImg.get(Id*4 + 2);
        RightImg = playersImg.get(Id*4 + 3);
        radius = r_;
    }

    public void update() {
        // pre-calculate
        PVector tmp = PVector.add(position, velocity);
        if (parent.mazemap.checkBall(tmp, radius)){
            position = tmp;
        }else { // add failure -> add bit by bit
            vAddBitByBit();
        }

        for(Item t : matchItems)
            t.update();
        if(!matchItems.isEmpty() && matchItems.get(0).isDisappearing() == true) {
            matchItems.remove(0);
            PVector v = new PVector(1, 1);
            int numofIcon = 0;
            for (Item t : matchItems) {
                if(t.getToolid() != 2) numofIcon ++;
                v = t.useItem(this, v, Iconpos, numofIcon);
            }
            multiplier.set(v);
        }
    }

    int r, g, b;
    public void display() {
        parent.stroke(r, g, b);
        parent.strokeWeight(4);
        parent.fill(r, g, b);
        parent.rect(Iconpos.x-5, Iconpos.y,28,28,5);
        parent.fill(255, 255, 255, 0);
        parent.rect(Iconpos.x-5, Iconpos.y,127,230);

        parent.noStroke();
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
        parent.image(UpImg, Iconpos.x -1, Iconpos.y +3, radius * 2, radius * 2);
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
        float rr = p.x*p.x + p.y*p.y;
        if(rr <= (radius + c.radius)*(radius + c.radius))
            return true;
        else
            return false;
    }

    public void addItem(Item T) {
        T.setIcon(Iconpos);
        matchItems.add(T);
        T.update();
        PVector m = new PVector(1, 1);
        int numofIcon = 0;
        for(Item t : matchItems)
        {
            if(t.getToolid() != 2) numofIcon ++;
            m = t.useItem(this, m, Iconpos, numofIcon);
        }
        multiplier.x = m.x;
        multiplier.y = m.y;
    }

    public void shrink(){
        Ani.to(this, 8f, "radius", 0);
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

    private void vAddBitByBit(){
        PVector tmp = position;
        if (velocity.x != 0){
            if (velocity.x > 0){
                // RIGHT
                float vxtmp = velocity.x;
                while (vxtmp > 1f){
                    tmp.x += 1f;
                    vxtmp -= 1f;
                    if (! parent.mazemap.checkBall(tmp, radius)){
                        tmp.x -= 1f;  // restore
                        position = tmp;
                        return;
                    }
                }
                tmp.x += vxtmp;  // remaining
                if (! parent.mazemap.checkBall(tmp, radius)) tmp.x -= vxtmp;
                position = tmp;
            }else {
                // LEFT
                float vxtmp = velocity.x;
                while (vxtmp < -1f){
                    tmp.x += -1f;
                    vxtmp -= -1f;
                    if (! parent.mazemap.checkBall(tmp, radius)){
                        tmp.x -= -1f;  // restore
                        position = tmp;
                        return;
                    }
                }
                tmp.x += vxtmp;  // remaining
                if (! parent.mazemap.checkBall(tmp, radius)) tmp.x -= vxtmp;
                position = tmp;
            }
        }else if (velocity.y != 0){
            if (velocity.y > 0){
                // DOWN
                float vytmp = velocity.y;
                while (vytmp > 1f){
                    tmp.y += 1f;
                    vytmp -= 1f;
                    if (! parent.mazemap.checkBall(tmp, radius)){
                        tmp.y -= 1f;  // restore
                        position = tmp;
                        return;
                    }
                }
                tmp.y += vytmp;  // remaining
                if (! parent.mazemap.checkBall(tmp, radius)) tmp.y -= vytmp;
                position = tmp;
            }else {
                // UP
                float vytmp = velocity.y;
                while (vytmp < -1f){
                    tmp.y += -1f;
                    vytmp -= -1f;
                    if (! parent.mazemap.checkBall(tmp, radius)){
                        tmp.y -= -1f;  // restore
                        position = tmp;
                        return;
                    }
                }
                tmp.y += vytmp;  // remaining
                if (! parent.mazemap.checkBall(tmp, radius)) tmp.y -= vytmp;
                position = tmp;
            }
        }
    }

    public int getId() {return id;}
    public void setId(int Id) {
        id = Id;
        switch (id){
            case 0:
                Ani.to(this, 8f, "r", 255);
                Ani.to(this, 8f, "g", 48);
                Ani.to(this, 8f, "b", 48);
                break;
            case 1:
                Ani.to(this, 8f, "r", 255);
                Ani.to(this, 8f, "g", 215);
                Ani.to(this, 8f, "b", 0);
                break;
            case 2:
                Ani.to(this, 8f, "r", 50);
                Ani.to(this, 8f, "g", 205);
                Ani.to(this, 8f, "b", 50);
                break;
        }
    }
    public PVector getPosition(){ return position; }
    public float getBaseVelocity(){ return baseVelocity; }
    public void setBaseVelocity(float bv ){ baseVelocity = bv; }
}

enum moveKey {
    UP(0), DOWN(1), LEFT(2), RIGHT(3);

    private int val;
    private moveKey(int i){ val = i; }
    public int getVal(){ return val; }
    public static moveKey toMoveKey(int i){
        switch (i){
            case 0: return UP;
            case 1: return DOWN;
            case 2: return LEFT;
            case 3: return RIGHT;
            default: return UP;
        }
    }
}
enum moveState {
    UP, DOWN, LEFT, RIGHT, STOP;
}
