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
