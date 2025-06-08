package editorview;

import javax.swing.*;
import java.util.function.BiConsumer;

import panel.CreatePanel;
import utils.Colors;
import utils.Size;

/**
 * creats a new menubar for the image editing function
 * it groups the function together in animation, color, general and format
 * each item is linked to its action via an actionrouter
 */

public class FunctionMenu {
    JMenuBar menu;
    static CreatePanel mainPanel;
    static PanelNorth panelNorth;
    public JMenu animationMenu;
    public JMenu colorMenu;
    public JMenu generalMenu;
    public JMenu converterMenu;

    FunctionMenu(JMenuBar menu, CreatePanel mainPanel, PanelNorth panelNorth) {
        this.menu = menu;
        this.mainPanel = mainPanel;
        this.panelNorth = panelNorth;
        addMenus();
       }

    /**
     * Adds all top-level menus in order
     */
    void addMenus() {
        addAnimationMenu();
        addColorMenu();
        addGeneral();
        addConverter();
    }

    /**
     * Menus
     */

    /**
     * Creates the "Animation" menu with its related functions.
     */
    private void addAnimationMenu() {
        animationMenu = new JMenu("Animation");
        animationMenu.setPreferredSize(Size.BUTTONSIZEMAINMENU);
        animationMenu.setMinimumSize(Size.BUTTONSIZEMAINMENU);
        animationMenu.setMaximumSize(Size.BUTTONSIZEMAINMENU);
        addMenuItem(animationMenu,  ActionRouter.createFunctionMenuActionRouter(this), "Seconds per frame");
        addMenuItem(animationMenu,  ActionRouter.createFunctionMenuActionRouter(this), "Frames per second");

        menu.add(animationMenu);
    }

    /**
     * Creates the "Color" menu with color manipulation options.
     */
    private void addColorMenu() {
        colorMenu = new JMenu("Color");
        colorMenu.setPreferredSize(Size.BUTTONSIZEMAINMENU);
        colorMenu.setMinimumSize(Size.BUTTONSIZEMAINMENU);
        colorMenu.setMaximumSize(Size.BUTTONSIZEMAINMENU);
        //for example
        /*colorMenu.add(createSubMenu("Color Picker", ActionRouter.createFunctionMenuActionRouter(this),"Farbname & Hexadezimalcode"));
        colorMenu.add(createSubMenu("Invert Color", ActionRouter.createFunctionMenuActionRouter(this),"Komplementärfarbe"));
        colorMenu.add(createSubMenu("Color Space", ActionRouter.createFunctionMenuActionRouter(this),"Select color space"));
        */
        addMenuItem(colorMenu, ActionRouter.createFunctionMenuActionRouter(this), "Color Picker");
        addMenuItem(colorMenu, ActionRouter.createFunctionMenuActionRouter(this), "Invert Color");
        addMenuItem(colorMenu, ActionRouter.createFunctionMenuActionRouter(this), "Color Space");
        menu.add(colorMenu);
    }

    /**
     * Creates the "General" menu with its related functions.
     */
    private void addGeneral() {
        generalMenu = new JMenu("General");
        generalMenu.setPreferredSize(Size.BUTTONSIZEMAINMENU);
        generalMenu.setMinimumSize(Size.BUTTONSIZEMAINMENU);
        generalMenu.setMaximumSize(Size.BUTTONSIZEMAINMENU);

        addMenuItem(generalMenu,  ActionRouter.createFunctionMenuActionRouter(this), "Height and width");
        addMenuItem(generalMenu, ActionRouter.createFunctionMenuActionRouter(this), "Mirror...");
        addMenuItem(generalMenu,  ActionRouter.createFunctionMenuActionRouter(this), "Rotate...");

        menu.add(generalMenu);
    }

    /**
     * Creates the "Format" menu for image format conversion.
     */
    private void addConverter() {
        converterMenu = new JMenu("Format");
        converterMenu.setPreferredSize(Size.BUTTONSIZEMAINMENU);
        converterMenu.setMinimumSize(Size.BUTTONSIZEMAINMENU);
        converterMenu.setMaximumSize(Size.BUTTONSIZEMAINMENU);
        addMenuItem(converterMenu,  ActionRouter.createFunctionMenuActionRouter(this), "JPEG");
        addMenuItem(converterMenu,  ActionRouter.createFunctionMenuActionRouter(this), "PNG");

        menu.add(converterMenu);
    }

    /**
     * Internal helper class to assign actions to menu items using a BiConsumer.
     */
    public static class ActionRouter {
        /**
         * Creates an action router that assigns actions to each menu item.
         *
         * @param functionMenu The parent FunctionMenu instance
         * @return BiConsumer assigning JMenuItem actions based on their name
         */
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
                            System.out.println("Unknown Function: " + name);
                    }
                });
            };
        }
    }

    /**
     * Privat Functions
     */

    /**
     * Adds a menu item to the given menu as assigns it an action
     * @param menu
     * @param actionAssigner
     * @param text
     * @return
     */
    private JMenuItem addMenuItem(JMenu menu, BiConsumer<JMenuItem, String> actionAssigner, String text) {
        JMenuItem item = new JMenuItem(text);
        item.setBackground(Colors.ITEMSSECONDARY);
        menu.add(item);
        actionAssigner.accept(item, text);
        return item;
    }

}
