import java.awt.*;

public class Life {
    int x,y;
    public Life(int x){
        this.x = x;
        y = 25;
    }

    public void draw(Graphics g){
        g.setColor(Color.MAGENTA);
        g.fillOval(x,y,20,20);
    }

    public void tick(){

    }
}
