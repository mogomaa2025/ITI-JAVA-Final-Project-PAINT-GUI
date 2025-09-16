import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class CanvasPanel extends JPanel {

    Modes myMode = Modes.RECTANGLE;
    Color myColor = Color.black;

    boolean fill = false;
    int x1 = 0;
    int y1 = 0;
    int x2 = 0;
    int y2 = 0;
    boolean clicked = false;
    boolean finished = true;
    boolean addOrNot = false;
    ArrayList<Shape> myData = new ArrayList<>();
    ArrayList<Point> points = new ArrayList<>();
    ArrayList<Point> erase = new ArrayList<>();
    private BufferedImage loadedImage = null;
    private BufferedImage editableImage = null;

    CanvasPanel(){
        setBackground(Color.white);
        setBounds(200,0,800,800);



        addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {

            }

            @Override
            public void mousePressed(MouseEvent e) {

                if(myMode == Modes.FREE){
                    points.add(e.getPoint());
                }

                if(myMode == Modes.ERASER){
                    erase.add(e.getPoint());
                }

                x1 = e.getX();
                y1 = e.getY();
                clicked = false;
                finished = false;
                addOrNot = myMode == Modes.FREE;
                repaint();
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                if(myMode == Modes.ERASER) {
                    erase.clear();
                } else {
                    x2 = e.getX();
                    y2 = e.getY();
                    clicked = true;
                    addOrNot = true;

                    if(myMode == Modes.FREE && points.size() > 1) {
                        // Add all free draw points to shapes before clearing
                        for (int i = 1; i < points.size(); i++) {
                            Point p1 = points.get(i - 1);
                            Point p2 = points.get(i);
                            myData.add(new Shape(p1.x, p1.y, p2.x, p2.y, Modes.FREE, myColor, true));
                        }
                        points.clear();
                    }
                }
                repaint();
            }

            @Override
            public void mouseEntered(MouseEvent e) {

            }

            @Override
            public void mouseExited(MouseEvent e) {

            }


        });

        addMouseMotionListener(new MouseMotionListener() {
            @Override
            public void mouseDragged(MouseEvent e) {

                if(myMode == Modes.FREE){
                    points.add(e.getPoint());
                }

                if(myMode == Modes.ERASER){
                    Point newPoint = e.getPoint();
                    if (!erase.isEmpty()) {
                        Point lastPoint = erase.get(erase.size() - 1);
                        Graphics2D g2 = (Graphics2D) editableImage.getGraphics();
                        g2.setColor(getBackground());
                        g2.setStroke(new BasicStroke(20, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
                        g2.drawLine(lastPoint.x, lastPoint.y, newPoint.x, newPoint.y);
                        g2.dispose();
                    }
                    erase.add(newPoint);
                }
                x2 = e.getX();
                y2 = e.getY();
                clicked = true;
                repaint();
            }

            @Override
            public void mouseMoved(MouseEvent e) {

            }
        });
    }

    void changeFill(boolean fill){
        this.fill = fill;
    }

    void undo(){
        if (!myData.isEmpty()) {
            myData.removeLast();
            myMode = Modes.DEFAULT;
            repaint();
        }
    }

    void changeMode(Modes changedMode){
        myMode = changedMode;
        if(changedMode == Modes.ERASER){
            erase.clear();
        }
    }

    void changeColor(Color color){
        myColor = color;
    }

    void clear(){
        myData.clear();
        points.clear();
        erase.clear();
        if (editableImage != null) {
            Graphics2D g2d = editableImage.createGraphics();
            g2d.setBackground(getBackground());
            g2d.clearRect(0, 0, getWidth(), getHeight());
            g2d.dispose();
        }
        myMode = Modes.DEFAULT;
        clicked = false;
        repaint();
    }

    void setLoadedImage(BufferedImage image) {
        this.loadedImage = image;
        // Create an editable copy of the image
        editableImage = new BufferedImage(getWidth(), getHeight(), BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = editableImage.createGraphics();
        g2d.setBackground(getBackground());
        g2d.clearRect(0, 0, getWidth(), getHeight());
        g2d.drawImage(image, 0, 0, getWidth(), getHeight(), null);
        g2d.dispose();
        repaint();
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);

        // Draw the editable image instead of the original loaded image
        if (editableImage != null) {
            g.drawImage(editableImage, 0, 0, this.getWidth(), this.getHeight(), null);
        }

        // Draw all saved shapes
        for (Shape l:myData) {
            g.setColor(l.shapeColor);
            if(l.filled){
                switch (l.myMode){
                    case RECTANGLE -> g.fillRect(l.x1,l.y1,l.x2,l.y2);
                    case OVAL,ERASER -> g.fillOval(l.x1,l.y1,l.x2,l.y2);
                    case FREE ,LINE -> g.drawLine(l.x1,l.y1,l.x2,l.y2);
                    case DEFAULT -> {}
                }
            }else{
                switch (l.myMode){
                    case RECTANGLE -> g.drawRect(l.x1,l.y1,l.x2,l.y2);
                    case OVAL -> g.drawOval(l.x1,l.y1,l.x2,l.y2);
                    case ERASER -> g.fillOval(l.x1,l.y1,l.x2,l.y2);
                    case FREE ,LINE -> g.drawLine(l.x1,l.y1,l.x2,l.y2);
                    case DEFAULT -> {}
                }
            }
        }

        // Draw current shape being drawn
        if(clicked){
            g.setColor(myColor);
            if(finished){
                x2 = x1;
                y2 = y1;
            }

            switch (myMode) {
                case LINE -> {
                    g.drawLine(x1, y1, x2, y2);
                    if (addOrNot) {
                        myData.add(new Shape(x1, y1, x2, y2, Modes.LINE,myColor,true));
                    }
                }
                case OVAL -> {
                    int w = x2 - x1;
                    int h = y2 - y1;
                    if(fill){
                        g.fillOval(x1, y1, w, h);
                    }else{
                        g.drawOval(x1, y1, w, h);
                    }

                    if (addOrNot) {
                        myData.add(new Shape(x1, y1, w, h, Modes.OVAL,myColor,fill));
                    }
                }
                case RECTANGLE -> {
                    int w_r = x2 - x1;
                    int h_r = y2 - y1;
                    if(fill){
                        g.fillRect(x1, y1, w_r, h_r);
                    }else {
                        g.drawRect(x1, y1, w_r, h_r);
                    }

                    if (addOrNot) {
                        myData.add(new Shape(x1, y1, w_r, h_r, Modes.RECTANGLE,myColor,fill));
                    }
                }
                case FREE -> {
                    // Only draw the current free-draw line segments
                    for (int i = 1; i < points.size(); i++) {
                        Point p1 = points.get(i - 1);
                        Point p2 = points.get(i);
                        g.drawLine(p1.x, p1.y, p2.x, p2.y);
                    }
                }
                case ERASER -> {
                    Graphics2D g2 = (Graphics2D) editableImage.getGraphics();
                    g2.setColor(getBackground());
                    g2.setStroke(new BasicStroke(20, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
                    for (int i = 1; i < erase.size(); i++) {
                        Point p1 = erase.get(i - 1);
                        Point p2 = erase.get(i);
                        g2.drawLine(p1.x, p1.y, p2.x, p2.y);
                    }
                    g2.dispose();
                }

                case DEFAULT -> {}
            }
        }
    }
}