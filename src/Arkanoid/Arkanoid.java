package Arkanoid;

import java.awt.*;
import java.awt.event.*;
import java.util.*;
import javax.swing.*;
import java.io.*;
public class Arkanoid extends JPanel  {

    private final int PADDLE_WIDTH = 80;
    private final int PADDLE_HEIGHT = 10;
    private final int PADDLE_MOVE = 20;
    private final int BALL_DIAMETER = 20;
    private final int BALL_SPEED = 5;
    private final int BRICK_WIDTH = 50;
    private final int BRICK_HEIGHT = 20;
    private final int GAP = 5;
    private int dx, dy;
    protected int score = 0;
    protected int highscore;
    private boolean ballMoving = false;
    private final Color[] BRICK_COLORS = {Color.RED, Color.ORANGE, Color.YELLOW};

    private ArrayList<Rectangle> bricks;
    private Rectangle paddle;
    private Rectangle ball;
    private File file;
    private FileWriter writer;
    private Scanner scanner;
    private Random random;



    public Arkanoid() {
        readFile();
        bricks = new ArrayList<Rectangle>();
        createBricks();
        paddle = new Rectangle(150, 460, PADDLE_WIDTH, PADDLE_HEIGHT);
        ball = new Rectangle(170, 435, BALL_DIAMETER, BALL_DIAMETER);
        random = new Random();
        if (random.nextBoolean()) {
            dx = BALL_SPEED;
        } else {
            dx = -BALL_SPEED;
        }
        dy = -BALL_SPEED;
        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                int keyCode = e.getKeyCode();
                if (keyCode == KeyEvent.VK_LEFT) {
                    movePaddle(-PADDLE_MOVE);
                } else if (keyCode == KeyEvent.VK_RIGHT) {
                    movePaddle(PADDLE_MOVE);
                }
            }
        });
        setFocusable(true);
        setFocusTraversalKeysEnabled(false);
    }
    private void createBricks() {
        for (int row = 0; row < 5; row++) {
            for (int col = 0; col < 10; col++) {
                Rectangle brick = new Rectangle((col * (BRICK_WIDTH + GAP)) + GAP, (row * (BRICK_HEIGHT + GAP)) + GAP, BRICK_WIDTH, BRICK_HEIGHT);
                bricks.add(brick);
            }
        }
    }

    protected void handleSpaceBar() {
        ballMoving = true;
    }

    public void moveBall() {
        if (!ballMoving) {
            return;
        }

        ball.x += dx;
        ball.y += dy;
        if (ball.x <= 0 || ball.x + BALL_DIAMETER >= getWidth()) {
            dx = -dx;
        }
        if (ball.y <= 0) {
            dy = -dy;
        }
        if (ball.y + BALL_DIAMETER >= paddle.y && ball.x + BALL_DIAMETER >= paddle.x && ball.x <= paddle.x + PADDLE_WIDTH) {
            dy = -dy;
            score++;
        }
        for (int i = 0; i < bricks.size(); i++) {
            Rectangle brick = bricks.get(i);
            if (ball.intersects(brick)) {
                dy = -dy;
                bricks.remove(i);
                score += 10;
            }
        }
        if (ball.y + BALL_DIAMETER >= getHeight()) {
            gameOver();
        }
    }

    private void movePaddle(int dir) {
        if (paddle.x + dir >= 0 && paddle.x + dir <= getWidth() - PADDLE_WIDTH) {
            paddle.x += dir;
        }
    }

    private void gameOver() {
        String message = "GAME OVER! Your score: " + score;
        JOptionPane.showMessageDialog(this, message, "Game Over", JOptionPane.PLAIN_MESSAGE);
        saveFile();
        System.exit(0);
    }

    private void readFile(){
        try {
            file = new File("src\\Arkanoid\\file.txt");
            scanner = new Scanner(file);
            scanner.nextLine();
            highscore = Integer.parseInt(scanner.nextLine());
            scanner.close();

        } catch (FileNotFoundException e) {
            System.out.println("Nie udało się otworzyć pliku");
            e.printStackTrace();
        }
    }

    private void saveFile(){
        try {
            if (score > highscore){
                writer = new FileWriter("src\\Arkanoid\\file.txt");
                writer.write("Highscore \n" + score);
                writer.close();
            }
        } catch (IOException e){
            e.printStackTrace();
        }
    }


    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.setColor(Color.BLACK);
        g.fillRect(0, 0, getWidth(), getHeight());
        g.setColor(Color.WHITE);
        g.fillRect(paddle.x, paddle.y-3, paddle.width, paddle.height);
        for (int i = 0; i < bricks.size(); i++) {
            g.setColor(BRICK_COLORS[i / (10 * 2)]);
            g.fillRect(bricks.get(i).x, bricks.get(i).y, bricks.get(i).width, bricks.get(i).height);
        }

        g.setColor(Color.WHITE);
        g.fillOval(ball.x, ball.y, ball.width, ball.height);
    }


}