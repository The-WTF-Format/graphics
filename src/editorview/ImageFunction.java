package editorview;

import image.LoadImage;
import panel.CreatePanel;
import utils.Colors;
import utils.Visible;
import wtf.file.api.color.ColorSpace;
import wtf.file.api.color.ColorSpaceChannels;
import wtf.file.api.color.channel.ColorChannel;
import wtf.file.api.editable.EditableWtfImage;
import wtf.file.api.editable.data.EditableFrame;
import wtf.file.api.editable.data.EditablePixel;

import utils.TransformationType;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Map;

/**
 * Handels image editing functions for the WTF image editor
 * Fuction: resizing, rotating, mirroring, color space conversion, pixel manipulation and format conversion
 * @see EditableWtfImage
 * @see TransformationType
 */

public class ImageFunction {

    private final PanelNorth panelNorth;
    private EditableWtfImage editable;
    JPanel mainPanel;
    ColorSpace selectedColorSpace;
    JSpinner widthPicker;
    JSpinner heightPicker;
    EditablePixel pixel;
    LoadImage loadimage;

    public ImageFunction(PanelNorth panelNorth, JPanel mainPanel) {
        this.panelNorth = panelNorth;
        this.mainPanel = mainPanel;
        this.loadimage = panelNorth.loadImage;

        if (panelNorth == null || panelNorth.loadImage == null) {
            //System.out.println("no imagepanel loaded!");
        }
        if (panelNorth != null) {
            if (panelNorth.loadImage != null) {
                editable = panelNorth.loadImage.getEditableWtfImage();
            }
        }
        if (editable == null) {
            //System.out.println("no image panel is loaded!");
        }
    }

    /**
     * Following Function are connected to the ActionRouter in FunctionMenu
     */

    /**
     * Opens a popup window to enter new height and with of the image
     * @see #changeWTFImageWidth(int)
     * @see #changeWTFImageHeight(int)
     */
    public void setEditableHeightWidth() {
        JDialog dialog = new JDialog((Frame) null, "Change Height width", true);
        dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        dialog.setSize(300, 200);
        dialog.setLayout(new BorderLayout());

        JPanel inputPanel = new JPanel(new GridLayout(2, 2, 10, 10));
        JLabel heightLabel = new JLabel("new height:");
        JTextField heightField = new JTextField();
        JLabel widthLabel = new JLabel("new width:");
        JTextField widthField = new JTextField();

        inputPanel.add(heightLabel);
        inputPanel.add(heightField);
        inputPanel.add(widthLabel);
        inputPanel.add(widthField);

        JButton okButton = new JButton("Accept");
        okButton.addActionListener(e -> {
            if(panelNorth.loadImage.editableWtfImage == null) {
                panelNorth.loadImage.editableWtfImage = panelNorth.loadImage.wtfImage.edit();
            }

            try {
                int height = Integer.parseInt(heightField.getText().trim());
                int width = Integer.parseInt(widthField.getText().trim());

                changeWTFImageWidth(width);
                changeWTFImageHeight(height);

                dialog.dispose();
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(dialog, "Please enter valid number!", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        dialog.add(inputPanel, BorderLayout.CENTER);
        dialog.add(okButton, BorderLayout.SOUTH);
        dialog.setLocationRelativeTo(null); // zentrieren
        dialog.setVisible(true);
    }

    /**
     * Shows the rotation transformation panel
     * @see #showTransformationPanel(TransformationType...)
     * @see TransformationType#ROTATE_CLOCKWISE
     * @see TransformationType#ROTATE_COUNTERCLOCKWISE
     */
    public void rotateEditable() {
        panelVisibility(false);
        showTransformationPanel(
                TransformationType.ROTATE_COUNTERCLOCKWISE,
                TransformationType.ROTATE_CLOCKWISE
        );
    }

    /**
     * Shows the mirror transformation panel
     * @see #showTransformationPanel(TransformationType...)
     * @see TransformationType#FLIP_HORIZONTAL
     * @see TransformationType#FLIP_HORIZONTAL
     */
    public void mirrorEditable() {
        panelVisibility(false);
        showTransformationPanel(
                TransformationType.FLIP_HORIZONTAL,
                TransformationType.FLIP_VERTICAL
        );
    }

    /**
     * Opens a dialog for selecting and applying a new color space to the image.
     * Presents all available color spaces as radio buttons and applies the selection.
     * Automatically creates an editable image if none exists.
     * @see ColorSpace
     */
    protected void colorSpaceSelection() {
        JDialog dialog = new JDialog((Frame) null, "Choose colorspace", true);
        dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        dialog.setPreferredSize(new Dimension(600, 220));
        dialog.setMaximumSize(new Dimension(600, 220));
        dialog.setMinimumSize(new Dimension(600, 220));
        CreatePanel gridPanel = new CreatePanel();
        gridPanel.setBackground(Colors.MAKERSPACEBACKGROUND);
        gridPanel.setLayout(new GridLayout(6,3));
        ButtonGroup group = new ButtonGroup();
        JRadioButton [] buttons = new JRadioButton[15];
        ColorSpace[] colorspaces = ColorSpace.values();

        JButton okButton = new JButton("Okay");
        gridPanel.add(okButton);
        for(int i = 0; i < 18; i++){
            if(i < 15) {
                buttons[i] = new JRadioButton(colorspaces[i].name());
                buttons[i].setMaximumSize(new Dimension(80, 30));
                buttons[i].setMaximumSize(new Dimension(80, 30));
                buttons[i].setPreferredSize(new Dimension(80, 30));
                group.add(buttons[i]);
                gridPanel.add(buttons[i]);
                buttons[i].addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        JRadioButton source = (JRadioButton) e.getSource();
                        selectedColorSpace = ColorSpace.valueOf(source.getText());
                    }
                });
            } else if(i == 16) {
                gridPanel.add(okButton);
            } else {
                gridPanel.add(new JPanel());
            }
        }
        okButton.addActionListener(e -> {
            if(selectedColorSpace == null) {
                return;
            }
            if(panelNorth.loadImage.editableWtfImage == null) {
                panelNorth.loadImage.editableWtfImage = panelNorth.loadImage.wtfImage.edit();
            }
            panelNorth.loadImage.editableWtfImage.setColorSpace(selectedColorSpace);
            dialog.dispose();
            try {
                panelNorth.loadImage.showImage();
            } catch (InterruptedException ex) {
                throw new RuntimeException(ex);
            }
        });
        dialog.getContentPane().add(gridPanel);
        dialog.pack();
        dialog.setLocationRelativeTo(null);
        dialog.setVisible(true);
    }

    /**
     * Opens a color picker interface for examining pixel values at specific coordinates.
     * Allows viewing color channel values and provides option to invert selected pixel.
     * Creates coordinate input fields and displays pixel information dialog.
     * @see #invertColor(EditablePixel)
     */
    protected void colorPicker() {
        if(panelNorth.loadImage.editableWtfImage == null) {
            panelNorth.loadImage.editableWtfImage = panelNorth.loadImage.wtfImage.edit();
        }
        Visible.setInvisible(panelNorth.loadImage.saveButton, panelNorth.loadImage.loadImage, panelNorth.loadImage.editViewButton.editor, panelNorth.editorMenuBar, panelNorth.loadImage.editViewButton.viewer);
        CreatePanel colorPickerPanel = new CreatePanel();
        colorPickerPanel.setPreferredSize(new Dimension(200, 50));
        colorPickerPanel.setLayout(new GridLayout(8, 1));
        colorPickerPanel.setBackground(Colors.MAKERSPACEBACKGROUND);

        JLabel label1 = new JLabel("   Enter a Coordinate: " + panelNorth.loadImage.editableWtfImage.width()
                + " x "
                + panelNorth.loadImage.editableWtfImage.height());
        JLabel widthText = new JLabel("  width");
        widthPicker = new JSpinner();
        JLabel heightText = new JLabel("  height");
        heightPicker = new JSpinner();
        colorPickerPanel.add(label1);
        colorPickerPanel.add(widthText);
        colorPickerPanel.add(widthPicker);
        colorPickerPanel.add(heightText);
        colorPickerPanel.add(heightPicker);
        JPanel background = new JPanel();
        background.setBackground(Colors.MAKERSPACEBACKGROUND);
        colorPickerPanel.add(background);

        JButton showValues = new JButton("Show values");
        colorPickerPanel.add(showValues);

        JButton invertThisValue = new JButton("Invert this value");
        JButton closePanel = new JButton("close");
        invertThisValue.addActionListener(x -> {
            Visible.setVisible(panelNorth.loadImage.saveButton, panelNorth.loadImage.loadImage);
            if(panelNorth.loadImage.editViewButton.isEditorVisible()) {
                Visible.setVisible(panelNorth.loadImage.editViewButton.viewer, panelNorth.editorMenuBar);
            } else {
                Visible.setVisible(panelNorth.loadImage.editViewButton.editor);
            }
            mainPanel.remove(colorPickerPanel);
            mainPanel.revalidate();
            mainPanel.repaint();
            invertColor(pixel);
        });
        closePanel.addActionListener( x -> {
            Visible.setVisible(panelNorth.loadImage.saveButton, panelNorth.loadImage.loadImage);
            if(panelNorth.loadImage.editViewButton.isEditorVisible()) {
                Visible.setVisible(panelNorth.loadImage.editViewButton.editor, panelNorth.editorMenuBar);
            } else {
                Visible.setVisible(panelNorth.loadImage.editViewButton.viewer);
            }
            mainPanel.remove(colorPickerPanel);
            mainPanel.revalidate();
            mainPanel.repaint();
        });
        showValues.addActionListener( x ->  {
            if((int)widthPicker.getValue() < 1
                    || (int)widthPicker.getValue() > panelNorth.loadImage.wtfImage.width()
                    || (int)heightPicker.getValue() < 1
                    || (int)heightPicker.getValue() > panelNorth.loadImage.wtfImage.height()) {
                JOptionPane optionPane = new JOptionPane("The width values have to be between 1 and " + panelNorth.loadImage.wtfImage.width()
                        + "\nThe height values have to be between 1 and " + panelNorth.loadImage.wtfImage.height(), JOptionPane.INFORMATION_MESSAGE);
                JDialog dialog = optionPane.createDialog("Note");
                dialog.setSize(450, 120);
                dialog.setLocationRelativeTo(null);
                dialog.setVisible(true);
                return;
            }
            String pixelText = widthPicker.getValue() + " x " + heightPicker.getValue();
            JDialog dialog = new JDialog((Frame) null, pixelText , true);
            dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
            dialog.setSize(200, 150);
            dialog.setLayout(new BorderLayout());
            JButton close = new JButton("close");

            pixel = panelNorth.loadImage.editableWtfImage.at((int) widthPicker.getValue()-1, (int )heightPicker.getValue()-1);
            StringBuilder pixelString = new StringBuilder("");
            for(ColorChannel c : pixel.colorSpace().channels()){
                pixelString.append(" ").append(c.name()).append(": ").append(pixel.valueOf(c)).append("\n");
            }
            JTextArea pixelInfo = new JTextArea(pixelString.toString());
            dialog.add(pixelInfo, BorderLayout.CENTER);
            dialog.add(close, BorderLayout.SOUTH);
            close.addActionListener( e -> {
                dialog.dispose();
                colorPickerPanel.remove(showValues);
                colorPickerPanel.add(invertThisValue);
                colorPickerPanel.add(closePanel);
                mainPanel.revalidate();
                mainPanel.repaint();
            });
            dialog.setLocationRelativeTo(null); // zentrieren
            dialog.setVisible(true);

        });
        mainPanel.add(colorPickerPanel, BorderLayout.EAST);
        mainPanel.revalidate();
        mainPanel.repaint();

    }

    /**
     * Opens a simplified interface for inverting colors at specific coordinates.
     * Provides coordinate input and direct color inversion functionality.
     * Hides other UI elements while active.
     * @see #invertColor(EditablePixel)
     */
    protected void invertColorPopUp() {
        if(panelNorth.loadImage.editableWtfImage == null) {
            panelNorth.loadImage.editableWtfImage = panelNorth.loadImage.wtfImage.edit();
        }
        Visible.setInvisible(panelNorth.loadImage.saveButton, panelNorth.loadImage.loadImage,
                panelNorth.loadImage.editViewButton.editor, panelNorth.editorMenuBar, panelNorth.loadImage.editViewButton.viewer);
        CreatePanel invert = new CreatePanel();
        invert.setPreferredSize(new Dimension(200, 50));
        invert.setLayout(new GridLayout(8, 1));
        invert.setBackground(Colors.MAKERSPACEBACKGROUND);

        JLabel label1 = new JLabel("   Enter a Coordinate: " + panelNorth.loadImage.editableWtfImage.width()
                + " x "
                + panelNorth.loadImage.editableWtfImage.height());
        JLabel widthText = new JLabel("  width");
        widthPicker = new JSpinner();
        JLabel heightText = new JLabel("  height");
        heightPicker = new JSpinner();
        invert.add(label1);
        invert.add(widthText);
        invert.add(widthPicker);
        invert.add(heightText);
        invert.add(heightPicker);
        JPanel background = new JPanel();
        background.setBackground(Colors.MAKERSPACEBACKGROUND);
        invert.add(background);

        JButton showValues = new JButton("Invert");
        invert.add(showValues);
        showValues.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if((int)widthPicker.getValue() < 1
                        || (int)widthPicker.getValue() > panelNorth.loadImage.wtfImage.width()
                        || (int)heightPicker.getValue() < 1
                        || (int)heightPicker.getValue() > panelNorth.loadImage.wtfImage.height()) {
                    JOptionPane optionPane = new JOptionPane("The width values have to be between 1 and " + panelNorth.loadImage.wtfImage.width()
                            + "\nThe height values have to be between 1 and " + panelNorth.loadImage.wtfImage.height(), JOptionPane.INFORMATION_MESSAGE);
                    JDialog dialog = optionPane.createDialog("Note");
                    dialog.setSize(450, 120);
                    dialog.setLocationRelativeTo(null);
                    dialog.setVisible(true);
                    return;
                }
                invertColor(panelNorth.loadImage.editableWtfImage.at((int) widthPicker.getValue()-1, (int )heightPicker.getValue()-1));
            }
        });
        JButton closeButton = new JButton("close");
        closeButton.addActionListener( x -> {
            Visible.setVisible(panelNorth.loadImage.saveButton, panelNorth.loadImage.loadImage);
            if(panelNorth.loadImage.editViewButton.isEditorVisible()) {
                Visible.setVisible(panelNorth.loadImage.editViewButton.viewer, panelNorth.editorMenuBar);
            } else {
                Visible.setVisible(panelNorth.loadImage.editViewButton.editor);
            }
            mainPanel.remove(invert);
            mainPanel.revalidate();
            mainPanel.repaint();
        });
        invert.add(closeButton);
        mainPanel.add(invert, BorderLayout.EAST);
        mainPanel.revalidate();
        mainPanel.repaint();
    }

    /**
     * Converts the current editable image to the specified format and saves it.
     * Handles alpha channel compatibility and creates appropriate BufferedImage.
     * JPEG format automatically removes alpha channel for compatibility.
     * @param extension the target file format (e.g., "JPEG", "PNG")
     * @see LoadImage#doSaveImage(BufferedImage, String)
     */
    public void converter(String extension) {
        if (panelNorth.loadImage.editableWtfImage == null) {
            panelNorth.loadImage.editableWtfImage = panelNorth.loadImage.wtfImage.edit();
        }
        EditableFrame frame = panelNorth.loadImage.editableWtfImage;

        ColorSpace cs = frame.at(1, 1).colorSpace();

        Image WTFimage = panelNorth.loadImage.editableWtfImage.asJavaImage();
        int width = panelNorth.loadImage.editableWtfImage.width();
        int height = panelNorth.loadImage.editableWtfImage.height();

        //Alpha Channel
        boolean hasAlpha = false;
        for (ColorChannel channel : cs.channels()) {
            if (channel == ColorSpaceChannels.FIXED_ALPHA || channel == ColorSpaceChannels.DYNAMIC_ALPHA) {
                hasAlpha = true;
                break;
            }
        }

        //transparent does not work for JPEG
        int imageType;
        if (extension.equalsIgnoreCase("JPEG")) {
            imageType = BufferedImage.TYPE_INT_RGB; //
        } else {
            imageType = hasAlpha ? BufferedImage.TYPE_INT_ARGB : BufferedImage.TYPE_INT_RGB;
        }
        BufferedImage bufferedImage = new BufferedImage(width, height, imageType);

        //draw Image as BufferedImage
        Graphics2D g2d = bufferedImage.createGraphics();
        g2d.drawImage(WTFimage, 0, 0, null);
        g2d.dispose();

        panelNorth.loadImage.doSaveImage(bufferedImage, extension);
    }


    /**
     * Privats Methods for editing image
     */

    /**
     * Transformation
     */

    /**
     * Changes the height of the editable image to the specified value.
     * Refreshes the image display after modification.
     * @param newHeight the new height in pixels
     * @throws RuntimeException if image display refresh fails
     * @see EditableWtfImage#setHeight(int)
     */
    protected void changeWTFImageHeight(int newHeight) {
        panelNorth.loadImage.editableWtfImage.setHeight(newHeight);
        try {
            panelNorth.loadImage.showImage();

        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Changes the width of the editable image to the specified value.
     * Refreshes the image display after modification.
     * @param newWidth the new width in pixels
     * @throws RuntimeException if image display refresh fails
     * @see EditableWtfImage#setWidth(int)
     */
    protected void changeWTFImageWidth(int newWidth) {
        panelNorth.loadImage.editableWtfImage.setWidth(newWidth);
        try {
            panelNorth.loadImage.showImage();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Applies the specified transformation to the entire image.
     * Handles rotation and mirroring operations by manipulating pixel arrays.
     * Creates temporary storage for pixel values to prevent data corruption during transformation.
     * @param type the transformation type to apply
     * @throws IllegalArgumentException if transformation type is unknown
     * @see TransformationType
     * @see #ensureHSVChannels(Map)
     */
    protected void transform(TransformationType type) {
        if (panelNorth.loadImage.editableWtfImage == null) {
            panelNorth.loadImage.editableWtfImage = panelNorth.loadImage.wtfImage.edit();
        }

        try {
            EditablePixel[][] original = panelNorth.loadImage.editableWtfImage.pixels();
            int width = original[0].length;
            int height = original.length;

            Map<ColorChannel, Short>[][] tempValues = new Map[height][width];


            for (int y = 0; y < height; y++) {
                for (int x = 0; x < width; x++) {
                    Map<ColorChannel, Short> originalValues = original[y][x].values();
                    Map<ColorChannel, Short> completeValues = new HashMap<>();

                    completeValues.putAll(originalValues);

                    ensureHSVChannels(completeValues);

                    tempValues[y][x] = completeValues;
                }
            }

            for (int y = 0; y < height; y++) {
                for (int x = 0; x < width; x++) {
                    int newY, newX;

                    switch (type) {
                        case ROTATE_COUNTERCLOCKWISE:
                            newY = x;
                            newX = height - 1 - y;
                            break;

                        case ROTATE_CLOCKWISE:
                            newY = width - 1 - x;
                            newX = y;
                            break;

                        case FLIP_HORIZONTAL:
                            newY = y;
                            newX = width - 1 - x;
                            break;

                        case FLIP_VERTICAL:
                            newY = height - 1 - y;
                            newX = x;
                            break;

                        default:
                            throw new IllegalArgumentException("Unknown transformation: " + type);
                    }

                    if (newY >= 0 && newY < height && newX >= 0 && newX < width) {
                        original[newY][newX].setValues(tempValues[y][x]);
                    }
                }
            }
            panelNorth.loadImage.showImage();

        } catch (Exception e) {
            System.err.println("ERROR: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Animation
     */

    /**
     * Opens a dialog to set seconds per frame for animation information.
     * Validates input range (0-127) and applies the setting to the editable image.
     * @see #frames(String, String)
     * @see EditableWtfImage#animationInformation()
     */
    protected void secondsPerFrames() {
        frames("Seconds per frame", "Seconds per frame (0 - 127)");
    }

    /**
     * Opens a dialog to set frames per second for animation information.
     * Validates input range (0-127) and applies the setting to the editable image.
     * @see #frames(String, String)
     * @see EditableWtfImage#animationInformation()
     */
    protected void framesPerSeconds() {
        frames("Frames per seconds", "Frames per seconds (0 - 127)");
    }


    /**
     * Creates and displays a dialog for setting animation frame parameters.
     * Provides input validation and applies the setting based on the header type.
     * @param header the dialog title and parameter type identifier
     * @param text the descriptive text for the input field
     */
    private void frames(String header, String text) {
        JDialog dialog = new JDialog((Frame) null, header, true);
        dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        dialog.setSize(400, 200);
        dialog.setLayout(new BorderLayout());

        // Panel für Eingabefelder
        JPanel inputPanel = new JPanel(new GridLayout(1, 2, 10, 50));
        JLabel label = new JLabel(text);
        JSpinner spinner = new JSpinner();
        inputPanel.add(label);
        inputPanel.add(spinner);

        // OK-Button
        JButton okButton = new JButton("Okay");
        okButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if((int) spinner.getValue() < 0 || (int) spinner.getValue() > 127) {
                    JOptionPane.showMessageDialog(null, "Please enter a value between 0 and 127!", "Invalid Value", JOptionPane.INFORMATION_MESSAGE);

                } else {
                    if(panelNorth.loadImage.editableWtfImage == null) {
                        panelNorth.loadImage.editableWtfImage = panelNorth.loadImage.wtfImage.edit();
                    }
                    if(header.equals("Seconds per frame")) {
                        panelNorth.loadImage.editableWtfImage.animationInformation().setSecondsPerFrame((int) spinner.getValue());
                    } else {
                        panelNorth.loadImage.editableWtfImage.animationInformation().setFramesPerSecond((int) spinner.getValue());
                    }
                    dialog.dispose();
                    try {
                        panelNorth.loadImage.showImage();

                    } catch (InterruptedException ex) {
                        throw new RuntimeException(ex);
                    }
                }
            }
        });
        dialog.add(inputPanel, BorderLayout.CENTER);
        dialog.add(okButton, BorderLayout.SOUTH);
        dialog.setLocationRelativeTo(null); // zentrieren
        dialog.setVisible(true);
    }

    /**
     * Color
     */

    /**
     * Inverts the color values of the specified pixel based on its color space.
     * For HSV color spaces, rotates HUE by 180 degrees while preserving saturation and value.
     * For other color spaces, inverts all color channels except alpha.
     * Refreshes the image display after modification.
     * @param pixel the pixel to invert
     * @see ColorSpace
     * @see ColorSpaceChannels
     */
    private void invertColor(EditablePixel pixel) {
        Map <ColorChannel, Short> newMap = new HashMap<>();
        ColorSpace colorSpace = pixel.colorSpace();

        short maxValue = (short) (Math.pow(2, panelNorth.loadImage.editableWtfImage.channelWidth()) -1);

        if (colorSpace == ColorSpace.HSV ||
                colorSpace == ColorSpace.HSVa ||
                colorSpace == ColorSpace.DYNAMIC_HSVa) {
            for (ColorChannel c : pixel.values().keySet()) {
                if (c.equals(ColorSpaceChannels.HUE)) {
                    short invertedValue = (short) ((pixel.valueOf(c) + (maxValue + 1) / 2) % (maxValue + 1));
                    newMap.put(c, invertedValue);

                } else {
                    newMap.put(c, pixel.valueOf(c));
                }
            }
        } else {
            for (ColorChannel c : pixel.values().keySet()) {
                if (!c.name().equals("alpha")) {
                    short invertedValue = (short) (maxValue - pixel.valueOf(c));
                    newMap.put(c, invertedValue);
                } else {
                    newMap.put(c, pixel.valueOf(c));

                }
            }
        }
        pixel.setValues(newMap);
        try {
            panelNorth.loadImage.showImage();
        } catch (InterruptedException ex) {
            System.out.println("Error at showing edits: " + ex);
        }

    }


    /**
     * Ensures HSV color channels have valid default values to prevent null pointer exceptions.
     * Sets default values: HUE=0 (red), SATURATION=0 (gray), VALUE=255 (bright), ALPHA=255 (opaque).
     * Required for proper Java image conversion and display.
     * @param values the color channel map to validate and fill
     */
    private void ensureHSVChannels(Map<ColorChannel, Short> values) {
        ColorChannel hueChannel = null;
        ColorChannel saturationChannel = null;
        ColorChannel valueChannel = null;
        ColorChannel alphaChannel = null;

        for (ColorChannel channel : values.keySet()) {
            String name = channel.toString().toLowerCase();
            if (name.contains("hue")) {
                hueChannel = channel;
            } else if (name.contains("saturation")) {
                saturationChannel = channel;
            } else if (name.contains("value")) {
                valueChannel = channel;
            } else if (name.contains("alpha")) {
                alphaChannel = channel;
            }
        }

        if (hueChannel != null && values.get(hueChannel) == null) {
            values.put(hueChannel, (short) 0); // Default Hue = 0 (rot)
        }
        if (saturationChannel != null && values.get(saturationChannel) == null) {
            values.put(saturationChannel, (short) 0); // Default Saturation = 0 (grau)
        }
        if (valueChannel != null && values.get(valueChannel) == null) {
            values.put(valueChannel, (short) 255); // Default Value = 255 (hell)
        }
        if (alphaChannel != null && values.get(alphaChannel) == null) {
            values.put(alphaChannel, (short) 255); // Default Alpha = 255 (undurchsichtig)
        }
    }

    /**
     * Methods for Panels
     */

    /**
     * shows panel with the specified transformation options
     * create button for types and handels visibility
     * @param transformations
     */
    protected void showTransformationPanel(TransformationType... transformations) {
        CreatePanel gridPanel = new CreatePanel();
        gridPanel.setLayout(new FlowLayout());
        gridPanel.setBackground(Color.LIGHT_GRAY);
        gridPanel.setPreferredSize(new Dimension(200, 50));

        if(panelNorth.loadImage.editableWtfImage == null) {
            panelNorth.loadImage.editableWtfImage = panelNorth.loadImage.wtfImage.edit();
        }

        for (TransformationType transformation : transformations) {
            JButton button = createTransformationButton(transformation);
            gridPanel.add(button);
        }

        JButton closeButton = new JButton("✕ close");
        closeButton.addActionListener(e -> closePanelInEast());
        gridPanel.add(closeButton);

        showPanelInEast(gridPanel);
    }

    /**
     * Displays the specified panel in the east region of the main panel.
     * Removes any existing east panel and hides save/load buttons during operation.
     * @param panel the panel to display in the east region
     * @see BorderLayout
     */
    private void showPanelInEast(JPanel panel) {
        if (mainPanel.getLayout() instanceof BorderLayout) {
            BorderLayout layout = (BorderLayout) mainPanel.getLayout();
            Component oldEast = layout.getLayoutComponent(BorderLayout.EAST);
            if (oldEast != null) {
                mainPanel.remove(oldEast);
            }
        }
        Visible.setInvisible(panelNorth.loadImage.saveButton, panelNorth.loadImage.loadImage);
        mainPanel.add(panel, BorderLayout.EAST);
        mainPanel.revalidate();
        mainPanel.repaint();
    }

    /**
     * Controls the visibility of main UI components based on the current operation state.
     * Shows or hides save button, load button, editor, menu bar, and viewer components.
     * @param klick true to show components, false to hide them
     */
    private void panelVisibility(boolean klick) {
        if(klick) {
            Visible.setVisible(panelNorth.loadImage.saveButton, panelNorth.loadImage.loadImage);
            if(panelNorth.loadImage.editViewButton.isEditorVisible()) {
                Visible.setVisible(panelNorth.loadImage.editViewButton.editor, panelNorth.editorMenuBar);
            } else {
                Visible.setVisible(panelNorth.loadImage.editViewButton.viewer);
            }
        } else {
            Visible.setInvisible(panelNorth.loadImage.saveButton, panelNorth.loadImage.loadImage,
                    panelNorth.loadImage.editViewButton.editor, panelNorth.editorMenuBar, panelNorth.loadImage.editViewButton.viewer);
        }

    }

    /**
     * Closes and removes the panel from the east region of the main layout.
     * Restores visibility of save and load buttons after panel closure.
     * @see #panelVisibility(boolean)
     */
    private void closePanelInEast() {
        if (mainPanel.getLayout() instanceof BorderLayout) {
            BorderLayout layout = (BorderLayout) mainPanel.getLayout();
            Component eastPanel = layout.getLayoutComponent(BorderLayout.EAST);
            if (eastPanel != null) {
                mainPanel.remove(eastPanel);
                mainPanel.revalidate();
                mainPanel.repaint();
            }
            Visible.setVisible(panelNorth.loadImage.saveButton,panelNorth.loadImage.saveButton, panelNorth.loadImage.loadImage);
        }
        panelVisibility(true);
    }

    /**
     * Creates a transformation button with appropriate text and action listener.
     * Maps transformation types to user-friendly button labels with symbols.
     * @param transformation the transformation type for the button
     * @return configured JButton with transformation action
     * @see #transform(TransformationType)
     */
    private JButton createTransformationButton(TransformationType transformation) {
        String text;
        switch (transformation) {
            case ROTATE_CLOCKWISE:
                text = "↷ Right";
                break;
            case ROTATE_COUNTERCLOCKWISE:
                text = "↶ Left";
                break;
            case FLIP_HORIZONTAL:
                text = "↔ Horizontal";
                break;
            case FLIP_VERTICAL:
                text = "↕ Vertical";
                break;
            default:
                text = transformation.toString();
        }

        JButton button = new JButton(text);
        button.addActionListener(e -> transform(transformation));
        return button;
    }


}
