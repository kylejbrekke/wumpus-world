import javax.swing.*;

public class Main {

    public static void main(String[] args) {
        Board board = new Board(10, 600, 0.06, 0.08, 0.04, false);
        JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(board);
        frame.pack();
        frame.setVisible(true);
    }
}
