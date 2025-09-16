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
