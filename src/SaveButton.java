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
