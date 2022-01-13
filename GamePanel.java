import java.util.Random;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class GamePanel extends JPanel implements ActionListener {

    static final int GRID_SIZE = 30;
    static final int UNIT_SIZE = 25;
    static final int SCREEN_SIZE = GRID_SIZE * UNIT_SIZE;
    static final int GAME_UNITS = GRID_SIZE * GRID_SIZE / UNIT_SIZE;
    static final int DELAY = 75;
    
    final int[] x = new int[GAME_UNITS];
    final int[] y = new int[GAME_UNITS];

    int bodyParts = 6;
    int applesEaten;
    
    int appleX, appleY;

    char direction = 'R';

    boolean running = false;
    Timer timer;
    Random random;

    GamePanel() {
        random = new Random();
        
        this.setPreferredSize(new Dimension(SCREEN_SIZE, SCREEN_SIZE));
        this.setBackground(Color.black);
        this.setFocusable(true);
        this.addKeyListener(new MyKeyAdapter());
        
        startGame();
    }

    public void startGame() {
        timer = new Timer(DELAY, this);
        timer.start();
        running = true;
        newApple();

        // Set snake postion
        for (int i = 0; i < bodyParts; i++) {
            x[i] = (bodyParts-i+1) * UNIT_SIZE;
        }
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        draw(g);
    }

    public void draw(Graphics g) {
        if (running) {
            // Draw apple
            g.setColor(Color.red);
            g.fillOval(appleX, appleY, UNIT_SIZE, UNIT_SIZE);

            for (int i = 0; i < bodyParts; i++) {
                if (i == 0) g.setColor(new Color(45,180,0));
                else g.setColor(Color.green);
                g.fillRect(x[i], y[i], UNIT_SIZE, UNIT_SIZE);
            }

            // Show score
            g.setColor(Color.red);
            g.setFont(new Font("Ink Free", Font.BOLD, 40));
            FontMetrics metrics = getFontMetrics(g.getFont());
            String score = "Score: " + applesEaten;
            g.drawString(score, (SCREEN_SIZE-metrics.stringWidth(score))/2, g.getFont().getSize());
        }

        else gameOver(g);
    }

    public void move() {
        for (int i = bodyParts-1; i > 0; i--) {
            x[i] = x[i-1]; y[i] = y[i-1];
        }

        switch (direction) {
            case 'U': 
                y[0] -= UNIT_SIZE;
                break;
            case 'D':
                y[0] += UNIT_SIZE;
                break;
            case 'L': 
                x[0] -= UNIT_SIZE;
                break;
            case 'R':
                x[0] += UNIT_SIZE;
                break;
        }
    }

    public void newApple() {
        boolean good;

        do {
            appleX = random.nextInt((int)GRID_SIZE) * UNIT_SIZE;
            appleY = random.nextInt((int)GRID_SIZE) * UNIT_SIZE;
            
            good = true;
            for (int i = 0; i < bodyParts && good; i++) {
                good &= (appleX != x[i] || appleY != y[i]);
            }

        } while (!good);
    }

    public void checkApple() {
        if (appleX == x[0] && appleY == y[0]) {
            applesEaten++;
            bodyParts++;
            newApple();
        }
    }

    public void checkCollision() {
        for (int i = 1; i < bodyParts && running; i++) {
            running &= (x[i] != x[0] || y[i] != y[0]);
        }

        running &= (0 <= x[0] && x[0] <= SCREEN_SIZE);
        running &= (0 <= y[0] && y[0] <= SCREEN_SIZE);

        if (!running) timer.stop();
    }

    public void gameOver(Graphics g) {
        // Show score
        g.setColor(Color.red);
        g.setFont(new Font("Ink Free", Font.BOLD, 40));
        FontMetrics metrics = getFontMetrics(g.getFont());
        String score = "Score: " + applesEaten;
        g.drawString(score, (SCREEN_SIZE-metrics.stringWidth(score))/2, g.getFont().getSize());

        // Show game over
        g.setColor(Color.red);
        g.setFont(new Font("Ink Free", Font.BOLD, 40));
        FontMetrics metrics1 = getFontMetrics(g.getFont());
        g.drawString("Game Over", (SCREEN_SIZE-metrics1.stringWidth("Game Over"))/2, SCREEN_SIZE/2);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (running) {
            move();
            checkApple();
            checkCollision();
        }
        repaint();
    }
    
    public class MyKeyAdapter extends KeyAdapter {

        @Override
        public void keyPressed(KeyEvent e) {
            switch(e.getKeyCode()) {
                case KeyEvent.VK_LEFT:
                    if (direction != 'R') direction = 'L';
                    break;
                case KeyEvent.VK_A:
                    if (direction != 'R') direction = 'L';
                    break;
                case KeyEvent.VK_RIGHT:
                    if (direction != 'L') direction = 'R';
                    break;
                case KeyEvent.VK_D:
                    if (direction != 'L') direction = 'R';
                    break;
                case KeyEvent.VK_UP:
                    if (direction != 'D') direction = 'U';
                    break;
                case KeyEvent.VK_W:
                    if (direction != 'D') direction = 'U';
                    break;
                case KeyEvent.VK_DOWN:
                    if (direction != 'U') direction = 'D';
                    break;
                case KeyEvent.VK_S:
                    if (direction != 'U') direction = 'D';
                    break;
                case KeyEvent.VK_SPACE:
                    System.out.println(running);
                    if (!running) startGame();
                    break;
            }
        }
    }
}
