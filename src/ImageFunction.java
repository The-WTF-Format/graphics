import values.Colors;
import wtf.file.api.color.ColorSpace;
import wtf.file.api.color.channel.ColorChannel;
import wtf.file.api.editable.EditableWtfImage;
import wtf.file.api.editable.data.EditablePixel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.Map;


public class ImageFunction {

    private final PanelNorth panelNorth;
    private EditableWtfImage editable;
    JPanel mainPanel;
    ColorSpace selectedColorSpace;
    JSpinner widthPicker;
    JSpinner heightPicker;

    public ImageFunction(PanelNorth panelNorth, JPanel mainPanel) {
        this.panelNorth = panelNorth;
        this.mainPanel = mainPanel;

        //ToDo panel wird mit Null übergeben -> muss gefixt werden, sonst wird bild nicht übergeben

        if (panelNorth == null || panelNorth.loadImage == null) {
            System.out.println("no imagepanel loaded!");
        }
        if (panelNorth != null) {
            if (panelNorth.loadImage != null) {
                //todo delete -> editing this will not change the actual visible image
                editable = panelNorth.loadImage.getEditableWtfImage();
            }
        }
        if (editable == null) {
            System.out.println("no image panel is loaded!");
        }
    }

    //-----------Funktionen für FUnction Menu-------------//
    public void setEditableHeightWidth() {
        //neues Fenster erstellen
        JDialog dialog = new JDialog((Frame) null, "Höhe und Breite ändern", true);
        dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        dialog.setSize(300, 200);
        dialog.setLayout(new BorderLayout());

        // Panel für Eingabefelder
        JPanel inputPanel = new JPanel(new GridLayout(2, 2, 10, 10));
        JLabel heightLabel = new JLabel("Neue Höhe:");
        JTextField heightField = new JTextField();
        JLabel widthLabel = new JLabel("Neue Breite:");
        JTextField widthField = new JTextField();

        inputPanel.add(heightLabel);
        inputPanel.add(heightField);
        inputPanel.add(widthLabel);
        inputPanel.add(widthField);

        // OK-Button
        JButton okButton = new JButton("Übernehmen");
        //TODO: Bitte auf englisch übersetzten
        okButton.addActionListener(e -> {
            //Abfrage ob editable bereits vorhanden, sonst anlegen
            if(panelNorth.loadImage.editableWtfImage == null) {
                panelNorth.loadImage.editableWtfImage = panelNorth.loadImage.wtfImage.edit();
            }

            try {
                int height = Integer.parseInt(heightField.getText().trim());
                int width = Integer.parseInt(widthField.getText().trim());

                //Methode aufrufen
                changeWTFImageWidth(width);
                changeWTFImageHeight(height);

                dialog.dispose();
            } catch (NumberFormatException ex) {
                //TODO: Bitte auf englisch übersetzten
                JOptionPane.showMessageDialog(dialog, "Bitte gültige Zahlen eingeben!", "Fehler", JOptionPane.ERROR_MESSAGE);
            }
        });

        //Interface zusammensetzen
        dialog.add(inputPanel, BorderLayout.CENTER);
        dialog.add(okButton, BorderLayout.SOUTH);
        dialog.setLocationRelativeTo(null); // zentrieren
        dialog.setVisible(true);
    }

    public void rotateEditable() {
        showTransformationPanel(
                TransformationType.ROTATE_COUNTERCLOCKWISE,
                TransformationType.ROTATE_CLOCKWISE
        );
    }

    public void mirrorEditable() {
        showTransformationPanel(
                TransformationType.FLIP_HORIZONTAL,
                TransformationType.FLIP_VERTICAL
        );
    }

    // ----------Methoden zum Panel herzeigen----------//
    protected void showTransformationPanel(TransformationType... transformations) {
        CreatePanel gridPanel = new CreatePanel();
        gridPanel.setLayout(new FlowLayout());
        gridPanel.setBackground(Color.LIGHT_GRAY);
        gridPanel.setPreferredSize(new Dimension(200, 50));

        if(panelNorth.loadImage.editableWtfImage == null) {
            panelNorth.loadImage.editableWtfImage = panelNorth.loadImage.wtfImage.edit();
        }

        // Buttons basierend auf den übergebenen Transformationen erstellen
        for (TransformationType transformation : transformations) {
            JButton button = createTransformationButton(transformation);
            gridPanel.add(button);
        }

        JButton closeButton = new JButton("✕ Schließen");
        closeButton.addActionListener(e -> closePanelInEast());
        gridPanel.add(closeButton);

        // Panel anzeigen
        showPanelInEast(gridPanel);
    }



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

    //------------private Methods----------------//
    //Transform
    protected void changeWTFImageHeight(int newHeight) {
        panelNorth.loadImage.editableWtfImage.setHeight(newHeight);
        System.out.println("Neue Höhe: " + panelNorth.loadImage.editableWtfImage.height());
        try {
            //Neues Bild muss ins Panel geladen werden
            panelNorth.loadImage.showImage();

        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    protected void changeWTFImageWidth(int newWidth) {
        //Höhe ändern
        panelNorth.loadImage.editableWtfImage.setWidth(newWidth);
        System.out.println("Neue Höhe: " + panelNorth.loadImage.editableWtfImage.height());
        try {
            //Neues Bild muss ins Panel geladen werden
            panelNorth.loadImage.showImage();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    protected void transform(TransformationType type) {
        if (panelNorth.loadImage.editableWtfImage == null) {
            panelNorth.loadImage.editableWtfImage = panelNorth.loadImage.wtfImage.edit();
        }

        try {
            EditablePixel[][] original = panelNorth.loadImage.editableWtfImage.pixels();
            int width = original[0].length;
            int height = original.length;

            //Temporärer Map für ColorChannel
            Map<ColorChannel, Short>[][] tempValues = new Map[height][width];

            //Alle Pixel-Werte in temporäres Array kopieren MIT Default-Werten
            for (int y = 0; y < height; y++) {
                for (int x = 0; x < width; x++) {
                    Map<ColorChannel, Short> originalValues = original[y][x].values();
                    Map<ColorChannel, Short> completeValues = new HashMap<>();

                    // Alle Original-Werte kopieren
                    completeValues.putAll(originalValues);

                    // Fehlende HSV-Kanäle mit Default-Werten ergänzen
                    ensureHSVChannels(completeValues);

                    tempValues[y][x] = completeValues;
                }
            }

            //Rotierte Werte zurück in die Pixel schreiben
            for (int y = 0; y < height; y++) {
                for (int x = 0; x < width; x++) {
                    int newY, newX;

                    switch (type) {
                        case ROTATE_CLOCKWISE:
                            // 90° rechts: (x,y) -> (y, width-1-x)
                            newY = x;
                            newX = height - 1 - y;
                            break;

                        case ROTATE_COUNTERCLOCKWISE:
                            // 90° links: (x,y) -> (height-1-x, y)
                            newY = width - 1 - x;
                            newX = y;
                            break;

                        case FLIP_HORIZONTAL:
                            // Horizontal spiegeln: (x,y) -> (width-1-x, y)
                            newY = y;
                            newX = width - 1 - x;
                            break;

                        case FLIP_VERTICAL:
                            // Vertikal spiegeln: (x,y) -> (x, height-1-y)
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

    //Animiation
    protected void secondsPerFrames() {
        frames("Seconds per frame", "Seconds per frame (0 - 127)");
    }
    protected void framesPerSeconds() {
        frames("Frames per seconds", "Frames per seconds (0 - 127)");
    }
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
                        System.out.println("Neue Seconds per frame: " + panelNorth.loadImage.editableWtfImage.animationInformation().secondsPerFrame());
                    } else {
                        panelNorth.loadImage.editableWtfImage.animationInformation().setFramesPerSecond((int) spinner.getValue());
                        System.out.println("Neue Frames per second: " + panelNorth.loadImage.editableWtfImage.animationInformation().framesPerSecond());
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

    //Color
    protected void invertColor() {
        if(panelNorth.loadImage.editableWtfImage == null) {
            panelNorth.loadImage.editableWtfImage = panelNorth.loadImage.wtfImage.edit();
        }
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
        mainPanel.add(invert, BorderLayout.EAST);
        mainPanel.revalidate();
        mainPanel.repaint();
    }
    protected void colorPicker() {
        if(panelNorth.loadImage.editableWtfImage == null) {
            panelNorth.loadImage.editableWtfImage = panelNorth.loadImage.wtfImage.edit();
        }
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
            mainPanel.remove(colorPickerPanel);
            mainPanel.revalidate();
            mainPanel.repaint();
        });
        closePanel.addActionListener( x -> {
            mainPanel.remove(colorPickerPanel);
            mainPanel.revalidate();
            mainPanel.repaint();
        });
        showValues.addActionListener( x ->  {
            if((int)widthPicker.getValue() < 1
                    || (int)widthPicker.getValue() > panelNorth.loadImage.wtfImage.width()
                    || (int)heightPicker.getValue() < 1
                    || (int)heightPicker.getValue() > panelNorth.loadImage.wtfImage.height()) {
                JOptionPane optionPane = new JOptionPane("The width values have to be between 1 and" + panelNorth.loadImage.wtfImage.width()
                        + "\nThe height values have to be between 1 and" + panelNorth.loadImage.wtfImage.height(), JOptionPane.INFORMATION_MESSAGE);
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

            EditablePixel pixel = panelNorth.loadImage.editableWtfImage.at((int) widthPicker.getValue()-1, (int )heightPicker.getValue()-1);
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

    //-------------------Hilfefunktionen für NullWerte im Channels----------------//
    //manche Pixel können null Werte im ColorChannel haben - Probleme mit Java-Image dann beim Darstellen
    //Funktion sucht null Werte und ersetzt sie mit sinnevolle Deafult Werte
    private void ensureHSVChannels(Map<ColorChannel, Short> values) {
        // Suche nach HSV-Kanälen (DynamicColorChannel) welche gibt es überhaupt
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

        // Default-Werte setzen falls Kanäle fehlen
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

    //----------------- Hilfsmethode für Panel-Management------------------//
    private void showPanelInEast(JPanel panel) {
        if (mainPanel.getLayout() instanceof BorderLayout) {
            BorderLayout layout = (BorderLayout) mainPanel.getLayout();
            Component oldEast = layout.getLayoutComponent(BorderLayout.EAST);
            if (oldEast != null) {
                mainPanel.remove(oldEast);
            }
        }

        mainPanel.add(panel, BorderLayout.EAST);
        mainPanel.revalidate();
        mainPanel.repaint();
    }

    //------------------Hilfsmethode um Buttons zu erstellen----------------------//
    private JButton createTransformationButton(TransformationType transformation) {
        String text;
        switch (transformation) {
            case ROTATE_CLOCKWISE:
                text = "↷ Rechts";
                break;
            case ROTATE_COUNTERCLOCKWISE:
                text = "↶ Links";
                break;
            case FLIP_HORIZONTAL:
                text = "↔ Horizontal";
                break;
            case FLIP_VERTICAL:
                text = "↕ Vertikal";
                break;
            default:
                text = transformation.toString();
        }

        JButton button = new JButton(text);
        button.addActionListener(e -> transform(transformation));
        return button;
    }

    private void closePanelInEast() {
        if (mainPanel.getLayout() instanceof BorderLayout) {
            BorderLayout layout = (BorderLayout) mainPanel.getLayout();
            Component eastPanel = layout.getLayoutComponent(BorderLayout.EAST);
            if (eastPanel != null) {
                mainPanel.remove(eastPanel);
                mainPanel.revalidate();
                mainPanel.repaint();
            }
        }
    }




}
