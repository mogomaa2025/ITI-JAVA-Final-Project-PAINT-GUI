This file is a merged representation of the entire codebase, combined into a single document by Repomix.
The content has been processed where security check has been disabled.

# File Summary

## Purpose
This file contains a packed representation of the entire repository's contents.
It is designed to be easily consumable by AI systems for analysis, code review,
or other automated processes.

## File Format
The content is organized as follows:
1. This summary section
2. Repository information
3. Directory structure
4. Repository files (if enabled)
5. Multiple file entries, each consisting of:
  a. A header with the file path (## File: path/to/file)
  b. The full contents of the file in a code block

## Usage Guidelines
- This file should be treated as read-only. Any changes should be made to the
  original repository files, not this packed version.
- When processing this file, use the file path to distinguish
  between different files in the repository.
- Be aware that this file may contain sensitive information. Handle it with
  the same level of security as you would the original repository.

## Notes
- Some files may have been excluded based on .gitignore rules and Repomix's configuration
- Binary files are not included in this packed representation. Please refer to the Repository Structure section for a complete list of file paths, including binary files
- Files matching patterns in .gitignore are excluded
- Files matching default ignore patterns are excluded
- Security check has been disabled - content may contain sensitive information
- Files are sorted by Git change count (files with more changes are at the bottom)

# Directory Structure
```
src/
  CanvasPanel.java
  ClearButton.java
  ColorChooserDialog.java
  EraserButton.java
  FillCheckBox.java
  FreeDrawButton.java
  LineButton.java
  Main.java
  Modes.java
  MyToolsPanel.java
  OpenButton.java
  OvalButton.java
  RectButton.java
  SaveButton.java
  Shape.java
  UndoButton.java
JavaProject.iml
README.md
```

# Files

## File: src/CanvasPanel.java
```java
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
```

## File: src/ClearButton.java
```java
import javax.swing.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class ClearButton extends JButton implements MouseListener {

    CanvasPanel canvasPanel;
    ClearButton(CanvasPanel canvasPanel){
        this.canvasPanel = canvasPanel;
        setContentAreaFilled(true);
        setBounds(20,380,80,20);
        setText("Clear");
        addMouseListener(this);
    }


    @Override
    public void mouseClicked(MouseEvent e) {

    }

    @Override
    public void mousePressed(MouseEvent e) {
        canvasPanel.clear();
    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }

}
```

## File: src/ColorChooserDialog.java
```java
import javax.swing.*;
import java.awt.*;
public class ColorChooserDialog extends JPanel {
    CanvasPanel canvasPanel;
    ColorChooserDialog(CanvasPanel canvasPanel){
        this.canvasPanel = canvasPanel;
        JPanel panel = new JPanel();
        JButton btn = new JButton("Choose Color");

        btn.addActionListener(e -> {
            Color selectedColor = JColorChooser.showDialog(
                    ColorChooserDialog.this,
                    "Pick a Color",
                    panel.getBackground());

            if (selectedColor != null) {
                panel.setBackground(selectedColor);
                canvasPanel.changeColor(selectedColor);
            }
        });

        panel.add(btn);
        add(panel);
        setVisible(true);
    }
}
```

## File: src/EraserButton.java
```java
import javax.swing.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class EraserButton extends JButton implements MouseListener {

    CanvasPanel canvasPanel;
    EraserButton(CanvasPanel canvasPanel){
        this.canvasPanel = canvasPanel;
        setContentAreaFilled(true);
        setBounds(20,420,80,20);
        setText("Erase");
        addMouseListener(this);
    }


    @Override
    public void mouseClicked(MouseEvent e) {

    }

    @Override
    public void mousePressed(MouseEvent e) {
        canvasPanel.changeMode(Modes.ERASER);
    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }

}
```

## File: src/FillCheckBox.java
```java
import javax.swing.*;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;


public class FillCheckBox extends JCheckBox implements ItemListener {

    CanvasPanel canvasPanel;
    FillCheckBox(CanvasPanel canvasPanel){
        this.canvasPanel = canvasPanel;
        setContentAreaFilled(true);
        setBounds(20,450,150,20);
        setText("Fill the next shapes");
        addItemListener(this);
    }


    @Override
    public void itemStateChanged(ItemEvent e) {
        canvasPanel.changeFill(this.isSelected());
    }
}
```

## File: src/FreeDrawButton.java
```java
import javax.swing.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class FreeDrawButton extends JButton implements MouseListener {

    CanvasPanel canvasPanel;
    FreeDrawButton(CanvasPanel canvasPanel){
        this.canvasPanel = canvasPanel;
        setContentAreaFilled(true);
        setBounds(20,180,100,30);
        setText("FreeDraw");
        addMouseListener(this);
    }


    @Override
    public void mouseClicked(MouseEvent e) {

    }

    @Override
    public void mousePressed(MouseEvent e) {
        canvasPanel.changeMode(Modes.FREE);
    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }

}
```

## File: src/LineButton.java
```java
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class LineButton extends JButton implements MouseListener {

    CanvasPanel canvasPanel;
    LineButton(CanvasPanel canvasPanel){
        this.canvasPanel = canvasPanel;
        setContentAreaFilled(true);
        setBounds(20,120,40,40);
        addMouseListener(this);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        g.setColor(Color.RED);
        g.drawLine(5, 5, 35, 35);
    }


    @Override
    public void mouseClicked(MouseEvent e) {

    }

    @Override
    public void mousePressed(MouseEvent e) {
        canvasPanel.changeMode(Modes.LINE);
    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }
}
```

## File: src/Main.java
```java
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
```

## File: src/Modes.java
```java
public enum Modes {
    RECTANGLE,OVAL,LINE,FREE,DEFAULT,ERASER
}
```

## File: src/MyToolsPanel.java
```java
import javax.swing.*;
import java.awt.*;

class MyToolsPanel extends JPanel {



    public MyToolsPanel(CanvasPanel canvasPanel) {
        setLayout(null);
        setBounds(0,0,200,800);
        setFocusable(true);
        add(new LineButton(canvasPanel));
        add(new OvalButton(canvasPanel));
        add(new RectButton(canvasPanel));

        ColorChooserDialog colorChooserDialog = new ColorChooserDialog(canvasPanel);
        colorChooserDialog.setBounds(20,280,120,60);
        add(colorChooserDialog);
        add(new ClearButton(canvasPanel));
        add(new FreeDrawButton(canvasPanel));
        add(new EraserButton(canvasPanel));
        add(new FillCheckBox(canvasPanel));
        add(new UndoButton(canvasPanel));
        add(new SaveButton(canvasPanel));
        add(new OpenButton(canvasPanel));
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        g.setColor(Color.black);
        g.drawLine(200,0,200,1000);

        //Shapes text
        g.setFont(new Font("Serif",Font.BOLD,30));
        g.drawString("Shapes",20,100);

        //Colors text
        g.setFont(new Font("Serif",Font.BOLD,30));
        g.drawString("Colors",20,260);

        //Clear text
        g.setFont(new Font("Serif",Font.BOLD,30));
        g.drawString("Clear",20,360);

        //File Operations text
        g.setFont(new Font("Serif",Font.BOLD,30));
        g.drawString("File",20,500);









    }
}
```

## File: src/OpenButton.java
```java
import javax.swing.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;

public class OpenButton extends JButton implements MouseListener {
    CanvasPanel canvasPanel;
    BufferedImage loadedImage;

    OpenButton(CanvasPanel canvasPanel) {
        this.canvasPanel = canvasPanel;
        setContentAreaFilled(true);
        setBounds(20, 560, 80, 20);
        setText("Open");
        addMouseListener(this);
        setToolTipText("Open an image");
    }

    @Override
    public void mouseClicked(MouseEvent e) {
    }

    @Override
    public void mousePressed(MouseEvent e) {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        fileChooser.setDialogTitle("Open Image");
        fileChooser.setFileFilter(new javax.swing.filechooser.FileFilter() {
            public boolean accept(File f) {
                String name = f.getName().toLowerCase();
                return name.endsWith(".png") || name.endsWith(".jpg") ||
                       name.endsWith(".jpeg") || f.isDirectory();
            }
            public String getDescription() {
                return "Image Files (*.png, *.jpg, *.jpeg)";
            }
        });

        int result = fileChooser.showOpenDialog(canvasPanel);
        if (result == JFileChooser.APPROVE_OPTION) {
            try {
                File selectedFile = fileChooser.getSelectedFile();
                loadedImage = ImageIO.read(selectedFile);
                canvasPanel.setLoadedImage(loadedImage);
                canvasPanel.repaint();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(canvasPanel,
                    "Error opening image: " + ex.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {
    }

    @Override
    public void mouseEntered(MouseEvent e) {
    }

    @Override
    public void mouseExited(MouseEvent e) {
    }
}
```

## File: src/OvalButton.java
```java
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class OvalButton extends JButton implements MouseListener {

    CanvasPanel canvasPanel;
    OvalButton(CanvasPanel canvasPanel){
        this.canvasPanel = canvasPanel;
        setContentAreaFilled(true);
        setBounds(70,120,40,40);
        setFocusable(true);
        setEnabled(true);

        addMouseListener(this);

    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        g.setColor(Color.RED);
        g.fillOval(3, 3, 35, 35);


    }


    @Override
    public void mouseClicked(MouseEvent e) {

    }

    @Override
    public void mousePressed(MouseEvent e) {
        canvasPanel.changeMode(Modes.OVAL);
    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }


}
```

## File: src/RectButton.java
```java
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class RectButton extends JButton implements MouseListener {

    CanvasPanel canvasPanel;
    RectButton(CanvasPanel canvasPanel){
        this.canvasPanel = canvasPanel;
        setContentAreaFilled(true);
        setBounds(120,120,40,40);
        addMouseListener(this);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        g.setColor(Color.RED);
        g.fillRect(5, 5, 30, 30);
    }


    @Override
    public void mouseClicked(MouseEvent e) {

    }

    @Override
    public void mousePressed(MouseEvent e) {
        canvasPanel.changeMode(Modes.RECTANGLE);
    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }

}
```

## File: src/SaveButton.java
```java
import javax.swing.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;

public class SaveButton extends JButton implements MouseListener {
    CanvasPanel canvasPanel;

    SaveButton(CanvasPanel canvasPanel) {
        this.canvasPanel = canvasPanel;
        setContentAreaFilled(true);
        setBounds(20, 520, 80, 20);
        setText("Save");
        addMouseListener(this);
        setToolTipText("Save drawing as image");
    }

    @Override
    public void mouseClicked(MouseEvent e) {
    }

    @Override
    public void mousePressed(MouseEvent e) {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        fileChooser.setDialogTitle("Save Drawing");
        fileChooser.setFileFilter(new javax.swing.filechooser.FileFilter() {
            public boolean accept(File f) {
                return f.getName().toLowerCase().endsWith(".png") || f.isDirectory();
            }
            public String getDescription() {
                return "PNG Images (*.png)";
            }
        });

        int result = fileChooser.showSaveDialog(canvasPanel);
        if (result == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();
            String filePath = selectedFile.getAbsolutePath();
            if (!filePath.toLowerCase().endsWith(".png")) {
                filePath += ".png";
            }

            try {
                BufferedImage image = new BufferedImage(canvasPanel.getWidth(),
                    canvasPanel.getHeight(), BufferedImage.TYPE_INT_ARGB);
                canvasPanel.paint(image.getGraphics());
                ImageIO.write(image, "PNG", new File(filePath));
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(canvasPanel,
                    "Error saving image: " + ex.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {
    }

    @Override
    public void mouseEntered(MouseEvent e) {
    }

    @Override
    public void mouseExited(MouseEvent e) {
    }
}
```

## File: src/Shape.java
```java
import java.awt.*;

public class Shape {

    Modes myMode;
    Color shapeColor;

    boolean filled;

    int x1;
    int y1;
    int x2;
    int y2;

    public Shape(int x1, int y1, int x2, int y2,Modes myMode,Color shapeColor,boolean filled) {
        this.filled = filled;
        this.myMode = myMode;
        this.shapeColor = shapeColor;
        this.x1 = x1;
        this.y1 = y1;
        this.x2 = x2;
        this.y2 = y2;
    }
}
```

## File: src/UndoButton.java
```java
import javax.swing.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class UndoButton extends JButton implements MouseListener {

    CanvasPanel canvasPanel;
    UndoButton(CanvasPanel canvasPanel){
        this.canvasPanel = canvasPanel;
        setContentAreaFilled(true);
        setBounds(20,480,80,20);
        setText("Undo");
        addMouseListener(this);
        setToolTipText("Undo last action");
    }

    @Override
    public void mouseClicked(MouseEvent e) {
    }

    @Override
    public void mousePressed(MouseEvent e) {
        if (canvasPanel.myData.isEmpty()) {
            setEnabled(false);
            Timer timer = new Timer(500, evt -> setEnabled(true));
            timer.setRepeats(false);
            timer.start();
        }
        canvasPanel.undo();
    }

    @Override
    public void mouseReleased(MouseEvent e) {
    }

    @Override
    public void mouseEntered(MouseEvent e) {
    }

    @Override
    public void mouseExited(MouseEvent e) {
    }

}
```

## File: JavaProject.iml
```
<?xml version="1.0" encoding="UTF-8"?>
<module type="JAVA_MODULE" version="4">
  <component name="NewModuleRootManager" inherit-compiler-output="true">
    <exclude-output />
    <content url="file://$MODULE_DIR$">
      <sourceFolder url="file://$MODULE_DIR$/src" isTestSource="false" />
    </content>
    <orderEntry type="inheritedJdk" />
    <orderEntry type="sourceFolder" forTests="false" />
  </component>
</module>
```

## File: README.md
```markdown
"# ITI-JAVA-Final-Project-PAINT-GUI"
```
