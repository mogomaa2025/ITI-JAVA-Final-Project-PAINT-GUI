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