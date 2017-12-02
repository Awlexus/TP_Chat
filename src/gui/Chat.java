package gui;

import com.sun.deploy.util.ArrayUtil;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import javax.swing.border.AbstractBorder;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.Rectangle2D;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;

import static gui.MainWindow.UI_SCALING;

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

    /**
     * describes the direction of the message
     * FROM...an other user is the autor
     * TO... I am the autor
     * INFO...a text for information purposes only
     */
    public enum chatMessageType {
        FROM, TO, INFO
    }

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

    /**
     * adds a new message at the bottom of the screen
     *
     * @param name    name of the user that writes the message
     * @param message content of the message
     * @param date    optional timestamp
     * @param type    type of the message
     */
    public void addMessage(chatMessageType type, String name, Message message, @Nullable String date) {
        chatContent.addChatMessage(type, name, message, date);
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
                    (int) UI_SCALING,0 , (int) UI_SCALING, (int) UI_SCALING, MainWindow.theme.getPrimaryColorDark()));


            textField = new JTextField();
            textField.setFont(new Font(MainWindow.FONT, 0, (int) (this.height * 4 / 5 - UI_SCALING * 6)));
            textField.setSize((int) (width * 4 / 6 - UI_SCALING * 6), textField.getPreferredSize().height);
            textField.setLocation((int) UI_SCALING * 3, height / 2 - textField.getHeight() / 2);
            textField.setBorder(BorderFactory.createMatteBorder((int) UI_SCALING, (int) UI_SCALING, (int) UI_SCALING, (int) UI_SCALING, MainWindow.theme.getPrimaryColorDark()));
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
            send.setFont(new Font(MainWindow.FONT, 0, (int) (this.height * 3 / 5 - UI_SCALING * 8)));
            send.setSize((int) (width * 2 / 6 - UI_SCALING * 6), textField.getHeight());
            send.setLocation((int) (textField.getWidth() + UI_SCALING * 6), height / 2 - textField.getHeight() / 2);
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
                    //TODO 6 find a way to controll the button color on press
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


        ArrayList<ChatMessage> chatMessages;

        public ChatContent(int width, int height) {
            this.setLayout(null);
            this.width = width;
            this.height = height;
            this.setSize(width, height);
            setBackground(MainWindow.theme.getPrimaryColorLight());
            chatMessages = new ArrayList<>();

        }

        /**
         * adds a new message at the bottom of the screen
         *
         * @param name    name of the user that writes the message
         * @param message content of the message
         * @param date    optional timestamp
         * @param type    type of the message
         */
        public void addChatMessage(chatMessageType type, String name, Message message, @Nullable String date) {
            ChatMessage chatMessage = new ChatMessage(type, name, message, date);
            chatMessages.add(0, chatMessage);
            //TODO 4 add scroll listener (like the contacts) to move the chat up and down
            repaintChatContent();
        }

        /**
         * repaint the chat gui
         */
        private void repaintChatContent() {
            removeAll();
            //TODO 5 effizienz: nicht allealle zeichnen sondern bei 20 oder so stoppen
            int margin = (int) (UI_SCALING * 3);
            int currentY = this.height - margin;
            for (int i = 0; i < chatMessages.size(); i++) {
                ChatMessage message = chatMessages.get(i);
                currentY -= (message.getHeight() + margin);
                int x = 0;
                switch (message.getType()) {
                    case INFO:
                        x = width / 2 - message.getWidth() / 2;
                        break;
                    case FROM:
                        x = margin * 2;
                        break;
                    case TO:
                        x = width - message.getWidth() - margin * 2;
                        break;
                }
                message.setLocation(x, currentY);
                this.add(message);
                message.repaint();
            }
        }

        /**
         * clears and deletes all the messages from the JPanel
         */
        public void removeAllChatMessages() {
            this.removeAll();
            this.repaint();
            this.chatMessages.clear();
        }

        private class ChatMessage extends JPanel {
            int width;
            int height;
            String name;
            Message message;
            String date;
            JLabel nameLabel;
            JTextArea textArea;
            JLabel timestamp;
            chatMessageType type;


            public ChatMessage(chatMessageType type, String name, Message message, @Nullable String date) {
                this.name = name;
                this.message = message;
                this.date = date;
                this.type = type;
                this.width = ChatContent.this.getWidth() * 3 / 5;
                this.setLayout(null);

                //width and height berechnen
                int margin = (int) UI_SCALING;
                nameLabel = new JLabel(name);
                nameLabel.setFont(new Font(MainWindow.FONT, 1, (int) (UI_SCALING * 16 / 2)));
                nameLabel.setSize(nameLabel.getPreferredSize());
                if (type == chatMessageType.FROM)
                    nameLabel.setLocation((int) (UI_SCALING * 6), (int) (UI_SCALING * 2));
                else
                    nameLabel.setLocation((int) (UI_SCALING * 2), (int) (UI_SCALING * 2));

                Font messageFont = new Font(MainWindow.FONT, 0, (int) (UI_SCALING * 10 / 2));
                textArea = new JTextArea();
                textArea.setText(formatTextForChat(message.getMessage(),messageFont,this.width- (int)(UI_SCALING * 8)));
                textArea.setEditable(false);
                textArea.setBackground(MainWindow.theme.getPrimaryColorLight());
                textArea.setFont(messageFont);
                //TODO 3 calc Height from message length
                textArea.setSize(textArea.getPreferredSize());
                textArea.setLocation(nameLabel.getX(), nameLabel.getY() + nameLabel.getHeight() + margin);

                timestamp = new JLabel(date);
                //TODO 7 positioning


                height = margin * 3 + nameLabel.getHeight() + textArea.getHeight() + timestamp.getHeight();
                this.setSize(width, height);

                Color borderColor;
                switch (this.getType()) {
                    case INFO:
                        borderColor = MainWindow.theme.getAccentColor();
                        break;
                    default:
                        borderColor = MainWindow.theme.getPrimaryColorDark();
                }

               this.setBackground(MainWindow.theme.getPrimaryColorLight());
                this.setOpaque(false);

                AbstractBorder brdr = new BubbleBorder(this.type, borderColor, (int) (UI_SCALING ), (int) (UI_SCALING*2), (int) (UI_SCALING * 4));
                this.setBorder(brdr);


                this.add(nameLabel);
                this.add(textArea);
                this.add(timestamp);
                Chat.this.repaint();
                this.repaint();
            }

            private String formatTextForChat(String message,Font fontUsed,int goalWidth) {
                String ret = message;
                //TODO 2 better formatting
                Rectangle2D rectangle2D=textArea.getFontMetrics(fontUsed).getStringBounds(ret,textArea.getGraphics());
                while (rectangle2D.getWidth()>goalWidth){
                    ret=splitLongest(ret);
                    rectangle2D=textArea.getFontMetrics(fontUsed).getStringBounds(ret,textArea.getGraphics());
                }
                return ret;
            }
            /**
             * split the line that is the longest in the most appropriate way
             */
            private String splitLongest(String message) {
                String ret = message;
                String []strings = ret.split("\n");
                for (int i = 0; i < strings.length; i++) {
                    //TODO 1 find the longest string and split it in a good way
                }
                return ret;
            }

            public chatMessageType getType() {
                return type;
            }

            public void setType(chatMessageType type) {
                this.type = type;
            }

            @Override
            public int getWidth() {
                return width;
            }

            public void setWidth(int width) {
                this.width = width;
            }

            @Override
            public int getHeight() {
                return height;
            }

            public void setHeight(int height) {
                this.height = height;
            }

            @Override
            public String getName() {
                return name;
            }

            @Override
            public void setName(String name) {
                this.name = name;
            }

            public Message getMessage() {
                return message;
            }

            public void setMessage(Message message) {
                this.message = message;
            }

            public String getDate() {
                return date;
            }

            public void setDate(String date) {
                this.date = date;
            }
        }
    }
}
