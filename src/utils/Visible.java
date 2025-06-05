package utils;

import javax.swing.*;

public class Visible {

    public static void setVisible(JComponent...components) {
        for(JComponent c : components) {
            if(c != null ) {
                c.setVisible(true);
            }
        }
    }
    public static void setInvisible(JComponent...components) {
        for(JComponent c : components) {
            if(c != null ) {
                c.setVisible(false);
            }
        }
    }
}
