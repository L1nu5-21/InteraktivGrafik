import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferStrategy;
import java.util.ArrayList;

public class Main extends Canvas implements Runnable{

    private boolean running = false;
    private Thread thread;

    // Bob
    private final int bobX = 200;
    private final int bobY = 200;
    private int bobVX = 0;
    private int bobVY = 0;
    private final int bobWidth = 30;
    private final int bobHeight = 30;
    private final Rectangle Bob = new Rectangle(bobX,bobY,bobWidth, bobHeight);

    // Projectiles
    private final ArrayList<Integer> projectileX = new ArrayList<>();
    private final ArrayList<Integer> projectileY = new ArrayList<>();
    private final ArrayList<Integer> projectileVX = new ArrayList<>();
    private final ArrayList<Integer> projectileVY = new ArrayList<>();

    // Bullet
    private final Color bulletColor = Color.darkGray;
    int bulletWidth = 20;
    int bulletHeight = 20;
    private final ArrayList<Rectangle> projectileHitbox = new ArrayList<>();

    // Target
    private final int targetX = (int) (Math.random()*560);
    private final int targetY = (int) (Math.random()*360);
    private final Rectangle target = new Rectangle(targetX, targetY, 40,40);
    private final Color targetColor = Color.green;

    // Steve
    private final Color SteveColor = Color.blue;
    private final int SteveWidth = 30;
    private final int SteveHeight = 30;
    private final int SteveX = 200;
    private final int SteveY = 400;
    private final Rectangle Steve = new Rectangle(SteveX, SteveY, SteveWidth, SteveHeight);
    // private final ArrayList<Rectangle> Steve = new ArrayList<>();

    public Main() {
        setSize(1200,800);
        JFrame frame = new JFrame();
        frame.add(this);
        frame.addKeyListener(new MyKeyListener());
        frame.requestFocus();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }

    public void render() {
        BufferStrategy bs = getBufferStrategy();
        if (bs == null) {
            createBufferStrategy(3);
            return;
        }
        Graphics g = bs.getDrawGraphics();

        // Rita ut den nya bilden
        draw(g);

        g.dispose();
        bs.show();
    }

    public void draw(Graphics g) {
        g.clearRect(0,0,getWidth(),getHeight());
        g.setColor(targetColor);
        g.fillRect(target.x, target.y, target.width, target.height);
        g.setColor(SteveColor);
        g.fillRect(Steve.x, Steve.y, Steve.width, Steve.height);
        g.setColor(Color.pink);
        g.fillRect(Bob.x,Bob.y, Bob.width,Bob.height);
        g.setColor(bulletColor);
        for (int i = 0 ; i < projectileX.size() ; i++) {
            g.fillRect(projectileHitbox.get(i).x,projectileHitbox.get(i).y, projectileHitbox.get(i).width, projectileHitbox.get(i).height);
        }
    }

    public static void main(String[] args) {
        Main minGrafik = new Main();
        minGrafik.start();
    }
    public synchronized void start() {
        running = true;
        thread = new Thread(this);
        thread.start();
    }
    public synchronized void stop() {
        running = false;
        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    public void run() {
        double ns = 1000000000.0 / 60.0;
        double delta = 0;
        long lastTime = System.nanoTime();

        while (running) {
            long now = System.nanoTime();
            delta += (now - lastTime) / ns;
            lastTime = now;

            while(delta >= 1) {
                // Uppdatera koordinaterna
                update();
                // Rita ut bilden med updaterad data
                render();
                delta--;
            }
        }
        stop();
    }
    private void update() {
        while (Steve.x < Bob.x || Steve.x > Bob.x) {

        }
        for (int i = 0; i < projectileX.size(); i++) {
            projectileHitbox.get(i).x = projectileHitbox.get(i).x + projectileVX.get(i);
            projectileHitbox.get(i).y = projectileHitbox.get(i).y + projectileVY.get(i);
            if (projectileHitbox.get(i).intersects(target)) {
                projectileHitbox.remove(i);
                projectileVX.remove(i);
                projectileVY.remove(i);
                projectileX.remove(i);
                projectileY.remove(i);
                break;
            }
            if (projectileHitbox.get(i).intersects(Steve)) {
                projectileHitbox.remove(i);
                projectileVX.remove(i);
                projectileVY.remove(i);
                projectileX.remove(i);
                projectileY.remove(i);
                Steve.x = 0;
                Steve.y = 0;
                Steve.width = 0;
                Steve.height = 0;
                break;
            }
        }
        if (Bob.intersects(target)) {
            switch (direction) {
                case 'a':
                    bobVX = 0;
                    Bob.x +=1;
                    break;
                case 's':
                    bobVY = 0;
                    Bob.y -=1;
                    break;
                case 'w':
                    bobVY = 0;
                    Bob.y +=1;
                    break;
                case 'd':
                    bobVX = 0;
                    Bob.x -=1;
                    break;
                default:
                    bobVX = 0;
                    Bob.x +=0;
            }
        }

        Bob.x += bobVX;
        Bob.y += bobVY;

    }
    char direction = 'd';
    public class MyKeyListener implements KeyListener {

        @Override
        public void keyTyped(KeyEvent e) {

        }

        @Override
        public void keyPressed(KeyEvent e) {
            int bobMaxMS = 10;
            if (e.getKeyChar() == 'a') {
                if (bobVX > -bobMaxMS) bobVX -= 5;
                direction = 'a';
                /*if (e.getKeyChar() == 'w') {direction = 'n';}
                if (e.getKeyChar() == 's') {direction = 'b';}*/
            }
            if (e.getKeyChar() == 'd') {
                if (bobVX < bobMaxMS) bobVX += 5;
                direction = 'd';
                /*if (e.getKeyChar() == 'w') {direction = 'b';}
                if (e.getKeyChar() == 's') {direction = 'c';}*/
            }
            if (e.getKeyChar() == 'w') {
                if (bobVY > -bobMaxMS) bobVY -= 5;
                direction = 'w';
                /*if (e.getKeyChar() == 'd') {direction = 'b';}
                if (e.getKeyChar() == 'a') {direction = 'n';}*/
            }
            if (e.getKeyChar() == 's') {
                if (bobVY < bobMaxMS) bobVY += 5;
                direction = 's';
                /*if (e.getKeyChar() == 'd') {direction = 'c';}
                if (e.getKeyChar() == 'a') {direction = 'v';}*/
            }
            if (e.getKeyChar() == KeyEvent.VK_SPACE) {
                projectileX.add(Bob.x);
                projectileY.add(Bob.y);
                projectileHitbox.add(new Rectangle(Bob.x, Bob.y, bulletWidth, bulletHeight));
                int bulletMaxMS = 20;
                switch (direction){
                    case 'w':
                        projectileVX.add(0);
                        projectileVY.add(-bulletMaxMS);
                        break;
                    case 'a':
                        projectileVX.add(-bulletMaxMS);
                        projectileVY.add(0);
                        break;
                    case 's':
                        projectileVX.add(0);
                        projectileVY.add(bulletMaxMS);
                        break;
                    case 'd':
                        projectileVX.add(bulletMaxMS);
                        projectileVY.add(0);
                        break;
                    default:
                        projectileVX.add(bulletMaxMS);
                        projectileVY.add(bulletMaxMS);
                }

            }
        }

        @Override
        public void keyReleased(KeyEvent e) {
            if (e.getKeyChar() == 'a') {
                bobVX = 0;
            }
            if (e.getKeyChar() == 'd') {
                bobVX = 0;
            }
            if (e.getKeyChar() == 'w') {
                bobVY = 0;
            }
            if (e.getKeyChar() == 's') {
                bobVY = 0;
            }
        }
    }
}