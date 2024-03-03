package Arkanoid;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        JFrame frame = new JFrame();
        Arkanoid arkanoid = new Arkanoid();
        frame.add(arkanoid);
        frame.setSize(570,500);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);
        frame.setVisible(true);

        ImageIcon icon = new ImageIcon("src\\Arkanoid\\icon.png");
        frame.setIconImage(icon.getImage());

        Timer timer = new Timer(100, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.setTitle("Arkanoid score: " + arkanoid.score + "         Highscore: " + arkanoid.highscore);
            }
        });
        timer.start();

        KeyboardFocusManager.getCurrentKeyboardFocusManager().addKeyEventDispatcher(e -> {
            if (e.getID() == KeyEvent.KEY_PRESSED && e.getKeyCode() == KeyEvent.VK_SPACE) {
                arkanoid.handleSpaceBar();
            }
            return false;
        });


        while (true) {
            arkanoid.moveBall();
            arkanoid.repaint();

            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
