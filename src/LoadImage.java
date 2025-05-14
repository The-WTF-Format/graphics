import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.nio.file.Path;

public class LoadImage {
    JMenuBar menu;
    JMenu loadImage;
    JPanel panel;
    JPanel createImage;
    ImagePanel imagePanel;
    BufferedImage image;
    JTextField text = new JTextField();
    LoadImage(JMenuBar menu, JPanel panel) {
        this.menu = menu;
        this.panel = panel;
        addMenu();
    }
    private void addMenu() {
        loadImage = new JMenu("Load image");
        loadImage.setPreferredSize(Size.buttonSizeMainMenu);
        loadImage.setMinimumSize(Size.buttonSizeMainMenu);
        loadImage.setMaximumSize(Size.buttonSizeMainMenu);
        menu.add(loadImage);
        addMenuItems();
    }
    private void addMenuItems() {
        addPathItem();
        addCreateItem();
    }
    private void addCreateItem() {
        JMenuItem createNewImage = new JMenuItem("create new image");
        loadImage.add(createNewImage);
        createNewImage.setBackground(Colors.itemsMain);
        createNewImage.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // todo alles entfernen, was an dieser Position ist
                if(imagePanel!= null) {
                    // sollte so funktionieren, da ein dargestelltes Bild ua. genau in der Mitte vorhanden sein sollte
                    panel.remove(imagePanel);
                }

                //-----
                /*
                //todo ersetzten mit ... und mehr
                WtfImageBuilder builder = by();
                createNewImage(builder);
                panel.remove(createImage);
                //todo neue Items hinzufügen um die ganzen Angaben angeben zu können - z.B. Buttons, Sliders ec.
                //todo mit den Methoden neues Bild kreieren
                */
                //probeweise ohne WtfImageBuilder parameter
                //-----
                createNewImage();
                saveImage();
                panel.revalidate();
                panel.repaint();
            }
        });
    }
    private void addPathItem() {
        JMenuItem byPath = new JMenuItem("by path");
        loadImage.add(byPath);
        byPath.setBackground(Colors.itemsMain);
        byPath.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // todo Entweder diese Art Bilder hochladen als 3. Variante behalten oder diesen Teil löschen und nur mit WtfLoader arbeiten

                // todo alles entfernen, was an dieser Position ist
                if(imagePanel != null) {
                    panel.remove(imagePanel);
                }
                getNewImage();

                //-----
                //todo ersetzten mit ... und mehr
                //from(getNewPath());
                //-----

                imagePanel = new ImagePanel(image);
                panel.add(imagePanel, BorderLayout.CENTER);
                saveImage();
                panel.revalidate();
                panel.repaint();
            }
        });
    }
    private void getNewImage() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Bild auswählen");
        fileChooser.setFileFilter(new FileNameExtensionFilter("jpg", "png", "jpeg"));

        int result = fileChooser.showOpenDialog(panel);
        //Zustand: ob der Benutzer eine Datei geöffnet hat, oder abgebrochen hat oder ein Fehler aufgetreten ist
        if(result == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();
            try{
                image = ImageIO.read(selectedFile);
            } catch(Exception ex) {
                System.out.println("Error when reading Image: " + ex);
            }
        }

    }
    private Path getNewPath() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Bild auswählen");
        fileChooser.setFileFilter(new FileNameExtensionFilter("jpg", "png", "jpeg"));

        int result = fileChooser.showOpenDialog(panel);
        //Zustand: ob der Benutzer eine Datei geöffnet hat, oder abgebrochen hat oder ein Fehler aufgetreten ist
        if(result == JFileChooser.APPROVE_OPTION) {
            return fileChooser.getSelectedFile().toPath();
        }
        return null;
    }
    private void saveImage() {
        JButton saveButton = new JButton("save Image");
        saveButton.setPreferredSize(Size.buttonSizeMainMenu);
        saveButton.setMinimumSize(Size.buttonSizeMainMenu);
        saveButton.setMaximumSize(Size.buttonSizeMainMenu);
        saveButton.setBackground(Colors.itemsMain);
        panel.add(saveButton, BorderLayout.SOUTH);
        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // todo save the image
            }
        });
    }
    private void createNewImage() {
        createImage = new JPanel();
        createImage.setLayout(new BoxLayout(createImage, BoxLayout.X_AXIS));
        panel.add(createImage, BorderLayout.CENTER);
        createImage.add(Box.createHorizontalStrut(50));
        //0.
        JLabel label = new JLabel("Please enter all attributes:");
        createImage.add(label);
        createImage.add(Box.createHorizontalStrut(150));
        //1.
        JSpinner width = new JSpinner(new SpinnerNumberModel(1, 1, 65535, 1));
        width.setPreferredSize(new Dimension(200, 200));
        width.setMaximumSize(new Dimension(200, 200));
        width.setMinimumSize(new Dimension(200, 200));
        width.setName("Width");
        createImage.add(width);
        createImage.add(Box.createHorizontalStrut(150));
        //2.
        JButton accept = new JButton("accept");
        createImage.add(accept);
        createImage.add(Box.createHorizontalStrut(50));
        // todo usw.
        // todo konzept wie Attributes "abarbeiten" fehlt
    }

}
