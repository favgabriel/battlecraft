import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;
import java.util.Random;
import java.util.concurrent.CopyOnWriteArrayList;

public class Ship extends JPanel implements Runnable, KeyListener {
    int x;
    int y;
    int by, bx;
    int velx;
    Thread thread;
    Rectangle bulletrect;
    private boolean readyToFire = false;
    private boolean shot = false;
    private ArrayList<Enemy> enemyArrayList;
    private ArrayList<Life> lives;
    private String gameScore;
    private int count = 0;
    boolean gameOver = false;
    private long starttime = 0L;
    private long prevtime = 0L;
    BufferedImage image;
    JButton retry= new JButton("Retry");
    public Ship() {
        x = 150;
        y = 345;
        enemyArrayList = new ArrayList<>();
        lives = new ArrayList<>();
        addKeyListener(this);
        //these parameters are necessary
        setFocusable(true);
        thread = new Thread(this);
        thread.start();

        for (int i = 0; i < 3; i++) {
            lives.add(new Life(10 + (30 * i)));
        }

        for (int i = 0; i < 10; i++) {
            enemyArrayList.add(new Enemy());
        }
        try {
            image = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("bg.jpg")));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        //retry = new JButton("Retry");
        retry.addActionListener(e -> {
            count=0;
            gameOver = false;
            for (int i = 0; i < 3; i++) {
                lives.add(new Life(10 + (30 * i)));
            }
            enemyArrayList.clear();
            for (int i = 0; i < 10; i++) {
                enemyArrayList.add(new Enemy());
            }
            retry.setVisible(false);
        });
        setSize(400,400);
        setLayout(new FlowLayout(FlowLayout.LEFT));
        add(retry);
        //setBackground(Color.GRAY);
    }

    private void score() {
        if (!gameOver) {
            count++;
        }
        gameScore = String.valueOf(count);
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        g.drawImage(image,0,0,null);

        g.setColor(Color.BLUE);
        g.fillRect(x, y, 35, 10);
        if (shot) {
            g.setColor(Color.RED);
            g.fillRect(bulletrect.x, bulletrect.y, bulletrect.width, bulletrect.height);
        }
        for (Enemy enemy : enemyArrayList) {
            enemy.draw(g);
        }
        for (int i = 0; i < lives.size(); i++) {
            lives.get(i).draw(g);
        }

        g.setColor(Color.CYAN);
        g.drawString(gameOver ? "Game Over score: "+gameScore : "score: "+gameScore,130,50);
        //retry.paint(g);
    }

    @Override
    public void run() {
        try {
            while (true) {
                //repaint is necessary to make the loop run
                repaint();
                for (Enemy enemy : enemyArrayList) {
                    enemy.tick();
                }
                if (enemyArrayList.size()==0){
                    for (int i = 0; i < 10; i++) {
                        enemyArrayList.add(new Enemy());
                    }
                }
                retry.setVisible(gameOver);
                score();
                collision();
                shoot();
                move();
                Thread.sleep(5);
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    private void move() {
        x += velx;
        if (x >= 350) {
            x = 350;
        }
        if (x <= 2) {
            x = 2;
        }
    }

    private void setVelx(int vlx) {
        velx = vlx;
    }

    @Override
    public void keyPressed(KeyEvent e) {
        int keycode = e.getKeyCode();
        if (keycode == KeyEvent.VK_LEFT) {
            setVelx(-1);
        } else if (keycode == KeyEvent.VK_RIGHT) {
            setVelx(1);
        } else if (keycode == KeyEvent.VK_SPACE) {
            if (bulletrect == null) {
                readyToFire = true;
            }
            bx = x + 20;
            by = y - 7;
            if (readyToFire) {
                bulletrect = new Rectangle(bx, by, 3, 5);
                shot = true;
            }
        }
    }

    private void shoot() {
        if (shot) {
            bulletrect.y--;
        }
    }

    private void collision() {
        CopyOnWriteArrayList<Enemy> copyOnWriteArrayList = new CopyOnWriteArrayList<>(enemyArrayList);
        if (shot) {
            //remove shot enemies
            for (Enemy enemy : copyOnWriteArrayList) {
                if (bulletrect.y > enemy.bound().y) {
                    if (enemy.bound().intersects(bulletrect)) {
                        if (!gameOver) {
                            bulletrect = new Rectangle(0, 0, 0, 0);
                            enemyArrayList.remove(enemy);
                            readyToFire = true;
                        }
                    }
                }
            }
        }
        // check for killed
        for (Enemy enemy : copyOnWriteArrayList) {
            if (enemy.y < y) {
                if (enemy.bound().intersects(collided())) {
                    if (lives.size() > 0) {
                        lives.remove(0);
                    }
                    if (lives.size() == 0) {
                        gameOver = true;
                        //retry.setVisible(true);
                    }
                    if (!gameOver) {
                        enemyArrayList.remove(enemy);
                    }
                }
            }
        }
/*
        for (int i = 1; i < enemyArrayList.size(); i+=2) {
            Enemy ist = enemyArrayList.get(i);
            Enemy snd = enemyArrayList.get(i-1);
            if (snd.bound().intersects(ist.bound())) {
                ist.setX(snd.x + 20);
            }
            if (snd.bound().intersects(ist.bound())) {
                ist.setY(snd.y + 10);
            }
        }*/
    }

    @Override
    public void keyReleased(KeyEvent e) {
        int keycode = e.getKeyCode();
        if (keycode == KeyEvent.VK_LEFT) {
            setVelx(0);
        } else if (keycode == KeyEvent.VK_RIGHT) {
            setVelx(0);
        } else if (keycode == KeyEvent.VK_SPACE) {
            readyToFire = false;
            if (bulletrect.y <= -5) {
                bulletrect = new Rectangle(0, 0, 0, 0);
                shot = false;
                readyToFire = true;
            }
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    private Rectangle collided() {
        return new Rectangle(x, y, 40 / 2, 20 / 2);
    }
}
