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
