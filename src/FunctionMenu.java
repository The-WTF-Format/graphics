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
import java.util.function.BiConsumer;

import image.BufferedImagePanel;
import image.ImagePanel;
import values.Colors;
import values.Size;
import wtf.file.api.WtfImage;
import wtf.file.api.color.ColorSpace;
import wtf.file.api.editable.EditableWtfImage;

//ähnlich aufgebaut wie LoadImage
//Hier wird ein neues Panel mit einer MenuBar darin erstellt, in der MenuBar sind 4 Gruppen und jeweiligen Untergruppen, die die Funktion des Editors beschreiben

public class FunctionMenu {
    JMenuBar menu;
    CreatePanel panel;
    JPanel mainPanel;
    PanelNorth panelNorth;
    //Konstruktor
    FunctionMenu(JMenuBar menu, CreatePanel panel, JPanel mainPanel, PanelNorth panelNorth) {
        this.menu = menu;
        this.panel = panel;
        this.mainPanel = mainPanel;
        this.panelNorth = panelNorth;
        addMenus();
    }

    // GENERAL //
    //addMenus - hier kann die Reihenfolge noch geändert werden
    private void addMenus() {
        addAnimationMenu();
        addColorMenu();
        addGeneral();
        addConverter();
    }

    // MENUS //
    //einzelne Menus werden erstellt - erstellen der Button und DropDowns sind in Methoden ausgelagert worden

    //Animation
    private void addAnimationMenu() {
        JMenu animationMenu = new JMenu("Animation");
        animationMenu.setPreferredSize(Size.BUTTONSIZEMAINMENU);
        animationMenu.setMinimumSize(Size.BUTTONSIZEMAINMENU);
        animationMenu.setMaximumSize(Size.BUTTONSIZEMAINMENU);

        addMenuItem(animationMenu, "Frames einstellen");
        addMenuItem(animationMenu, "Frames per second einstellen");

        menu.add(animationMenu);
    }

    //Color
    private void addColorMenu() {
        JMenu colorMenu = new JMenu("Color");
        colorMenu.setPreferredSize(Size.BUTTONSIZEMAINMENU);
        colorMenu.setMinimumSize(Size.BUTTONSIZEMAINMENU);
        colorMenu.setMaximumSize(Size.BUTTONSIZEMAINMENU);
        // todo wir benötigen diese Components jeweils als eigenständige Objekte mit Zugriff
        //z.B.
        colorMenu.add(createSubMenu("Color Picker", ActionRouter.createFunctionMenuActionRouter(this),"Farbname & Hexadezimalcode"));
        colorMenu.add(createSubMenu("Farbe invertieren", ActionRouter.createFunctionMenuActionRouter(this),"Komplementärfarbe"));
        colorMenu.add(createSubMenu("Color Space", ActionRouter.createFunctionMenuActionRouter(this),"Select color space"));

        menu.add(colorMenu);
    }

    //General
    private void addGeneral() {
        JMenu generalMenu = new JMenu("General");
        generalMenu.setPreferredSize(Size.BUTTONSIZEMAINMENU);
        generalMenu.setMinimumSize(Size.BUTTONSIZEMAINMENU);
        generalMenu.setMaximumSize(Size.BUTTONSIZEMAINMENU);

        // todo change it back: just trying
        //addMenuItem(generalMenu, "Höhe und Größe ändern");
        JMenuItem height = addMenuItem(generalMenu, "Höhe und Größe ändern");
        height.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                changeWTFImageHeight();
                repaintImage();
            }
        });
        addMenuItem(generalMenu, "Spiegeln");
        addMenuItem(generalMenu, "Drehen");

        menu.add(generalMenu);
    }

    //Converter
    private void addConverter() {
        JMenu converterMenu = new JMenu("Format");
        // todo change one "General" to another String - this one could be called "format"
        //done
        converterMenu.setPreferredSize(Size.BUTTONSIZEMAINMENU);
        converterMenu.setMinimumSize(Size.BUTTONSIZEMAINMENU);
        converterMenu.setMaximumSize(Size.BUTTONSIZEMAINMENU);
        addMenuItem(converterMenu, "GIF");
        addMenuItem(converterMenu, "JPEG");
        addMenuItem(converterMenu, "PNG");

        menu.add(converterMenu);
    }

    // PRIVAT FUNCTION
    //Hauptgruppe erstellen
    private JMenuItem addMenuItem(JMenu menu, String text) {
        JMenuItem item = new JMenuItem(text);
        //todo möglichkeit von erstellen einer MenuStyler - verschiedene Gruppen mit verschiedenen Aussehen?
        item.setBackground(Colors.ITEMSSECONDARY);
        menu.add(item);
        return item;
    }

    //Submenu erstellen
    private JMenu createSubMenu(String name, BiConsumer<JMenuItem, String> actionAssigner, String... items) {
        JMenu subMenu = new JMenu(name);
        for (String itemName : items) {
            JMenuItem item = new JMenuItem(itemName);
            item.setBackground(Colors.ITEMSSECONDARY);
            //todo möglichkeit von erstellen einer MenuStyler - verschiedene Gruppen mit verschiedenen Aussehen?
            subMenu.add(item);
            //Action werden zugewiesen
            actionAssigner.accept(item, itemName);
        }
        return subMenu;
    }

    public static class ActionRouter {
        //BiConsumer nimmt zwei Elemente entgegen und gibt keinen Rückgabe wert
        //das Menüelement, was ausgewählt wird
        //String, wie die Methode heißt

        public static BiConsumer<JMenuItem, String> createFunctionMenuActionRouter(FunctionMenu functionMenu) {
            return (item, name) -> {
                item.addActionListener(e -> {
                    switch (name) {
                        case "Select color space":
                            functionMenu.colorSpaceSelection();
                            break;
                        default:
                            System.out.println("Unbekannte Aktion: " + name);
                    }
                });
            };
        }
    }

    private void colorSpaceSelection() {
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
    boolean hasExistingImage() {
        if(panelNorth.loadImage.imagePanel== null) {
            return false;
        }
        return true;
    }
    void repaintImage() {
        panelNorth.loadImage.panel.revalidate();
        panelNorth.loadImage.panel.repaint();
    }

    //Todo:
    //Hinweise:
    //How to change WTFImage:
    //z.B. Change Height
    void changeWTFImageHeight() {
        panelNorth.loadImage.getEditableWtfImage().edit().setHeight(10);
        panelNorth.loadImage.setImagePanel(panelNorth.loadImage.getEditableWtfImage());
    }

}
