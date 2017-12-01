package gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

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

    private ChatContent chatContent;
    private ChatControls chatControls;

    private ArrayList<ChatActionListener> chatActionListeners;


    public Chat() {
        setBackground(MainWindow.theme.getPrimaryColorLight());
        chatActionListeners = new ArrayList<>();

    }

    public void setupUI() {

        chatContent = new ChatContent(getWidth(), (int) (getHeight() * (1 - SCREEN_SPLITING_RATIO)));
        chatContent.setLocation(0, 0);

        chatControls = new ChatControls(getWidth(), getHeight() - chatContent.height);
        chatControls.setLocation(0, chatContent.height);

        add(chatContent);
        add(chatControls);
    }

    public void addChatActionListener(ChatActionListener chatActionListener) {
        this.chatActionListeners.add(chatActionListener);
    }

    public void removeChatActionListener(ChatActionListener chatActionListener) {
        this.chatActionListeners.remove(chatActionListener);
    }


    private class ChatControls extends JPanel {
        int width;
        int height;

        JTextField textField;
        JButton send;

        public ChatControls(int width, int height) {
            this.setLayout(null);
            this.width = width;
            this.height = height;
            this.setSize(width, height);
            setBackground(MainWindow.theme.getPrimaryColorLight());
            setBorder(BorderFactory.createMatteBorder(
                    (int) MainWindow.UI_SCALING, (int) MainWindow.UI_SCALING, (int) MainWindow.UI_SCALING, (int) MainWindow.UI_SCALING, MainWindow.theme.getPrimaryColorDark()));


            textField = new JTextField();
            textField.setFont(new Font(MainWindow.FONT, 0, (int) (this.height * 4 / 5 - MainWindow.UI_SCALING * 6)));
            textField.setSize((int) (width * 4 / 6 - MainWindow.UI_SCALING * 6), textField.getPreferredSize().height);
            textField.setLocation((int) MainWindow.UI_SCALING * 3, height / 2 - textField.getHeight() / 2);
            textField.setBorder(BorderFactory.createMatteBorder((int) MainWindow.UI_SCALING, (int) MainWindow.UI_SCALING, (int) MainWindow.UI_SCALING, (int) MainWindow.UI_SCALING, MainWindow.theme.getPrimaryColorDark()));
            textField.setBackground(MainWindow.theme.getPrimaryColorLight());
            textField.setForeground(Color.BLACK);
            textField.addKeyListener(new KeyAdapter() {
                @Override
                public void keyReleased(KeyEvent e) {
                    for (int i = 0; i < chatActionListeners.size(); i++) {
                        chatActionListeners.get(i).onEditTextChanged(new TextChangedEvent(textField));
                    }
                }
            });


            send = new JButton("SENDE");
            send.setFont(new Font(MainWindow.FONT, 0, (int) (this.height * 3 / 5 - MainWindow.UI_SCALING * 8)));
            send.setSize((int) (width * 2 / 6 - MainWindow.UI_SCALING * 6), textField.getHeight());
            send.setLocation((int) (textField.getWidth() + MainWindow.UI_SCALING * 6), height / 2 - textField.getHeight() / 2);
            send.setBackground(MainWindow.theme.getPrimaryColorDark());
            if (MainWindow.theme.getDark())
                send.setForeground(Color.white);
            else
                send.setForeground(Color.black);

            send.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    for (int i = 0; i < chatActionListeners.size(); i++) {
                        chatActionListeners.get(i).onSendPressed(new SendEvent(send, new Message(textField.getText())));
                    }
                }

                @Override
                public void mousePressed(MouseEvent e) {
                    //TODO find a way to controll the button color on press
                }

                @Override
                public void mouseEntered(MouseEvent e) {
                    send.setForeground(MainWindow.theme.getAccentColor());
                }

                @Override
                public void mouseExited(MouseEvent e) {
                    if (MainWindow.theme.getDark())
                        send.setForeground(Color.white);
                    else
                        send.setForeground(Color.black);
                }
            });


            this.add(textField);
            this.add(send);
        }

    }

    private class ChatContent extends JPanel {
        int width;
        int height;

        public ChatContent(int width, int height) {
            this.setLayout(null);
            this.width = width;
            this.height = height;
            this.setSize(width, height);
            setBackground(MainWindow.theme.getPrimaryColorLight());
            setBorder(BorderFactory.createMatteBorder(
                    (int) MainWindow.UI_SCALING, (int) MainWindow.UI_SCALING, (int) MainWindow.UI_SCALING, (int) MainWindow.UI_SCALING, MainWindow.theme.getPrimaryColorDark()));
        }
    }
}
