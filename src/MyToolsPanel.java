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
