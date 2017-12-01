package gui;

import javax.swing.*;

/**
 * @author Matteo Cosi
 * @since 01.12.2017
 */
public class TextChangedEvent {
    private JTextField source;

    public TextChangedEvent(JTextField source) {
        this.source = source;
    }

    public JTextField getSource() {
        return source;
    }

    public String getText() {
        return source.getText();
    }
}
