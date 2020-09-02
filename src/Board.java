import javax.swing.*;
import java.awt.*;
import java.util.Random;

public class Board extends JPanel {

    private Agent agent;
    private Tile[][] tiles;
    private boolean slow;
    private int size;
    private int windowSize;
    private int wumpusCount;

    public Board(int size, int windowSize, double pitProbability, double obstacleProbability, double wumpusProbability, boolean slow) {
        super();
        this.slow = slow;
        this.size = size;
        this.windowSize = windowSize;
        tiles = new Tile[size][size];
        generateBoard(pitProbability, obstacleProbability, wumpusProbability);

        setSize(windowSize, windowSize);
        setDoubleBuffered(true);
        setPreferredSize(new Dimension(windowSize, windowSize));
    }

    private void generateBoard(double pitProbability, double obstacleProbability, double wumpusProbability) {
        for (int i = 0; i < size; i++) {
            Tile[] column = new Tile[size];
            for (int j = 0; j < size; j++) {
                column[j] = new Tile();
            }
            tiles[i] = column;
        }

        for (int x = 0; x < size; x++) {
            for (int y = 0; y < size; y++) {
                if (roll(pitProbability)) {
                    setTileTraitAtCoordinate("pit", x, y);
                } else if (roll(obstacleProbability)) {
                    setTileTraitAtCoordinate("obstacle", x, y);
                } else if (roll(wumpusProbability)) {
                    setTileTraitAtCoordinate("wumpus", x, y);
                }
            }
        }

        boolean goldPlaced = false;
        boolean agentPlaced = false;
        Random random = new Random();
        do {
            int x = random.nextInt(size);
            int y = random.nextInt(size);
            if (agentPlaced) {
                goldPlaced = setTileTraitAtCoordinate("gold", x, y);
            } else {
                agentPlaced = setTileTraitAtCoordinate("agent", x, y);
            }
        } while (!goldPlaced);
    }

    private boolean roll(double probability) {
        Random random = new Random();
        double result = random.nextDouble();
        return result <= probability;
    }

    private boolean setTileTraitAtCoordinate(String trait, int x, int y) {
        if (x >= 0 && x < size && y >= 0 && y < size) {
            System.out.println(trait);
            switch (trait) {
                case "pit":
                    tiles[x][y].digPit();
                    setTileTraitAtCoordinate("breeze", x - 1, y);
                    setTileTraitAtCoordinate("breeze", x + 1, y);
                    setTileTraitAtCoordinate("breeze", x, y - 1);
                    setTileTraitAtCoordinate("breeze", x, y + 1);
                    return true;
                case "wumpus":
                    wumpusCount++;
                    tiles[x][y].birthWumpus();
                    setTileTraitAtCoordinate("stench", x - 1, y);
                    setTileTraitAtCoordinate("stench", x + 1, y);
                    setTileTraitAtCoordinate("stench", x, y - 1);
                    setTileTraitAtCoordinate("stench", x, y + 1);
                    return true;
                case "obstacle":
                    tiles[x][y].buildObstacle();
                    return true;
                case "breeze":
                    tiles[x][y].blowBreeze();
                    return true;
                case "stench":
                    tiles[x][y].waftStench();
                    return true;
                case "agent":
                    if (tiles[x][y].isEmpty()) {
                        agent = new Agent(x, y, wumpusCount, size, tiles);
                        return true;
                    } else {
                        return false;
                    }
                case "gold":
                    if (tiles[x][y].isClear()) {
                        tiles[x][y].dropGold();
                        return true;
                    } else {
                        return false;
                    }
                default:
                    System.out.println(trait + " does not exist in the Wumpus world!");
                    return false;
            }
        }
        return false;
    }

    private void paintBoard(Graphics2D graphics) {
        graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
        int stepSize = windowSize / size;
        for (int x = 0; x < size; x++) {
            for (int y = 0; y < size; y++) {
                tiles[x][y].draw(x * stepSize, y * stepSize, stepSize, graphics);
            }
        }
        agent.draw(stepSize, graphics);
    }

    public void paint(Graphics graphics) {
        paintBoard((Graphics2D) graphics);
    }

}
