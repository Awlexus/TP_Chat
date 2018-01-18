package gui;

import javax.swing.*;

/**
 * @author Matteo Cosi
 * @since 17.01.2018
 */
public class SendEvent {
    String message;
    JComponent source;
    JTextField textField;

    public SendEvent( JComponent source,String message, JTextField textField) {
        this.message = message;
        this.source = source;
        this.textField = textField;
    }

    public String getMessage() {
        return message;
    }

    public JComponent getSource() {
        return source;
    }

    public JTextField getTextField() {
        return textField;
    }
}
