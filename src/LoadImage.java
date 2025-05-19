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
    JButton saveButton;
    JMenuBar menu;
    JMenu loadImage;
    CreatePanel panel;
    CreatePanel createImage;
    ImagePanel imagePanel;
    BufferedImage image;
    JMenuItem createNewImage;
    JMenuItem byPath;
    EditViewButton editViewButton;
    LoadImage(JMenuBar menu, CreatePanel panel, EditViewButton editViewButton) {
        this.menu = menu;
        this.panel = panel;
        this.editViewButton = editViewButton;
        addMenu();
    }
    private void addMenu() {
        loadImage = new JMenu("Load image");
        loadImage.setPreferredSize(Size.BUTTONSIZEMAINMENU);
        loadImage.setMinimumSize(Size.BUTTONSIZEMAINMENU);
        loadImage.setMaximumSize(Size.BUTTONSIZEMAINMENU);
        menu.add(loadImage);
        addMenuItems();
        saveImage();
    }
    private void addMenuItems() {
        addPathItem();
        addCreateItem();
    }
    private void addCreateItem() {
        createNewImage = new JMenuItem("create new image");
        loadImage.add(createNewImage);
        createNewImage.setBackground(Colors.ITEMSPRIMARY);
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
                Visible.setInvisible(byPath, createNewImage, saveButton, editViewButton.getEditor(), editViewButton.getViewer());
                createNewImage();
                panel.revalidate();
                panel.repaint();
            }
        });
    }
    private void addPathItem() {
        byPath = new JMenuItem("by path");
        loadImage.add(byPath);
        byPath.setBackground(Colors.ITEMSPRIMARY);
        byPath.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // todo Entweder diese Art Bilder hochladen als 3. Variante behalten oder diesen Teil löschen und nur mit WtfLoader arbeiten

                // todo alles entfernen, was an dieser Position ist
                if(imagePanel != null) {
                    panel.remove(imagePanel);
                }
                Visible.setInvisible(editViewButton.getViewer(), editViewButton.getEditor());
                getNewImage();

                //-----
                //todo ersetzten mit ... und mehr
                //from(getNewPath());
                //-----
                if(editViewButton.isEditorVisible()) {
                    Visible.setVisible(saveButton, editViewButton.getViewer());
                } else {
                    Visible.setVisible(saveButton, editViewButton.getEditor());
                }
                imagePanel = new ImagePanel(image);
                panel.add(imagePanel, BorderLayout.CENTER);
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
        saveButton = new JButton("save Image");
        saveButton.setPreferredSize(Size.BUTTONSIZEMAINMENU);
        saveButton.setMinimumSize(Size.BUTTONSIZEMAINMENU);
        saveButton.setMaximumSize(Size.BUTTONSIZEMAINMENU);
        saveButton.setBackground(Colors.ITEMSPRIMARY);
        panel.add(saveButton, BorderLayout.SOUTH);
        Visible.setInvisible(saveButton);
        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // todo save the image
            }
        });
    }
    private void createNewImage() {
        //todo in getrennte KLasse auslagern
        createImage = new CreatePanel();
        createImage.setLayout(new BoxLayout(createImage, BoxLayout.X_AXIS));
        panel.add(createImage, BorderLayout.CENTER);
        createImage.add(Box.createHorizontalStrut(50));
        //0.
        JLabel label = new JLabel("Please enter all attributes:");
        createImage.add(label);
        createImage.add(Box.createHorizontalStrut(100));
        //1.
        JSpinner width = new JSpinner(new SpinnerNumberModel(1, 1, 65535, 1));
        width.setPreferredSize(new Dimension(200, 200));
        width.setMaximumSize(new Dimension(200, 200));
        width.setMinimumSize(new Dimension(200, 200));
        width.setName("Width");
        createImage.add(width);
        createImage.add(Box.createHorizontalStrut(100));
        //2.
        JButton next = new JButton("next");
        createImage.add(next);
        createImage.add(Box.createHorizontalStrut(100));
        JButton cancel = new JButton("cancel");
        createImage.add(cancel);
        createImage.add(Box.createHorizontalStrut(50));
        cancel.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                panel.remove(createImage);
                if(editViewButton.isEditorVisible()) {
                    Visible.setVisible(byPath, createNewImage, saveButton, editViewButton.getViewer());
                } else {
                    Visible.setVisible(byPath, createNewImage, saveButton, editViewButton.getEditor());
                }
                panel.revalidate();
                panel.repaint();
            }
        });

        // todo usw.
        // todo Konzept wie Attributes "abarbeiten" fehlt
    }

}
