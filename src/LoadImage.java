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
import javax.imageio.ImageReader;
import javax.swing.*;
import javax.swing.event.MenuEvent;
import javax.swing.event.MenuListener;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Objects;
import java.util.concurrent.TimeUnit;
import java.util.List;


public class LoadImage {
    JButton saveButton;
    JMenuBar menu;
    JMenu loadImage;
    CreatePanel panel;
    JPanel lowPanel;
    ImagePanel imagePanel;
    BufferedImage image;
    JMenuItem createNewImage;
    JMenuItem byPath;
    JMenuItem normalImage;
    EditViewButton editViewButton;
    WtfImage wtfImage;
    WtfImageBuilder builder;
    EditableWtfImage editableWtfImage;
    boolean loaded = false;
    String standardFormat;

    LoadImage(JMenuBar menu, CreatePanel panel, EditViewButton editViewButton) {
        this.menu = menu;
        this.panel = panel;
        this.editViewButton = editViewButton;
        addMenu();
    }

    void setImagePanel(Image image) {
        this.imagePanel = new ImagePanel(image);
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
                if(imagePanel != null) {
                    if (unsavedWarning()) {
                        return;
                    }
                    if(wtfImage.animationInformation().isAnimated()) {
                        Visible.setVisible(editViewButton.panelNorth.editorMenu.functionMenuEditor.colorMenu,
                                editViewButton.panelNorth.editorMenu.functionMenuEditor.generalMenu,
                                editViewButton.panelNorth.editorMenu.functionMenuEditor.converterMenu);
                    } else {
                        Visible.setInvisible(editViewButton.panelNorth.editorMenu.functionMenuEditor.animationMenu);
                    }
                    panel.remove(imagePanel);
                    imagePanel = null;
                    image = null;
                    wtfImage = null;
                    editableWtfImage = null;
                }
                builder = WtfLoader.by();
                Visible.setInvisible(byPath, createNewImage, saveButton, editViewButton.getEditor(), editViewButton.getViewer(), editViewButton.panelNorth.editorMenuBar);
                createNewImage(builder);
            }
        });
    }

    private void addPathItem() {
        byPath = new JMenuItem("WTF by path");
        loadImage.add(byPath);
        byPath.setBackground(Colors.ITEMSPRIMARY);
        byPath.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(imagePanel != null) {
                    if (unsavedWarning()) {
                        return;
                    }
                    if(wtfImage.animationInformation().isAnimated()) {
                        Visible.setVisible(editViewButton.panelNorth.editorMenu.functionMenuEditor.colorMenu,
                                editViewButton.panelNorth.editorMenu.functionMenuEditor.generalMenu,
                                editViewButton.panelNorth.editorMenu.functionMenuEditor.converterMenu);
                    } else {
                        Visible.setInvisible(editViewButton.panelNorth.editorMenu.functionMenuEditor.animationMenu);
                    }
                    panel.remove(imagePanel);
                    imagePanel = null;
                    image = null;
                    wtfImage = null;
                    editableWtfImage = null;
                }
                Visible.setInvisible(editViewButton.getViewer(), editViewButton.getEditor(), editViewButton.panelNorth.editorMenuBar);
                try {
                    wtfImage = WtfLoader.from(Objects.requireNonNull(getNewPath()));
                    loaded = true;
                } catch (WtfException | IOException ex) {
                    System.out.println(ex);
                }

                if(editViewButton.isEditorVisible()) {
                    Visible.setVisible(saveButton, editViewButton.getViewer(), editViewButton.panelNorth.editorMenuBar);
                } else {
                    Visible.setVisible(saveButton, editViewButton.getEditor());
                }
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
                    if (unsavedWarning()) {
                        return;
                    }
                    if(wtfImage.animationInformation().isAnimated()) {
                        Visible.setVisible(editViewButton.panelNorth.editorMenu.functionMenuEditor.colorMenu,
                                editViewButton.panelNorth.editorMenu.functionMenuEditor.generalMenu,
                                editViewButton.panelNorth.editorMenu.functionMenuEditor.converterMenu);
                    } else {
                        Visible.setInvisible(editViewButton.panelNorth.editorMenu.functionMenuEditor.animationMenu);
                    }
                    panel.remove(imagePanel);
                    imagePanel = null;
                    image = null;
                    wtfImage = null;
                    editableWtfImage = null;
                }
                Visible.setInvisible(editViewButton.getViewer(), editViewButton.getEditor(), editViewButton.panelNorth.editorMenuBar);
                getNewImage();
                Visible.setVisible(saveButton);

                imagePanel = new BufferedImagePanel(image);
                panel.add(imagePanel, BorderLayout.CENTER);
                panel.revalidate();
                panel.repaint();
            }
        });
        loaded = true;

    }

    private void getNewImage() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Bild auswählen");
        fileChooser.setFileFilter(new FileNameExtensionFilter(null, "jpg", "png", "jpeg"));

        int result = fileChooser.showOpenDialog(panel);
        //Zustand: ob der Benutzer eine Datei geöffnet hat, oder abgebrochen hat oder ein Fehler aufgetreten ist
        if(result == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();
            standardFormat = selectedFile.getName().split("\\.")[1];
            System.out.println(standardFormat);
            try {
                image = ImageIO.read(selectedFile);
                loaded = true;
            } catch (Exception ex) {
                System.out.println("Error when reading Image: " + ex);
                loaded = false;
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
        loaded = true;
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
        lowPanel = new JPanel();
        lowPanel.setLayout(new FlowLayout());
        lowPanel.setBackground(Colors.BACKGROUND);
        panel.add(lowPanel, BorderLayout.SOUTH);
        lowPanel.add(saveButton);
        Visible.setInvisible(saveButton);
        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(wtfImage!= null) {
                    // saving a WTF Image
                    if(editableWtfImage == null) {
                        // initialize editableWTFImage if it does not exist yet
                        editableWtfImage = wtfImage.edit();
                    }
                    try {
                        System.out.println("I will ask for a path");
                        Path path = getSavingPath();
                        System.out.println("Got a Path");
                        editableWtfImage.save(path);
                        System.out.println("Saved this Image");
                    } catch (IOException | WtfException ex) {
                        throw new RuntimeException(ex);
                    }
                } else {
                    // normal Image formats
                    File file = new File((getSavingPath().toString()) + "." +  standardFormat);
                    try {
                       ImageIO.write(image, standardFormat, file);
                    } catch (Exception ex) {
                        System.out.println("Error when saving Image: " + ex);
                    }

                }
                panel.remove(imagePanel);
                Visible.setInvisible(saveButton);
                if(wtfImage.animationInformation().isAnimated()) {
                    Visible.setVisible(editViewButton.panelNorth.editorMenu.functionMenuEditor.colorMenu,
                            editViewButton.panelNorth.editorMenu.functionMenuEditor.generalMenu,
                            editViewButton.panelNorth.editorMenu.functionMenuEditor.converterMenu);
                }
                if(editViewButton.isEditorVisible()) {
                    Visible.setVisible(editViewButton.getViewer(), editViewButton.panelNorth.editorMenuBar);
                } else {
                    Visible.setVisible(editViewButton.getEditor(), editViewButton.panelNorth.viewerMenuBar);
                }
                imagePanel.repaint();
                imagePanel.revalidate();
                imagePanel = null;
                wtfImage = null;
                editableWtfImage = null;
                image = null;
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
        if(editableWtfImage != null && !wtfImage.edit().equals(editableWtfImage)) {
            wtfImage = null;
        }
        if (imagePanel != null) {
            panel.remove(imagePanel);
            imagePanel = null;
            image = null;
            wtfImage = null;
            editableWtfImage = null;
        }
        panel.repaint();
        panel.revalidate();
        if(editableWtfImage != null) {
            if(editableWtfImage.animationInformation().isAnimated()) {
                Visible.setInvisible(editViewButton.panelNorth.editorMenu.functionMenuEditor.colorMenu,
                            editViewButton.panelNorth.editorMenu.functionMenuEditor.generalMenu,
                            editViewButton.panelNorth.editorMenu.functionMenuEditor.converterMenu);
                showAnimatedImage(editableWtfImage);
                //imagePanel = new ImagePanel(editableWtfImage.animationInformation().frame(0).asJavaImage());
                //panel.add(imagePanel, BorderLayout.CENTER);
            } else {
                Visible.setInvisible(editViewButton.panelNorth.editorMenu.functionMenuEditor.animationMenu);
                imagePanel = new ImagePanel(editableWtfImage.asJavaImage());
                panel.add(imagePanel, BorderLayout.CENTER);
            }
        } else {
            if(wtfImage.animationInformation().isAnimated()) {
                Visible.setInvisible(editViewButton.panelNorth.editorMenu.functionMenuEditor.colorMenu,
                            editViewButton.panelNorth.editorMenu.functionMenuEditor.generalMenu,
                            editViewButton.panelNorth.editorMenu.functionMenuEditor.converterMenu);
                showAnimatedImage(wtfImage);
            } else {
                Visible.setInvisible(editViewButton.panelNorth.editorMenu.functionMenuEditor.animationMenu);
                imagePanel = new ImagePanel(wtfImage.asJavaImage());
                panel.add(imagePanel, BorderLayout.CENTER);
            }
        }
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

    void showAnimatedImage(WtfImage localImage) throws InterruptedException {
        Visible.setInvisible(saveButton, loadImage);
        int seconds = 0;
        int frames = 0;
        int duration = 0;
        if(localImage.animationInformation().isFpsCoded()) {
            frames = localImage.animationInformation().framesPerSecond();
            duration = 1000 / frames;
        } else {
            seconds = localImage.animationInformation().secondsPerFrame();
            duration = seconds * 1000;
        }
        imagePanel = new ImagePanel(localImage.animationInformation().frame(0).asJavaImage());
        /*if(editableWtfImage != null) {
            imagePanel = new ImagePanel(editableWtfImage.animationInformation().frame(0).asJavaImage());
        } else {
            imagePanel = new ImagePanel(wtfImage.animationInformation().frame(0).asJavaImage());
        }*/
        panel.add(imagePanel, BorderLayout.CENTER);
        panel.revalidate();
        panel.repaint();
        final int [] i = {0};
        JButton stop = new JButton("stop");
        stop.setBackground(Colors.ITEMSPRIMARY);
        lowPanel.add(stop);
        Timer timer = new Timer(duration, e -> {
            System.out.println(System.currentTimeMillis());
            Image next = null;

            /*if(editableWtfImage != null) {
                next = editableWtfImage.animationInformation().frame(i[0]).asJavaImage();
            } else {
                next = wtfImage.animationInformation().frame(i[0]).asJavaImage();
            }*/
            next = localImage.animationInformation().frame(i[0]).asJavaImage();
            imagePanel.setImage(next);
            imagePanel.revalidate();
            imagePanel.repaint();
            i[0] = i[0]+1;
            if(i[0] == localImage.animationInformation().frames()) {
                i[0] = 0;
            }
        });
        timer.start();
        stop.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                lowPanel.remove(stop);
                timer.stop();
                imagePanel.repaint();
                imagePanel.revalidate();
                panel.repaint();
                panel.revalidate();
                if(editViewButton.isEditorVisible()) {
                    Visible.setInvisible(editViewButton.getViewer(), editViewButton.panelNorth.editorMenuBar);
                    Visible.setVisible(saveButton, loadImage);
                } else {
                    Visible.setInvisible(editViewButton.getEditor());
                    Visible.setVisible(saveButton, loadImage);
                }
            }
        });
    }

}
