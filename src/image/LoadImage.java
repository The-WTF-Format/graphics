package image;

import editorview.EditViewButton;
import panel.BufferedImagePanel;
import panel.CreatePanel;
import panel.ImagePanel;
import utils.Colors;
import utils.Size;
import utils.Visible;
import wtf.file.api.WtfImage;
import wtf.file.api.WtfLoader;
import wtf.file.api.builder.WtfImageBuilder;
import wtf.file.api.color.ColorSpace;
import wtf.file.api.editable.EditableWtfImage;
import wtf.file.api.editable.compression.DataCompressionType;
import wtf.file.api.exception.WtfException;

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
import java.util.Objects;

/**
 * stores all the most general image functions:
 * - load the image ...
 * ... in 3 different ways
 * - save the image
 */
public class LoadImage {
    public JButton saveButton;
    JMenuBar menu;
    public JMenu loadImage;

    public CreatePanel panel;
    JPanel lowPanel;
    ImagePanel imagePanel;
    BufferedImage image;
    public JMenuItem createNewImage;
    public JMenuItem byPath;
    JMenuItem normalImage;
    public EditViewButton editViewButton;
    public WtfImage wtfImage;
    WtfImageBuilder builder;
    public EditableWtfImage editableWtfImage;
    boolean loaded = false;
    String standardFormat;
    JButton stop;

    /**
     * initializes an LoadImage object and calls the methode addMenu()
     * @param menu the main menuBar
     * @param panel the main panel
     * @param editViewButton the object that managed the view and the editor buttons
     */
    public LoadImage(JMenuBar menu, CreatePanel panel, EditViewButton editViewButton) {
        this.menu = menu;
        this.panel = panel;
        this.editViewButton = editViewButton;
        addMenu();
    }
    public EditableWtfImage getEditableWtfImage() {
        return editableWtfImage;
    }

    /**
     * the menu for the different ways of loading a image
     * calls the methods addMenuItems and saveImage
     */
    private void addMenu() {
        loadImage = new JMenu("Load image");
        loadImage.setPreferredSize(Size.BUTTONSIZEMAINMENU);
        loadImage.setMinimumSize(Size.BUTTONSIZEMAINMENU);
        loadImage.setMaximumSize(Size.BUTTONSIZEMAINMENU);
        menu.add(loadImage);
        addMenuItems();
        saveImage();
    }

    /**
     * calls methods
     */
    private void addMenuItems() {
        addPathItem();
        addCreateItem();
        getNormalImageByPath();
    }

    /**
     * initializes the JMenuItem for "create new image"
     * the function of its actionListener that calls WtfLoader.by() and
     * createNewImage(builder) that initializes a new CreateNewImage object
     * @see CreateNewImage
     */
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
                        /**
                         * the user decide to cancel this operation, because there is already a loaded image
                         */
                    }
                    if(wtfImage.animationInformation().isAnimated()) {
                        Visible.setVisible(editViewButton.panelNorth.editorMenu.functionMenuEditor.colorMenu,
                                editViewButton.panelNorth.editorMenu.functionMenuEditor.generalMenu,
                                editViewButton.panelNorth.editorMenu.functionMenuEditor.converterMenu);
                        lowPanel.remove(stop);
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
    /**
     * initializes the JMenuItem for "WTF by path"
     * the function of its actionListener that calls WtfLoader.from() by handling over a path
     * calls the methode showImage()
     * only usable for wtf images
     */
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
                        /**
                         * the user decide to cancel this operation, because there is already a loaded image
                         */
                    }
                    if(wtfImage.animationInformation().isAnimated()) {
                        Visible.setVisible(editViewButton.panelNorth.editorMenu.functionMenuEditor.colorMenu,
                                editViewButton.panelNorth.editorMenu.functionMenuEditor.generalMenu,
                                editViewButton.panelNorth.editorMenu.functionMenuEditor.converterMenu);
                        lowPanel.remove(stop);
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
    /**
     * initializes the JMenuItem for "get standard format Image"
     * the function of its actionListener that loads the image by its path as a bufferedImage
     * calls the methode showImage()
     * only usable for non-wtf images
     */
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
                        /**
                         * the user decide to cancel this operation, because there is already a loaded image
                         */
                    }
                    if(wtfImage.animationInformation().isAnimated()) {
                        Visible.setVisible(editViewButton.panelNorth.editorMenu.functionMenuEditor.colorMenu,
                                editViewButton.panelNorth.editorMenu.functionMenuEditor.generalMenu,
                                editViewButton.panelNorth.editorMenu.functionMenuEditor.converterMenu);
                        lowPanel.remove(stop);
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

    /**
     * gets the path of a non wtf image and saves the image as the Bufferedimage image
     */
    private void getNewImage() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Select image");
        fileChooser.setFileFilter(new FileNameExtensionFilter(null, "jpg", "png", "jpeg"));

        int result = fileChooser.showOpenDialog(panel);
        //State: if the User opened a file or canceled the operation or an error occurred
        if(result == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();
            standardFormat = selectedFile.getName().split("\\.")[1];
            try {
                image = ImageIO.read(selectedFile);
                loaded = true;
            } catch (Exception ex) {
                System.out.println("Error when reading Image: " + ex);
                loaded = false;
            }
        }
    }

    /**
     * @return the path for a WTFImage to load
     */
    private Path getNewPath() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Select image");
        fileChooser.setFileFilter(new FileNameExtensionFilter(null, "wtf"));

        int result = fileChooser.showOpenDialog(panel);
        if(result == JFileChooser.APPROVE_OPTION) {
            return fileChooser.getSelectedFile().toPath();
        }
        loaded = true;
        return null;

    }

    /**
     * gets the path for a wtf or a non wtf image to be saved
     * it adds the format extension of each format
     * @param isWTFImage gives information whether the image is a wtf or w non wtf image
     * @return the path of the image to be saved
     */
    private Path getSavingPath(Boolean isWTFImage) {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Choose path");
        int result = fileChooser.showOpenDialog(panel);
        if(result == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();
            String fileName = selectedFile.getName();
            String extension = ".wtf";

            if (isWTFImage && !fileName.toLowerCase().endsWith(extension)) {
                selectedFile = new File(selectedFile.getParentFile(), fileName + extension);
            }

            return selectedFile.toPath();
        }
        return null;
    }

    /**
     * the button and the actionListener to save an image
     * calls doSaveImage();
     * manages the visibility of different Swing components
     * sets all variables that store information about the current image to null
     */
    private void saveImage() {
        saveButton = new JButton("save image");
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
                doSaveImage(null, "WTF");
                panel.remove(imagePanel);
                Visible.setInvisible(saveButton);
                if(wtfImage != null && wtfImage.animationInformation().isAnimated()) {
                    Visible.setVisible(editViewButton.panelNorth.editorMenu.functionMenuEditor.colorMenu,
                            editViewButton.panelNorth.editorMenu.functionMenuEditor.generalMenu,
                            editViewButton.panelNorth.editorMenu.functionMenuEditor.converterMenu);
                    lowPanel.remove(stop);
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

    /**
     * initializes a new CreateNewImage object
     * @see CreateNewImage
     */
    private void createNewImage(WtfImageBuilder builder) {
        new CreateNewImage(this, builder);
    }

    /**
     * gets the parameter that were set in
     * @see CreateNewImage from the user to create a new image
     * calls showImage()
     * @param valWidth
     * @param valHeight
     * @param valSecondsPerFrame
     * @param valFramePerSeconds
     * @param valFrames
     * @param valChannelWidth
     * @param colorspace
     * @throws InterruptedException when a invalid parameter was set
     */
    public void onCreateNewImageDone(int valWidth, int valHeight, int valSecondsPerFrame, int valFramePerSeconds, int valFrames, int valChannelWidth, ColorSpace colorspace) throws InterruptedException {
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

    /**
     * manages the visibility of an image depending on the image being animated or not
     * calls showAnimatedImage() when the image is animated
     * creates an ImagePanel object that draws the image
     * @see ImagePanel
     * @throws InterruptedException
     */
    public void showImage() throws InterruptedException {
        if (imagePanel != null) {
            panel.remove(imagePanel);
        }
        panel.repaint();
        panel.revalidate();
        if(editableWtfImage != null) {
            if(editableWtfImage.animationInformation().isAnimated()) {
                showAnimatedImage(editableWtfImage);
            } else {
                Visible.setInvisible(editViewButton.panelNorth.editorMenu.functionMenuEditor.animationMenu);
                imagePanel = new ImagePanel(editableWtfImage.asJavaImage());
                panel.add(imagePanel, BorderLayout.CENTER);
            }
        } else {
            if(wtfImage.animationInformation().isAnimated()) {
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

    /**
     * displays a warning saying that you are about to call a new image, although there is already a loaded image
     * @return false when the user clicks on "return" and excepts that the current image will be deleted
     */
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

    /**
     * gets all the information about the animation features
     * iterates through all frames of an animated image and displays the new frame after the given spf or fps
     * creates a new ImagePanel that draws the frame
     * @see ImagePanel
     * @param localImage the loaded image that is animated
     * @throws InterruptedException
     */

    void showAnimatedImage(WtfImage localImage) throws InterruptedException {
        Visible.setVisible(loadImage);
        if(editViewButton.isEditorVisible()) {
            Visible.setInvisible(editViewButton.getViewer(), editViewButton.panelNorth.editorMenuBar);
        } else {
            Visible.setInvisible(editViewButton.getEditor(), editViewButton.panelNorth.viewerMenuBar);
        }
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
        panel.add(imagePanel, BorderLayout.CENTER);
        panel.revalidate();
        panel.repaint();
        final int [] i = {0};
        stop = new JButton("stop");
        stop.setBackground(Colors.ITEMSPRIMARY);
        lowPanel.add(stop);
        Timer timer = new Timer(duration, e -> {
            Image next;
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
                    Visible.setVisible(editViewButton.getViewer(), editViewButton.panelNorth.editorMenuBar, saveButton, loadImage);
                } else {
                    Visible.setVisible(editViewButton.getEditor(), saveButton, loadImage);
                }
                Visible.setInvisible(editViewButton.panelNorth.editorMenu.functionMenuEditor.colorMenu,
                        editViewButton.panelNorth.editorMenu.functionMenuEditor.generalMenu,
                        editViewButton.panelNorth.editorMenu.functionMenuEditor.converterMenu);
            }
        });
    }

    /**
     * saves a image by getting a path from the user
     * can be called from other methodes
     * @param image BufferedImage that has to be saved
     * @param extension the Image extension for the image to get saved correctly
     */
    public void doSaveImage(BufferedImage image, String extension) {
        extension = extension.toLowerCase();
        // for a wtf image
        if (extension.equals("wtf")) {
            if (wtfImage != null) {
                if (editableWtfImage == null) {
                    editableWtfImage = wtfImage.edit();
                }
                try {
                    //choose path
                    Path path = getSavingPath(true); // true = WTF Format
                    //compression
                    if (editableWtfImage.width() * editableWtfImage.height() < 50000) {
                        editableWtfImage.save(path);
                    } else {
                        editableWtfImage.save(path, DataCompressionType.NO_COMPRESSION);
                    }
                } catch (IOException | WtfException ex) {
                    throw new RuntimeException(ex);
                }
            } else {
                System.out.println("No WTF image to save.");
            }

        } else if (extension.equals("jpeg") || extension.equals("png")) {
            if (image != null) {
                Path path = getSavingPath(false);
                String pathStr = path.toString();
                String extLower = extension.toLowerCase();

                // tests whether there already is an existing path extension for this image (for lower and upper case letters)
                if (!pathStr.toLowerCase().endsWith("." + extLower)) {
                    pathStr += "." + extLower;
                }

                File file = new File(pathStr);

                try {
                    ImageIO.write(image, extLower, file);
                } catch (Exception ex) {
                    System.out.println("Error at saving as " + extension + ": " + ex);
                }
            } else {
                System.out.println("No BufferedImage to save.");
            }

        } else {
            System.out.println("Unknown Format: " + extension);
        }
    }

}