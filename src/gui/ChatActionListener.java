package gui;

/**
 * @author Matteo Cosi
 * @since 01.12.2017
 */
interface ChatActionListener {
    void onSendPressed(SendEvent e);
    void onEditTextChanged(TextChangedEvent e);
}
