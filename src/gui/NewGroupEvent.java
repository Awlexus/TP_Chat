package gui;

import javax.swing.*;

/**
 * @author Matteo Cosi
 * @since 25.01.2018
 */
public class NewGroupEvent {
    String names;
    JTextField source;

    public NewGroupEvent(String names, JTextField source) {
        this.names = names;
        this.source = source;
    }

    public String getNames() {
        return names;
    }

    public JTextField getSource() {
        return source;
    }
}
