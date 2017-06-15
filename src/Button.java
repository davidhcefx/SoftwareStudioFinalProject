
public class Button {
    Main parent;
    float posX, posY;

    public Button(Main parent) {
        this.parent = parent;
        posX = (float)(830/3);
        posY = (float)(936/2);
    }

    public int display(){
        parent.strokeWeight(1);
        parent.fill (0);
        parent.noStroke();

        if (parent.mouseX>posX && parent.mouseY>posY  && parent.mouseX<posX+264 && parent.mouseY<posY+97){
            parent.image(parent.buttonState[0],posX,posY);
            if (parent.mousePressed) return 1;
            else return 0;
        }
        else {
            parent.image(parent.buttonState[1],posX,posY);
            return 0;
        }
    }//display
}
