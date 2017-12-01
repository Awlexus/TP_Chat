package gui;

import javax.swing.*;

/**
 * @author Matteo Cosi
 * @since 01.12.2017
 */
public class SendEvent {
    private JButton source;
    private Message message;

    public SendEvent(JButton source, Message message) {
        this.source = source;
        this.message = message;
    }

    public JButton getSource() {
        return source;
    }

    public Message getMessage() {
        return message;
    }
}
