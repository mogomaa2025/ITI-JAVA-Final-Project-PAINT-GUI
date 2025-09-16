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
