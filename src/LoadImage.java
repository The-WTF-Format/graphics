import wtf.file.api.WtfImage;
import wtf.file.api.WtfLoader;
import wtf.file.api.builder.WtfImageBuilder;
import wtf.file.api.color.ColorSpace;
import wtf.file.api.exception.WtfException;
import wtf.file.api.v1.impl.editable.EditableWtfImageImpl;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Objects;

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
    JMenuItem normalImage;
    EditViewButton editViewButton;
    ArrayList<JComponent> setVisibility;
    WtfImage wtfImage;
    WtfImageBuilder builder;
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
        getNormalImageByPath();
    }
    private void addCreateItem() {
        createNewImage = new JMenuItem("create new image");
        loadImage.add(createNewImage);
        createNewImage.setBackground(Colors.ITEMSPRIMARY);
        createNewImage.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(imagePanel!= null) {
                    // sollte so funktionieren, da ein dargestelltes Bild ua. genau in der Mitte vorhanden sein sollte
                    panel.remove(imagePanel);
                }
                builder = WtfLoader.by();
                Visible.setInvisible(byPath, createNewImage, saveButton, editViewButton.getEditor(), editViewButton.getViewer(), editViewButton.panelNorth.editorMenuBar);
                createNewImage(builder);
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
                if(imagePanel != null) {
                    panel.remove(imagePanel);
                }
                Visible.setInvisible(editViewButton.getViewer(), editViewButton.getEditor(), editViewButton.panelNorth.editorMenuBar);

                try {
                    wtfImage = WtfLoader.from(Objects.requireNonNull(getNewPath()));
                } catch (WtfException | IOException ex) {
                    System.out.println("This is an invalid Path");
                }


                if(editViewButton.isEditorVisible()) {
                    Visible.setVisible(saveButton, editViewButton.getViewer(), editViewButton.panelNorth.editorMenuBar);
                } else {
                    Visible.setVisible(saveButton, editViewButton.getEditor());
                }

                imagePanel = new ImagePanel(wtfImage.asJavaImage());
                panel.add(imagePanel, BorderLayout.CENTER);
                panel.revalidate();
                panel.repaint();
            }
        });
    }
    private void getNormalImageByPath() {
        normalImage = new JMenuItem("get normal Image");
        loadImage.add(normalImage);
        normalImage.setBackground(Colors.ITEMSPRIMARY);
        normalImage.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(imagePanel != null) {
                    panel.remove(imagePanel);
                }
                Visible.setInvisible(editViewButton.getViewer(), editViewButton.getEditor(), editViewButton.panelNorth.editorMenuBar);
                getNewImage();
                if(editViewButton.isEditorVisible()) {
                    Visible.setVisible(saveButton, editViewButton.getViewer(), editViewButton.panelNorth.editorMenuBar);
                } else {
                    Visible.setVisible(saveButton, editViewButton.getEditor());
                }
                imagePanel = new BufferedImagePanel(image);
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
    private void createNewImage(WtfImageBuilder builder) {
        new CreateNewImage(this, builder);
    }

    void onCreateNewImageDone(int valWidth, int valHeight, int valSecondsPerFrame, int valFramePerSeconds, int valFrames, int valChannelWidth, ColorSpace colorspace) {
        builder.width(valWidth);
        builder.height(valHeight);
        builder.secondsPerFrame(valSecondsPerFrame);
        builder.framesPerSecond(valFramePerSeconds);
        builder.frames(valFrames);
        builder.channelWidth(valChannelWidth);
        builder.colorSpace(colorspace);
        wtfImage = builder.build();

        imagePanel = new ImagePanel(wtfImage.asJavaImage());
        panel.add(imagePanel, BorderLayout.CENTER);
        panel.revalidate();
        panel.repaint();
        System.out.println("Im here but nothing happens");
        //todo Darstellen von WTFImage
    }
}
