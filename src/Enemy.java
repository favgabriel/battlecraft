import java.awt.*;
import java.util.Random;

public class Enemy {
    float x,y;
    int type;
    int w,h;
    public Enemy(){
        Random random = new Random();
        x =random.nextFloat()*370;
        y= random.nextFloat()*350+50;
        type = new Random().nextInt(4);
        if (type == 0){
            w =20;
            h =20;
        }else if (type==1){
            w =25;
            h =25;
        }else {
            w =30;
            h =20;
        }
    }

    public void draw(Graphics g){
        if (type==0) {
            //healthy enemy
            g.setColor(Color.green);
        }else if (type==1){
            //dangerous enemy
            g.setColor(Color.BLACK);
        }else {
            //cunny enemy
            g.setColor(Color.ORANGE);
        }
        g.fillRect((int) x, (int) y, w, h);
    }

    public void tick() {
        y++;
        if (y>330){
            y=0;
            type = new Random().nextInt(4);
        }
    }

    public int getX() {
        return (int) x;
    }

    public void setX(float x) {
        this.x = x;
    }

    public float getY() {
        return y;
    }

    public void setY(float y) {
        this.y = y;
    }

    public Rectangle bound(){
        return new Rectangle((int) x, (int) y,w,h);
    }
}
