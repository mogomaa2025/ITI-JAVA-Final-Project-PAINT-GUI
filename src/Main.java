import javax.swing.*;
import java.awt.*;

public class Main {
    public static void main(String[] args) {
        JFrame jFrame = new JFrame();
        jFrame.setSize(1000,800);
        jFrame.setTitle("Java Final Project");
        jFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        jFrame.setFocusable(true);
        jFrame.setLayout(new BorderLayout());
        CanvasPanel canvasPanel = new CanvasPanel();
        jFrame.add(canvasPanel);
        jFrame.add(new MyToolsPanel(canvasPanel));
        jFrame.setVisible(true);
    }
}