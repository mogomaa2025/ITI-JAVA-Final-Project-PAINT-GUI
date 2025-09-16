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
