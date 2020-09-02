import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Agent {

    private static final String UNKOWN = "unkown";
    private static final String WUMPUS = "wumpus";
    private static final String STENCH = "stench";
    private static final String PIT = "pit";
    private static final String BREEZE = "breeze";
    private static final String OBSTACLE = "obstacle";
    private static final String GOLD = "gold";

    private static final String NORTH = "north";
    private static final String EAST = "east";
    private static final String SOUTH = "south";
    private static final String WEST = "west";
    private static final List<String> ORIENTATIONS = Arrays.asList(NORTH, EAST, SOUTH, WEST);

    private int xPos;
    private int yPos;
    private boolean dead = false;
    private String orientation = NORTH;
    private int ammunition;
    private ArrayList<String>[][] knowledge;
    private int realitySize;
    private Tile[][] reality;

    public Agent(int xPosition, int yPosition, int ammunitionCount, int boardSize, Tile[][] board) {
        xPos = xPosition;
        yPos = yPosition;
        ammunition = ammunitionCount;
        realitySize = boardSize;
        reality = board;

        knowledge = new ArrayList[realitySize][realitySize];

        for (int i = 0; i < realitySize; i++) {
            Arrays.setAll(knowledge[i], element -> new ArrayList<String>());
            for (ArrayList<String> list : knowledge[i]) {
                list.add(UNKOWN);
            }
        }
    }

    private void turn(String direction) {
        int index = ORIENTATIONS.indexOf(orientation);
        if (direction.toLowerCase().equals("left")) {
            if (index == 0) {
                orientation = ORIENTATIONS.get(ORIENTATIONS.size() - 1);
            } else {
                orientation = ORIENTATIONS.get(index - 1);
            }
        } else if (direction.toLowerCase().equals("right")) {
            if (index == ORIENTATIONS.size() - 1) {
                orientation = ORIENTATIONS.get(0);
            } else {
                orientation = ORIENTATIONS.get(index + 1);
            }
        }
    }

    private void move() {
        int origX = xPos;
        int origY = yPos;
        switch (orientation) {
            case NORTH:
                yPos++;
                break;
            case EAST:
                xPos++;
                break;
            case SOUTH:
                yPos--;
                break;
            case WEST:
                xPos--;
                break;
            default:
                break;
        }

        if ((reality[xPos][yPos]).isObstacle()) {
            knowledge[xPos][yPos].remove(UNKOWN);
            knowledge[xPos][yPos].add(OBSTACLE);
            xPos = origX;
            yPos = origY;
        }
    }

    private boolean shoot() {
        ammunition--;
        int[] pos = getArrowTrajectory(xPos, yPos);
        return calculateHit(pos);
    }

    private int[] getArrowTrajectory(int arrowX, int arrowY) {
        switch (orientation) {
            case NORTH:
                return new int[]{arrowX, arrowY + 1};
            case EAST:
                return new int[]{arrowX + 1, arrowY};
            case SOUTH:
                return new int[]{arrowX, arrowY - 1};
            case WEST:
                return new int[]{arrowX - 1, arrowY};
        }
        return null; // if orientation is broken, return null
    }

    private boolean calculateHit(int[] pos) {
        if (pos != null) {
            boolean outOfBounds = getOutOfBounds(pos[0], pos[1]);
            if (outOfBounds || reality[pos[0]][pos[1]].isObstacle()) {
                return false;
            } else if (reality[pos[0]][pos[1]].hasWumpus()) {
                return true;
            } else {
                return updateArrow(pos);
            }
        }
        return false; // if position is null, return false
    }

    private boolean updateArrow(int[] pos) {
        pos = getArrowTrajectory(pos[0], pos[1]);
        return calculateHit(pos);
    }

    private boolean getOutOfBounds(int x, int y) {
        return x < 0 || x > realitySize || y < 0 || y > realitySize;
    }

    public void draw(int stepSize, Graphics2D graphics) {
        try {
            BufferedImage img = ImageIO.read(new File("src/images/agent.png"));
            Image scaledImage = img.getScaledInstance(stepSize, stepSize, Image.SCALE_SMOOTH);
            graphics.drawImage(scaledImage, xPos * stepSize, yPos * stepSize, null);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
