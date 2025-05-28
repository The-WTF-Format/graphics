import image.BufferedImagePanel;
import image.ImagePanel;
import values.Colors;
import values.Size;
import wtf.file.api.WtfImage;
import wtf.file.api.WtfLoader;
import wtf.file.api.builder.WtfImageBuilder;
import wtf.file.api.color.ColorSpace;
import wtf.file.api.editable.EditableWtfImage;
import wtf.file.api.exception.WtfException;
import wtf.file.api.v1.impl.editable.EditableWtfImageImpl;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.event.MenuEvent;
import javax.swing.event.MenuListener;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

public class LoadImage {
    JButton saveButton;
    JMenuBar menu;
    JMenu loadImage;
    CreatePanel panel;
    ImagePanel imagePanel;
    BufferedImage image;
    JMenuItem createNewImage;
    JMenuItem byPath;
    JMenuItem normalImage;
    EditViewButton editViewButton;
    WtfImage wtfImage;
    WtfImageBuilder builder;
    EditableWtfImage editableWtfImage;
    LoadImage(JMenuBar menu, CreatePanel panel, EditViewButton editViewButton) {
        this.menu = menu;
        this.panel = panel;
        this.editViewButton = editViewButton;
        addMenu();
    }

    void setImagePanel(EditableWtfImage editableWtfImage) {
        this.imagePanel = new ImagePanel(editableWtfImage.asJavaImage());
    }
    void setEditableWtfImage(EditableWtfImage editableWtfImage) {
        this.editableWtfImage = editableWtfImage;
    }
    EditableWtfImage getEditableWtfImage() {
        return editableWtfImage;
    }
    ImagePanel getImagePanel() {
        return this.imagePanel;
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
                    if (unsavedWarning()) {
                        return;
                    }
                    panel.remove(imagePanel);
                }
                builder = WtfLoader.by();
                Visible.setInvisible(byPath, createNewImage, saveButton, editViewButton.getEditor(), editViewButton.getViewer(), editViewButton.panelNorth.editorMenuBar);
                createNewImage(builder);
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
                    if (unsavedWarning()) {
                        return;
                    }
                    panel.remove(imagePanel);
                }
                Visible.setInvisible(editViewButton.getViewer(), editViewButton.getEditor(), editViewButton.panelNorth.editorMenuBar);
                try {
                    wtfImage = WtfLoader.from(Objects.requireNonNull(getNewPath()));
                } catch (WtfException | IOException ex) {
                    System.out.println(ex);
                }

                if(editViewButton.isEditorVisible()) {
                    Visible.setVisible(saveButton, editViewButton.getViewer(), editViewButton.panelNorth.editorMenuBar);
                } else {
                    Visible.setVisible(saveButton, editViewButton.getEditor());
                }
                System.out.println("n Frames: " + wtfImage.animationInformation().frames());
                editableWtfImage = wtfImage.edit();
                try {
                    showImage();
                } catch (InterruptedException ex) {
                    throw new RuntimeException(ex);
                }

            }
        });
    }
    private void getNormalImageByPath() {
        normalImage = new JMenuItem("get standard format Image");
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
        fileChooser.setFileFilter(new FileNameExtensionFilter(null, "jpg", "png", "jpeg"));

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
        fileChooser.setFileFilter(new FileNameExtensionFilter(null, "wtf"));
        //TODO all possible formats

        int result = fileChooser.showOpenDialog(panel);
        //Zustand: ob der Benutzer eine Datei geöffnet hat, oder abgebrochen hat oder ein Fehler aufgetreten ist
        if(result == JFileChooser.APPROVE_OPTION) {
            return fileChooser.getSelectedFile().toPath();
        }
        return null;
    }
    private Path getSavingPath() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Pfad auswählen");
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
                if(editableWtfImage == null) {
                    editableWtfImage = wtfImage.edit();
                }
                try {
                    editableWtfImage.save(getSavingPath());
                } catch (IOException | WtfException ex) {
                    throw new RuntimeException(ex);
                }
                panel.remove(imagePanel);
                Visible.setInvisible(saveButton);
                imagePanel = null;
                wtfImage = null;
                editableWtfImage = null;
                panel.repaint();
                panel.revalidate();
            }
        });
    }
    private void createNewImage(WtfImageBuilder builder) {
        new CreateNewImage(this, builder);
    }

    void onCreateNewImageDone(int valWidth, int valHeight, int valSecondsPerFrame, int valFramePerSeconds, int valFrames, int valChannelWidth, ColorSpace colorspace) throws InterruptedException {
        if(valFrames == 1 ) {
            builder.width(valWidth).height(valHeight).frames(valFrames)
                    .channelWidth(valChannelWidth).colorSpace(colorspace);
            wtfImage = builder.build();
        } else if (valFramePerSeconds != 0 ) {
            builder.width(valWidth).height(valHeight).framesPerSecond(valFramePerSeconds)
                    .frames(valFrames).channelWidth(valChannelWidth).colorSpace(colorspace);
            wtfImage = builder.build();
        } else {
            builder.width(valWidth).height(valHeight).secondsPerFrame(valSecondsPerFrame)
                    .frames(valFrames).channelWidth(valChannelWidth).colorSpace(colorspace);
            wtfImage = builder.build();
        }
        editableWtfImage = wtfImage.edit();
        showImage();
    }
    void showImage() throws InterruptedException {
        if(editViewButton.isEditorVisible()) {
            if(editableWtfImage.animationInformation().isAnimated()) {
                showAnimatedImage(editableWtfImage);
            }
            imagePanel = new ImagePanel(editableWtfImage.asJavaImage());
            panel.add(imagePanel, BorderLayout.CENTER);
        } else {
            if(wtfImage.animationInformation().isAnimated()) {
                showAnimatedImage(wtfImage);
            }
            imagePanel = new ImagePanel(wtfImage.asJavaImage());
            panel.add(imagePanel, BorderLayout.CENTER);
        }
        //todo zurückstellen
        /*imagePanel = new ImagePanel(editableWtfImage.asJavaImage());
        panel.add(imagePanel, BorderLayout.CENTER);*/

        panel.revalidate();
        panel.repaint();
    }
    boolean unsavedWarning() {
        int choice = JOptionPane.showOptionDialog(
                null,
                "You have not saved your current image yet",
                "Note",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE,
                null,
                new Object[]{"Return", "Okay"},
                "Return"
        );
        return choice == 0;
    }
    void showAnimatedImage(WtfImage image) throws InterruptedException {
        Visible.setInvisible(editViewButton.getEditor());
        int seconds = 0;
        int frames = 0;
        long duration = 0;
        if(image.animationInformation().isFpsCoded()) {
            frames = image.animationInformation().framesPerSecond();
            duration = 1000 / frames;
        } else {
            seconds = image.animationInformation().secondsPerFrame();
            duration =seconds * 1000L;
        }

        long start = System.currentTimeMillis();
        int i = 0;
        panel.add(imagePanel, BorderLayout.CENTER);
        while(System.currentTimeMillis() - start > 30000) {
            if(i == image.animationInformation().frames()) {
                i = 0;
            }
            TimeUnit.MILLISECONDS.sleep(duration);
            if(editViewButton.isEditorVisible()) {
                imagePanel = new ImagePanel(editableWtfImage.animationInformation().frame(i).asJavaImage());
            } else {
                imagePanel = new ImagePanel(wtfImage.animationInformation().frame(i).asJavaImage());
            }
            panel.revalidate();
            panel.repaint();
            i++;
        }

    }
}
