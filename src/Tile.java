import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class Tile {

    private boolean obstacle = false;
    private boolean pit = false;
    private boolean wumpus = false;
    private boolean stench = false;
    private boolean breeze = false;
    private boolean gold = false;

    public Tile() { }

    public void buildObstacle() {
        obstacle = true;
    }

    public void blowBreeze() {
        breeze = true;
    }

    public void digPit() {
        pit = true;
    }

    public void birthWumpus() {
        wumpus = true;
    }

    public void killWumpus() { wumpus = false; }

    public void waftStench() {
        stench = true;
    }

    public void dissipateStench() { stench = false; }

    public void dropGold() {
        gold = true;
    }

    public boolean isObstacle() {
        return obstacle;
    }

    public boolean isPit() {
        return pit;
    }

    public boolean hasWumpus() {
        return wumpus;
    }

    public boolean hasStench() {
        return stench;
    }

    public boolean hasBreeze() {
        return breeze;
    }

    public boolean isEmpty() {
        return !wumpus && !pit && !obstacle && !stench && !gold && !breeze;
    }

    public boolean isClear() {
        return !wumpus && !pit && !obstacle;
    }

    public void draw(int x, int y, int size, Graphics2D graphics) {
        Rectangle tile = new Rectangle(x, y, size, size);
        graphics.draw(tile);

        if(isEmpty()) {
            return;
        }

        if (breeze && isClear()) {
            drawImage("src/images/wind.png", x, y, size, graphics);
        }

        if (stench && isClear()) {
            drawImage("src/images/stench.png", x, y, size, graphics);
        }

        if (obstacle) {
            drawImage("src/images/obstacle.png", x, y, size, graphics);
        }

        if (wumpus) {
            drawImage("src/images/wumpus.png", x, y, size, graphics);
        }

        if (pit) {
            drawImage("src/images/pit.png", x, y, size, graphics);
        }

        if (gold) {
            drawImage("src/images/gold.png", x, y, size, graphics);
        }
    }

    private void drawImage(String path, int x, int y, int size, Graphics2D graphics) {
        try {
            BufferedImage img = ImageIO.read(new File(path));
            Image scaledImage = img.getScaledInstance(size, size, Image.SCALE_SMOOTH);
            graphics.drawImage(scaledImage, x, y, null);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
