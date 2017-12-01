package gui;

import javax.swing.*;

/**
 * @author Matteo Cosi
 * @since 30.11.2017
 */
public class Chat extends JPanel {

    /**
     * SCREEN_SPLITING_RATIO tells how to split the screen.
     *
     * @example 0.15 means 15% of the height is reserved for the
     * ChatControls and 85% is the height of the actual ChatContent
     */
    public static final double SCREEN_SPLITING_RATIO = 0.15;

    ChatContent chatContent;
    ChatControls chatControls;

    public Chat() {
        setBackground(MainWindow.theme.getPrimaryColorLight());

    }

    public void setupUI() {

        chatContent = new ChatContent(getWidth(), (int) (getHeight() * (1 - SCREEN_SPLITING_RATIO)));
        chatContent.setLocation(0, 0);

        chatControls = new ChatControls(getWidth(), getHeight() - chatContent.height);
        chatControls.setLocation(0, chatContent.height);

        add(chatContent);
        add(chatControls);
    }


    private class ChatControls extends JPanel {
        int width;
        int height;

        public ChatControls(int width, int height) {
            this.width = width;
            this.height = height;
            this.setSize(width, height);
            setBorder(BorderFactory.createMatteBorder(
                    (int) MainWindow.UI_SCALING, (int) MainWindow.UI_SCALING, (int) MainWindow.UI_SCALING, (int) MainWindow.UI_SCALING, MainWindow.theme.getPrimaryColorDark()));

        }

    }

    private class ChatContent extends JPanel {
        int width;
        int height;

        public ChatContent(int width, int height) {
            this.width = width;
            this.height = height;
            this.setSize(width, height);
            setBorder(BorderFactory.createMatteBorder(
                    (int) MainWindow.UI_SCALING, (int) MainWindow.UI_SCALING, (int) MainWindow.UI_SCALING, (int) MainWindow.UI_SCALING, MainWindow.theme.getPrimaryColorDark()));
        }
    }
}
