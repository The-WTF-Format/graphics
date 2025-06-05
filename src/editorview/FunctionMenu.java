package editorview;

import javax.swing.*;
import java.util.function.BiConsumer;

import panel.CreatePanel;
import utils.Colors;
import utils.Size;

//ähnlich aufgebaut wie Image.image.CreateNewImage.LoadImage
//Hier wird ein neues Panel mit einer editorview.MenuBar darin erstellt, in der editorview.MenuBar sind 4 Gruppen und jeweiligen Untergruppen, die die Funktion des Editors beschreiben

public class FunctionMenu {
    JMenuBar menu;
    CreatePanel panel;
    static JPanel mainPanel;
    static PanelNorth panelNorth;
    //TODO: HInweis:
    //Um die verschiedenen Menus auf Invisible zu stellen benötigen wir globale Variablen, alternativ können mir auch ein JMenu Array erstellen, wenn dir das lieber ist
    public JMenu animationMenu;
    public JMenu colorMenu;
    public JMenu generalMenu;
    public JMenu converterMenu;

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
    void addMenus() {
        addAnimationMenu();
        addColorMenu();
        addGeneral();
        addConverter();
    }

    // MENUS //
    //einzelne Menus werden erstellt - erstellen der Button und DropDowns sind in Methoden ausgelagert worden

    //Animation
    private void addAnimationMenu() {
        animationMenu = new JMenu("Animation");
        animationMenu.setPreferredSize(Size.BUTTONSIZEMAINMENU);
        animationMenu.setMinimumSize(Size.BUTTONSIZEMAINMENU);
        animationMenu.setMaximumSize(Size.BUTTONSIZEMAINMENU);
        addMenuItem(animationMenu,  ActionRouter.createFunctionMenuActionRouter(this), "Seconds per frame");
        addMenuItem(animationMenu,  ActionRouter.createFunctionMenuActionRouter(this), "Frames per second");

        menu.add(animationMenu);
    }

    //Color
    private void addColorMenu() {
        colorMenu = new JMenu("Color");
        colorMenu.setPreferredSize(Size.BUTTONSIZEMAINMENU);
        colorMenu.setMinimumSize(Size.BUTTONSIZEMAINMENU);
        colorMenu.setMaximumSize(Size.BUTTONSIZEMAINMENU);
        //z.B.
        /*colorMenu.add(createSubMenu("Color Picker", ActionRouter.createFunctionMenuActionRouter(this),"Farbname & Hexadezimalcode"));
        colorMenu.add(createSubMenu("Invert Color", ActionRouter.createFunctionMenuActionRouter(this),"Komplementärfarbe"));
        colorMenu.add(createSubMenu("Color Space", ActionRouter.createFunctionMenuActionRouter(this),"Select color space"));
        */
        addMenuItem(colorMenu, ActionRouter.createFunctionMenuActionRouter(this), "Color Picker");
        addMenuItem(colorMenu, ActionRouter.createFunctionMenuActionRouter(this), "Invert Color");
        addMenuItem(colorMenu, ActionRouter.createFunctionMenuActionRouter(this), "Color Space");
        menu.add(colorMenu);
    }

    //General
    private void addGeneral() {
        generalMenu = new JMenu("General");
        generalMenu.setPreferredSize(Size.BUTTONSIZEMAINMENU);
        generalMenu.setMinimumSize(Size.BUTTONSIZEMAINMENU);
        generalMenu.setMaximumSize(Size.BUTTONSIZEMAINMENU);

        addMenuItem(generalMenu,  ActionRouter.createFunctionMenuActionRouter(this), "Height and width");
        // Code probieren obs klappt
        /*JMenuItem height = addMenuItem(generalMenu, "Höhe und Größe ändern");
        height.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                changeWTFImageHeight();
                repaintImage();
            }
        });*/
        addMenuItem(generalMenu, ActionRouter.createFunctionMenuActionRouter(this), "Mirror...");
        addMenuItem(generalMenu,  ActionRouter.createFunctionMenuActionRouter(this), "Rotate...");

        menu.add(generalMenu);
    }

    //Converter
    private void addConverter() {
        converterMenu = new JMenu("Format");
        // todo change one "General" to another String - this one could be called "format"
        //done
        converterMenu.setPreferredSize(Size.BUTTONSIZEMAINMENU);
        converterMenu.setMinimumSize(Size.BUTTONSIZEMAINMENU);
        converterMenu.setMaximumSize(Size.BUTTONSIZEMAINMENU);
        addMenuItem(converterMenu,  ActionRouter.createFunctionMenuActionRouter(this), "JPEG");
        addMenuItem(converterMenu,  ActionRouter.createFunctionMenuActionRouter(this), "PNG");

        menu.add(converterMenu);
    }

    //ActionRouter, der die Funktionen zu den Buttons zuweist
    public static class ActionRouter {
        //BiConsumer nimmt zwei Elemente entgegen und gibt keinen Rückgabe wert
        //das Menüelement, was ausgewählt wird
        //String, wie die Methode heißt
        public static BiConsumer<JMenuItem, String> createFunctionMenuActionRouter(FunctionMenu functionMenu) {
            ImageFunction imageFunction = new ImageFunction(panelNorth, mainPanel);
            return (item, name) -> {
                item.addActionListener(e -> {
                    if(panelNorth.loadImage.wtfImage == null && panelNorth.loadImage.editableWtfImage == null) {
                        //check if there is a loaded image visible and open pop-up to avoid Nullpointer
                        JOptionPane optionPane = new JOptionPane("There is no WTF-Image you can edit!", JOptionPane.INFORMATION_MESSAGE);
                        JDialog dialog = optionPane.createDialog("Note");
                        dialog.setSize(450, 120);
                        dialog.setLocationRelativeTo(null);
                        dialog.setVisible(true);
                        return;
                    }
                    switch (name) {
                        case "Color Space":
                            imageFunction.colorSpaceSelection();
                            break;
                        case "Height and width":
                            imageFunction.setEditableHeightWidth();
//                            imageFunction.changeWTFImageHeight(10);
//                            imageFunction.changeWTFImageWidth(10);
                            break;
                        case "Rotate...":
                            imageFunction.rotateEditable();
                            break;
                        case "Seconds per frame":
                            imageFunction.secondsPerFrames();
                            break;
                        case "Frames per second":
                            imageFunction.framesPerSeconds();
                            break;
                        case "Invert Color":
                            imageFunction.invertColorPopUp();
                            break;
                        case "Color Picker":
                            imageFunction.colorPicker();
                            break;
                        case "Mirror...":
                            imageFunction.mirrorEditable();
                            break;
                        case "JPEG":
                            imageFunction.converter("JPEG");
                            break;
                        case "PNG":
                            imageFunction.converter("PNG");
                            break;
                        default:
                            System.out.println("Unbekannte Aktion: " + name);
                    }
                });
            };
        }
    }

    // PRIVAT FUNCTION//
    //Hauptgruppe erstellen
    private JMenuItem addMenuItem(JMenu menu, BiConsumer<JMenuItem, String> actionAssigner, String text) {
        JMenuItem item = new JMenuItem(text);
        //todo möglichkeit von erstellen einer MenuStyler - verschiedene Gruppen mit verschiedenen Aussehen?
        item.setBackground(Colors.ITEMSSECONDARY);
        menu.add(item);
        //Action werden zugewiesen
        actionAssigner.accept(item, text);
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

}
