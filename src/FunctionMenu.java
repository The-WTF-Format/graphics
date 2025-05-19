import javax.swing.*;

//ähnlich aufgebaut wie LoadImage
//Hier wird ein neues Panel mit einer MenuBar darin erstellt, in der MenuBar sind 4 Gruppen und jeweiligen Untergruppen, die die Funktion des Editors beschreiben

public class FunctionMenu {
    JMenuBar menu;
    CreatePanel panel;

    //Konstruktor
    FunctionMenu(JMenuBar menu, CreatePanel panel) {
        this.menu = menu;
        this.panel = panel;
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

        colorMenu.add(createSubMenu("Color Spaces", "Farbräume auswählen", "Transparenz"));
        colorMenu.add(createSubMenu("Color Picker", "Farbname & Hexadezimalcode"));
        colorMenu.add(createSubMenu("Farbe invertieren", "Komplementärfarbe"));

        menu.add(colorMenu);
    }

    //General
    private void addGeneral() {
        JMenu generalMenu = new JMenu("General");
        generalMenu.setPreferredSize(Size.BUTTONSIZEMAINMENU);
        generalMenu.setMinimumSize(Size.BUTTONSIZEMAINMENU);
        generalMenu.setMaximumSize(Size.BUTTONSIZEMAINMENU);

        addMenuItem(generalMenu, "Höhe und Größe ändern");
        addMenuItem(generalMenu, "Spiegeln");
        addMenuItem(generalMenu, "Drehen");

        menu.add(generalMenu);
    }

    //Converter
    private void addConverter() {
        JMenu converterMenu = new JMenu("General");
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
    private JMenu createSubMenu(String name, String... items) {
        JMenu subMenu = new JMenu(name);
        for (String itemName : items) {
            JMenuItem item = new JMenuItem(itemName);
            item.setBackground(Colors.ITEMSSECONDARY);
            //todo möglichkeit von erstellen einer MenuStyler - verschiedene Gruppen mit verschiedenen Aussehen?
            subMenu.add(item);
            //todo Method erstellen, wo action via name zugeordnert werden?!
        }
        return subMenu;
    }
}
