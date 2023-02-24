import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferStrategy;
import java.util.ArrayList;

public class Main extends Canvas implements Runnable{
    private BufferStrategy bs;

    private boolean running = false;
    private Thread thread;

    // Bob
    private int bobX = 200;
    private int bobY = 200;
    private int bobVX = 0;
    private int bobVY = 0;
    private int bobWidth = 30;
    private int bobHeight = 30;
    private int bobMaxMS = 10;
    private Rectangle Bob = new Rectangle(bobX,bobY,bobWidth, bobHeight);

    // Projectiles
    private ArrayList<Integer> projectileX = new ArrayList<Integer>();
    private ArrayList<Integer> projectileY = new ArrayList<Integer>();
    private ArrayList<Integer> projectileVX = new ArrayList<Integer>();
    private ArrayList<Integer> projectileVY = new ArrayList<Integer>();

    // Bullet
    private int bulletWidth = 20;
    private int bulletHeight = 20;
    private int bulletMaxMS = 20;




    private int x = 0;
    private int y = 0;
    private Rectangle hitbox = new Rectangle(x,y,30,30);

    private int targetX = (int) (Math.random()*560);
    private int targetY = (int) (Math.random()*360);
    private Rectangle target = new Rectangle(targetX, targetY, 40,40);
    private Color targetColor = Color.green;

    public Main() {
        setSize(600,400);
        JFrame frame = new JFrame();
        frame.add(this);
        frame.addKeyListener(new MyKeyListener());
        frame.requestFocus();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }

    public void render() {
        bs = getBufferStrategy();
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
        g.setColor(Color.pink);
        g.fillRect(Bob.x,Bob.y, Bob.width,Bob.height);
        for (int i = 0 ; i < projectileX.size() ; i++) {
            g.fillRect(projectileX.get(i), projectileY.get(i), bulletWidth, bulletHeight);
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
        for (int i = 0; i < projectileX.size(); i++) {
            projectileX.set(i, projectileX.get(i) + projectileVX.get(i));
            projectileY.set(i, projectileY.get(i) + projectileVY.get(i));
        }
        if ()
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
                    /*case 'c':
                        projectileVX.add(bulletMaxMS);
                        projectileVY.add(bulletMaxMS);
                    case 'v':
                        projectileVX.add(-bulletMaxMS);
                        projectileVY.add(bulletMaxMS);
                    case 'b':
                        projectileVX.add(bulletMaxMS);
                        projectileVY.add(-bulletMaxMS);
                    case 'n':
                        projectileVX.add(-bulletMaxMS);
                        projectileVY.add(-bulletMaxMS);*/
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