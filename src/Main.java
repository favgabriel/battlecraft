import javax.swing.*;
import java.awt.*;

public class Main {
    public static void main(String[] args) {
        Ship ship = new Ship();

        JFrame frame = new JFrame("Battle Craft");

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
        frame.setSize(400,400);
        frame.add(ship,BorderLayout.CENTER);
        frame.setLocationRelativeTo(null);

    }
}