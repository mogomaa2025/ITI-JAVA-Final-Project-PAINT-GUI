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
