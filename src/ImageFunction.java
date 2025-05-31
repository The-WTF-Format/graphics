import values.Colors;
import wtf.file.api.editable.EditableWtfImage;
import wtf.file.api.editable.data.EditablePixel;

import javax.swing.*;
import java.awt.*;


public class ImageFunction {

    private final PanelNorth panelNorth;
    private EditableWtfImage editable;
    JPanel mainPanel;

    public ImageFunction(PanelNorth panelNorth) {
        this.panelNorth = panelNorth;
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
        okButton.addActionListener(e -> {
            //Abfrage ob editable bereits vorhanden, sonst anlegen
            if(panelNorth.loadImage.editableWtfImage == null) {
                panelNorth.loadImage.editableWtfImage = panelNorth.loadImage.wtfImage.edit();
            }

            try {
                int height = Integer.parseInt(heightField.getText().trim());
                int width = Integer.parseInt(widthField.getText().trim());

                //Methode aufrufen
                //changeWTFImageWidth(width);
                changeWTFImageHeight(height);

                dialog.dispose();
            } catch (NumberFormatException ex) {
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
        CreatePanel gridPanel = new CreatePanel();
        gridPanel.setBackground(Colors.MAKERSPACEBACKGROUND);

        JButton rotateLeftButton = new JButton("↶ Links");
        JButton rotateRightButton = new JButton("↷ Rechts");

        rotateLeftButton.addActionListener(e -> {
            rotateLeft();
        });
        rotateRightButton.addActionListener(e -> {
            rotateRight();
        });
        //mainPanel.add(gridPanel, BorderLayout.EAST);


    }

    protected void colorSpaceSelection() {
        // TODO Beim Aufruf der Methode muss das Panel als PopUp dargestellt werden
        //DONE JE
        // Neues modales Pop-up (Dialogfenster)
        JDialog dialog = new JDialog((Frame) null, "Farbraum auswählen", true);
        dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);

        // Hinweis: habe den Namen deines loaklen Panels verändert, da dieser das globale Panel überschreibt.
        CreatePanel gridPanel = new CreatePanel();
        gridPanel.setBackground(Colors.MAKERSPACEBACKGROUND);
        gridPanel.setLayout(new GridLayout(5,3));
        // TODO anderer Layouttyp wäre vermütlich sinnvoller (z.B. Borderlayout und dann alle im selben Bereich hinzufügen),
        //  da auf diese Art die komplette Fläche genutzt wird und sehr "packed" ausschaut.
        ButtonGroup group = new ButtonGroup();
        String [] names = new String[15];
        JRadioButton [] buttons = new JRadioButton[15];
        // TODO Namen von der Enumklasse extrahieren und dann mit einer Schleife zuweisen
        names[0] = "RGB";
        names[1] = "RGBa";
        names[2] = "DYNAMIC_RGBa";
        names[3] = "GRAY_SCALE";
        names[4] = "GRAY_SCALE_A";
        names[5] = "DYNAMIC_GRAY_SCALE_A";
        names[6] = "CMY";
        names[7] = "CMYa";
        names[8] = "DYNAMIC_CMYa";
        names[9] = "HSV";
        names[10] = "HSVa";
        names[11] = "DYNAMIC_HSVa";
        names[12] = "YCbCr";
        names[13] = "YCbCra";
        names[14] = "DYNAMIC_YCbCra";
        for(int i = 0; i < 15; i++){
            buttons[i] = new JRadioButton(names[i]);
            buttons[i].setMaximumSize(new Dimension(80, 30));
            buttons[i].setMaximumSize(new Dimension(80, 30));
            buttons[i].setPreferredSize(new Dimension(80, 30));
            group.add(buttons[i]);
            gridPanel.add(buttons[i]);
        }
        //mainPanel.add(gridPanel, BorderLayout.EAST);
        dialog.getContentPane().add(gridPanel);
        dialog.pack();
        dialog.setLocationRelativeTo(null);
        dialog.setVisible(true);

    }

    //private Methods
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
        System.out.println("Neue Höhe: " + editable.height());
        try {
            //Neues Bild muss ins Panel geladen werden
            panelNorth.loadImage.showImage();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    protected void rotateRight() {
        int width = editable.width();
        int height = editable.height();
        EditablePixel[][] original = editable.pixels();
        System.out.println(original);
    }

    protected void rotateLeft() {
        int width = editable.width();
        int height = editable.height();
        EditablePixel[][] original = editable.pixels();
        System.out.println(original);
    }


}
