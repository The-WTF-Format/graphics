package utils;

import javax.swing.*;

/**
 * Manages the visibility of all the swing components
 */
public class Visible {
    /**
     * set different swing components to visible
     * @param components vararg of different swing components
     */

    public static void setVisible(JComponent...components) {
        for(JComponent c : components) {
            if(c != null ) {
                c.setVisible(true);
            }
        }
    }
    /**
     * set different swing components to invisible
     * @param components vararg of different swing components
     */
    public static void setInvisible(JComponent...components) {
        for(JComponent c : components) {
            if(c != null ) {
                c.setVisible(false);
            }
        }
    }
}
