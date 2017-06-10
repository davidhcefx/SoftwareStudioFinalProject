import processing.core.PImage;

/**
 * Created by W.C.Lin on 2017/6/8.
 */
public class Scoreboard {
    private Main parent;
    PImage scoreboard;
    PImage Icon;
    public Scoreboard(Main parent, PImage Image, PImage IconImg) {
        this.parent = parent;
        scoreboard = Image;
        Icon = IconImg;
    }
    public void display()
    {
        parent.image(scoreboard, 700, 0, 130, 700);
    }
}
