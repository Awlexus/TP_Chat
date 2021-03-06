package gui;

import com.vdurmont.emoji.EmojiParser;
import org.jetbrains.annotations.Nullable;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.AbstractBorder;
import javax.swing.filechooser.FileSystemView;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;

import static gui.MainWindow.UI_SCALING;
import static gui.MainWindow.theme;

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
    private ArrayList<ChatContent> chatContents;

    public void clearChatById(int id) {
        ChatContent chatContent = new ChatContent(getChatContents().get(id).width,
                getChatContents().get(id).height);
        getChatContents().add(id, chatContent);
    }



    /**
     * describes the direction of the text
     * FROM...an other user is the autor
     * TO... I am the autor
     * INFO...a text for information purposes only
     */
    public enum chatMessageType {
        FROM, TO, INFO
    }

    public Chat() {
        setBackground(theme.getPrimaryColorLight());
        chatActionListeners = new ArrayList<>();
        chatContents = new ArrayList<>();

    }

    public void setupUI() {

        chatContent = new ChatContent(getWidth(), (int) (getHeight() * (1 - SCREEN_SPLITING_RATIO)));
        chatContent.setLocation(0, 0);

        chatControls = new ChatControls(getWidth(), getHeight() - chatContent.height);
        chatControls.setLocation(0, chatContent.height);

        add(chatContent);
        add(chatControls);
        chatContents.add(chatContent);

        //add the scrolling for the chat. EXPORTED to make it more readable

    }


    /**
     * adds a new File at the bottom of the screen
     */
    public void addDatei(DateiMessageBlueprint blueprint, int id) {
        chatContents.get(id).addDateiMessage(blueprint);
    }//TODO

    /**
     * adds a new text at the bottom of the screen
     */
    public void addMessage(ChatMessageBlueprint blueprint, int id) {

        chatContents.get(id).addChatMessage(blueprint);
    }

    /**
     * see Mainwindow description
     *
     * @param blueprints
     */
    public void addMessages(ChatMessageBlueprint[] blueprints, int id) {
        chatContents.get(id).addChatMessages(blueprints);
    }

    public void addChatActionListener(ChatActionListener chatActionListener) {
        this.chatActionListeners.add(chatActionListener);
    }

    public void removeChatActionListener(ChatActionListener chatActionListener) {
        this.chatActionListeners.remove(chatActionListener);
    }

    public ArrayList<ChatContent> getChatContents() {
        return chatContents;
    }

    /**
     * removes all chat messages
     */
    public void clearChat() {
        chatContent.removeAllChatMessages();
    }

    public void setChatByUserId(int id) {
        remove(chatContent);
        chatContent = chatContents.get(id);
        chatContent.repaint();
        add(chatContent);
        repaint();

    }

    public void addNewUserChat(int id) {
        while (id >= chatContents.size()) {
            ChatContent chatContent = new ChatContent(getWidth(), (int) (getHeight() * (1 - SCREEN_SPLITING_RATIO)));
            chatContent.setLocation(0, 0);
            chatContents.add(chatContent);
        }

    }


    private class ChatControls extends JPanel {
        int width;
        int height;

        JTextField textField;
        JButton send;
        JButton attach;
        JButton audio;

        public ChatControls(int width, int height) {
            this.setLayout(null);
            this.width = width;
            this.height = height;
            this.setSize(width, height);
            setBackground(theme.getPrimaryColorLight());
            setBorder(BorderFactory.createMatteBorder(
                    (int) UI_SCALING, 0, (int) UI_SCALING, (int) UI_SCALING, theme.getPrimaryColorDark()));


            textField = new JTextField();
            textField.setFont(new Font(MainWindow.FONT, 0, (int) (this.height * 4 / 5 - UI_SCALING * 6)));
            textField.setSize((int) (width * 3 / 6 + UI_SCALING * 6), textField.getPreferredSize().height);
            textField.setLocation((int) UI_SCALING * 3, height / 2 - textField.getHeight() / 2);
            textField.setBorder(BorderFactory.createMatteBorder((int) UI_SCALING, (int) UI_SCALING, (int) UI_SCALING, (int) UI_SCALING, theme.getPrimaryColorDark()));
            textField.setBackground(theme.getPrimaryColorLight());
            textField.setForeground(Color.BLACK);
            textField.addKeyListener(new KeyAdapter() {
                @Override
                public void keyReleased(KeyEvent e) {
                    for (int i = 0; i < chatActionListeners.size(); i++) {
                        chatActionListeners.get(i).onEditTextChanged(new TextChangedEvent(textField));
                    }
                    if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                        for (int i = 0; i < chatActionListeners.size(); i++) {
                            chatActionListeners.get(i).onSendPressed(new SendEvent(textField, textField.getText(), textField));
                        }
                    }
                }

                public void keyPressed(KeyEvent e) {
                    if (e.isAltDown()) {
                        if (e.getKeyCode() == KeyEvent.VK_F4) {
                            System.out.println("exit");
                            for (int i = 0; i < MainWindow.mainWindowListeners.size(); i++) {
                                MainWindow.mainWindowListeners.get(i).onExitClicked();
                            }
                        }
                    }
                }
            });


            send = new JButton("SENDE");
            send.setFont(new Font(MainWindow.FONT, 0, (int) (this.height * 3 / 5 - UI_SCALING * 8)));
            send.setSize((int) ((width * 2 / 6 - UI_SCALING * 6)*2  / 3), textField.getHeight());
            send.setLocation((int) (textField.getWidth() + UI_SCALING * 6), height / 2 - textField.getHeight() / 2);
            send.setBackground(theme.getPrimaryColorDark());
            if (theme.getDark())
                send.setForeground(Color.white);
            else
                send.setForeground(Color.black);

            send.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    for (int i = 0; i < chatActionListeners.size(); i++) {
                        chatActionListeners.get(i).onSendPressed(new SendEvent(send, textField.getText(), textField));
                    }
                }

                @Override
                public void mousePressed(MouseEvent e) {
                    //TODO 6 find a way to controll the button color on press
                }

                @Override
                public void mouseEntered(MouseEvent e) {
                    send.setForeground(theme.getAccentColor());
                }

                @Override
                public void mouseExited(MouseEvent e) {
                    if (theme.getDark())
                        send.setForeground(Color.white);
                    else
                        send.setForeground(Color.black);
                }
            });
            send.addKeyListener(new KeyAdapter() {

                public void keyPressed(KeyEvent e) {
                    if (e.isAltDown()) {
                        if (e.getKeyCode() == KeyEvent.VK_F4) {
                            System.out.println("exit");
                            for (int i = 0; i < MainWindow.mainWindowListeners.size(); i++) {
                                MainWindow.mainWindowListeners.get(i).onExitClicked();
                            }
                        }
                    }
                }
            });
            BufferedImage buttonIcon = null;
            try {
                buttonIcon = ImageIO.read(Chat.class.getResource("attach.png"));
            } catch (IOException e) {
                e.printStackTrace();
            }
            attach = new JButton(new ImageIcon(buttonIcon));
            attach.setFont(new Font(MainWindow.FONT, 0, (int) (this.height * 3 / 5 - UI_SCALING * 8)));
            attach.setSize((int) ((width * 2 / 6 - UI_SCALING * 6)) / 3, textField.getHeight());
            attach.setLocation((int) (send.getX() + send.getWidth() + UI_SCALING * 3), height / 2 - textField.getHeight() / 2);
            attach.setBackground(theme.getPrimaryColorDark());
            attach.setBorderPainted(false);
            if (theme.getDark())
                attach.setForeground(Color.white);
            else
                attach.setForeground(Color.black);
            attach.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    JFileChooser jfc = new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory());
                    int returnValue = jfc.showOpenDialog(null);
                    if (returnValue == JFileChooser.APPROVE_OPTION) {
                        File selectedFile = jfc.getSelectedFile();
                        Path path = Paths.get(selectedFile.getAbsolutePath());
                        StringBuilder bytes = new StringBuilder();
                        try {
                            byte[] data = Files.readAllBytes(path);
                            for (int i = 0; i < data.length; i++) {
                                bytes.append(data[i]);
                            }
                        } catch (IOException e1) {
                            e1.printStackTrace();
                        }
                        for (int i = 0; i < chatActionListeners.size(); i++) {
                            chatActionListeners.get(i).onSendPressed(new SendEvent(attach, bytes.toString(), null));
                        }
                    }
                }

                @Override
                public void mousePressed(MouseEvent e) {
                    //TODO 6 find a way to controll the button color on press
                }

                @Override
                public void mouseEntered(MouseEvent e) {
                    attach.setForeground(theme.getAccentColor());
                }

                @Override
                public void mouseExited(MouseEvent e) {
                    if (theme.getDark())
                        attach.setForeground(Color.white);
                    else
                        attach.setForeground(Color.black);
                }
            });
            attach.addKeyListener(new KeyAdapter() {

                public void keyPressed(KeyEvent e) {
                    if (e.isAltDown()) {
                        if (e.getKeyCode() == KeyEvent.VK_F4) {
                            System.out.println("exit");
                            for (int i = 0; i < MainWindow.mainWindowListeners.size(); i++) {
                                MainWindow.mainWindowListeners.get(i).onExitClicked();
                            }
                        }
                    }
                }
            });

            BufferedImage buttonIcon2 = null;
            try {
                buttonIcon2 = ImageIO.read(Chat.class.getResource("audio.png"));
            } catch (IOException e) {
                e.printStackTrace();
            }
            audio = new JButton(new ImageIcon(buttonIcon2));
            audio.setFont(new Font(MainWindow.FONT, 0, (int) (this.height * 3 / 5 - UI_SCALING * 8)));
            audio.setSize((int) ((width * 2 / 6 - UI_SCALING * 6)) / 3, textField.getHeight());
            audio.setLocation((int) (attach.getX() + attach.getWidth() + UI_SCALING * 3), height / 2 - textField.getHeight() / 2);
            audio.setBackground(theme.getPrimaryColorDark());
            audio.setBorderPainted(false);
            if (theme.getDark())
                audio.setForeground(Color.white);
            else
                audio.setForeground(Color.black);

            audio.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {

                }

                @Override
                public void mousePressed(MouseEvent e) {
                    //TODO 6 find a way to controll the button color on press
                }

                @Override
                public void mouseEntered(MouseEvent e) {
                    audio.setForeground(theme.getAccentColor());
                }

                @Override
                public void mouseExited(MouseEvent e) {
                    if (theme.getDark())
                        audio.setForeground(Color.white);
                    else
                        audio.setForeground(Color.black);
                }
            });
            audio.addKeyListener(new KeyAdapter() {

                public void keyPressed(KeyEvent e) {
                    if (e.isAltDown()) {
                        if (e.getKeyCode() == KeyEvent.VK_F4) {
                            System.out.println("exit");
                            for (int i = 0; i < MainWindow.mainWindowListeners.size(); i++) {
                                MainWindow.mainWindowListeners.get(i).onExitClicked();
                            }
                        }
                    }
                }
            });

            this.add(textField);
            this.add(send);
            this.add(attach);
            this.add(audio);
        }

    }


    public class ChatContent extends JPanel {
        int width;
        int height;


        ArrayList<ChatMessage> chatMessages;
        JProgressBar chatMessagesLoadingProgress = new JProgressBar();


        public ChatContent(int width, int height) {
            this.setLayout(null);
            this.width = width;
            this.height = height;
            this.setSize(width, height);
            setBackground(theme.getPrimaryColorLight());
            chatMessages = new ArrayList<>();

            setBorder(BorderFactory.createMatteBorder(
                    0, 0, 0, (int) UI_SCALING, theme.getPrimaryColorDark()));


            ChatScrolling scrolling = new ChatScrolling(this);
            this.addMouseListener(scrolling);
            this.addMouseMotionListener(scrolling);
            this.addMouseWheelListener(scrolling);

            chatMessagesLoadingProgress.setSize(width / 2, (int) (10 * UI_SCALING));
            chatMessagesLoadingProgress.setLocation(width / 4, height / 2 - (int) (10 * UI_SCALING) / 2);
            chatMessagesLoadingProgress.setVisible(false);
            this.add(chatMessagesLoadingProgress);

        }



        public void addDateiMessage(DateiMessageBlueprint blueprint) {


        }
        /**
         * adds a new text at the bottom of the screen
         */
        public void addChatMessage(ChatMessageBlueprint blueprint) {
            ChatMessage chatMessage = new ChatMessage(blueprint.getType(), blueprint.getName(), blueprint.getMessage(), blueprint.getDate(), blueprint.getColor());
            chatMessages.add(0, chatMessage);
            ChatScrolling scrolling = new ChatScrolling(ChatContent.this);
            chatMessage.addMouseListener(scrolling);
            chatMessage.addMouseMotionListener(scrolling);
            chatMessage.addMouseWheelListener(scrolling);
            repaintChatContent();
        }


        public void addChatMessages(ChatMessageBlueprint[] blueprints) {
            chatMessagesLoadingProgress.setVisible(true);
            chatMessagesLoadingProgress.setMinimum(0);
            chatMessagesLoadingProgress.setValue(0);
            chatMessagesLoadingProgress.setMaximum(blueprints.length);
            int i = 0;
            for (ChatMessageBlueprint blueprint : blueprints) {
                ChatMessage chatMessage = new ChatMessage(blueprint.getType(), blueprint.getName(), blueprint.getMessage(), blueprint.getDate(), blueprint.getColor());
                chatMessages.add(0, chatMessage);
                ChatScrolling scrolling = new ChatScrolling(ChatContent.this);
                chatMessage.addMouseListener(scrolling);
                chatMessage.addMouseMotionListener(scrolling);
                chatMessage.addMouseWheelListener(scrolling);
                if (i % 2 == 0) {
                    chatMessagesLoadingProgress.setValue(i);
                    chatMessagesLoadingProgress.repaint();
                }
                i++;
            }
            chatMessagesLoadingProgress.setVisible(false);
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

        /**
         * checks if scrolling is alowed
         */
        public boolean isRelocateValid(boolean scrollDown) {
            if (chatMessages.size() > 0)
                if (!scrollDown) {
                    ChatMessage first = chatMessages.get(chatMessages.size() - 1);
                    int valToCheck = first.getY();

                    if (valToCheck > 0)
                        return false;
                } else {
                    ChatMessage last = chatMessages.get(0);
                    int valToCheck = last.getY();
                    if (valToCheck + last.getHeight() < this.getHeight())
                        return false;
                }
            return true;
        }



        class ChatMessage extends JPanel {
            int width;
            int height;
            String name;
            String message;
            String date;
            JLabel nameLabel;
            JTextArea textArea;
            JLabel timestamp;
            chatMessageType type;
            Color nameColor = Color.BLACK;

            public ChatMessage(chatMessageType type, String name, String message, @Nullable String date, Color color) {
                this.name = name;
                this.message = message;
                this.date = date;
                this.type = type;

                if (type != chatMessageType.INFO)
                    this.width = ChatContent.this.getWidth() * 3 / 5;
                this.setLayout(null);

                if (color != null)
                    this.nameColor = color;

                //width and height berechnen
                int margin = (int) UI_SCALING;
                nameLabel = new JLabel(name);
                nameLabel.setForeground(nameColor);
                if (type != chatMessageType.INFO) {
                    nameLabel.setFont(new Font(MainWindow.FONT, 1, (int) (UI_SCALING * 16 / 2)));
                    nameLabel.setSize(nameLabel.getPreferredSize());
                    if (type == chatMessageType.FROM)
                        nameLabel.setLocation((int) (UI_SCALING * 6), (int) (UI_SCALING * 2));
                    else
                        nameLabel.setLocation((int) (UI_SCALING * 2), (int) (UI_SCALING * 2));
                }
                Font messageFont = new Font(MainWindow.FONT, 0, (int) (UI_SCALING * 10 / 2));

                textArea = new JTextArea();
                if (type == chatMessageType.INFO) {
                    this.width = (int) textArea.getFontMetrics(messageFont).getStringBounds(message, textArea.getGraphics()).getWidth() + margin * 8 + 100;
                    if (this.width > ChatContent.this.getWidth() * 3 / 5)
                        this.width = ChatContent.this.getWidth() * 3 / 5;
                }

                textArea.setText(formatTextForChat(EmojiParser.parseToUnicode(message), messageFont, this.width - (int) (UI_SCALING * 8) - margin * 4));
                textArea.setEditable(false);
                textArea.setBackground(theme.getPrimaryColorLight().darker());
                textArea.setFont(messageFont);

                textArea.setSize(textArea.getPreferredSize());
                if (type == chatMessageType.INFO) {
                    textArea.setLocation(width / 2 - textArea.getSize().width / 2, (int) (margin * 2 + UI_SCALING * 2));
                    textArea.setForeground(Color.GRAY);
                    textArea.setAlignmentX(CENTER_ALIGNMENT);
                    textArea.setForeground(theme.getAccentColor());
                } else
                    textArea.setLocation(nameLabel.getX() + margin * 2, nameLabel.getY() + nameLabel.getHeight() + margin);
                ChatScrolling scrolling = new ChatScrolling(ChatContent.this);
                textArea.addMouseListener(scrolling);
                textArea.addMouseMotionListener(scrolling);
                textArea.addMouseWheelListener(scrolling);
                textArea.addKeyListener(new KeyAdapter() {
                    public void keyPressed(KeyEvent e) {
                        if (e.isAltDown()) {
                            if (e.getKeyCode() == KeyEvent.VK_F4) {
                                System.out.println("exit");
                                for (int i = 0; i < MainWindow.mainWindowListeners.size(); i++) {
                                    MainWindow.mainWindowListeners.get(i).onExitClicked();
                                }
                            }
                        }
                    }
                });
                timestamp = new JLabel(date);
                height = textArea.getLocation().y + textArea.getHeight();
                this.setSize(width, height);

                this.add(textArea);
                this.add(timestamp);


                this.setSize(width, height);

                Color borderColor = theme.getPrimaryColorDark();


                this.setBackground(theme.getPrimaryColorLight().darker());
                this.setOpaque(true);

                if (type != chatMessageType.INFO) {
                    AbstractBorder brdr = new BubbleBorder(this.type, borderColor, (int) (UI_SCALING), (int) (UI_SCALING * 2), (int) (UI_SCALING * 4));
                    this.setBorder(brdr);
                }

                this.add(nameLabel);
                this.repaint();
            }

            public Color getNameColor() {
                return nameColor;
            }

            private String formatTextForChat(String message, Font fontUsed, int goalWidth) {
                String ret = message;
                boolean allsplited = false;
                while (!allsplited) {
                    ret = splitLongest(ret, goalWidth, fontUsed);
                    allsplited = true;
                    String[] strings = ret.split("\n");
                    for (int i = 0; i < strings.length; i++) {
                        //if text exceeds limit
                        int length = (int) textArea.getFontMetrics(fontUsed).getStringBounds(strings[i], textArea.getGraphics()).getWidth();
                        if (length > goalWidth)
                            allsplited = false;
                    }

                }
                ret = appendSmallest(ret, goalWidth);
                return ret;
            }

            private String appendSmallest(String ret, int goalWidth) {
                //TODO optional final formatting
                //zum beispiel \n nicht zu ignorieren
                return ret;
            }

            /**
             * split the line that is the longest in the most appropriate way
             */
            private String splitLongest(String message, int goalWidth, Font fontUsed) {
                String ret = message;
                String[] strings = ret.split("\n");

                StringBuilder builder = new StringBuilder();
                //find longest
                for (int i = 0; i < strings.length; i++) {
                    //if text exceeds limit
                    int length = (int) textArea.getFontMetrics(fontUsed).getStringBounds(strings[i], textArea.getGraphics()).getWidth();
                    if (length > goalWidth) {
                        String split1, split2;
                        double ratio = goalWidth / (length + 0.0);
                        int indexToSplit = (int) (strings[i].toCharArray().length * ratio);
                        if (ret.contains(" ")) {
                            //find the next space downwards or upwards
                            int splitindex = -1;
                            for (int j = indexToSplit; j > 0; j--) {
                                if (strings[i].charAt(j) == ' ') {
                                    splitindex = j;
                                    break;
                                }
                            }
                            //split
                            if (splitindex > 0) {
                                split1 = strings[i].substring(0, splitindex);
                                split2 = strings[i].substring(splitindex + 1, strings[i].length());
                            } else {
                                split1 = strings[i].substring(0, indexToSplit - 1);
                                split2 = strings[i].substring(indexToSplit, strings[i].length());
                            }
                            if (goalWidth > (int) textArea.getFontMetrics(fontUsed).getStringBounds(split2, textArea.getGraphics()).getWidth())
                                builder.append(split1 + "\n" + split2 + "");
                                //TODO wenn ein user enter drükt wird das ignoriert
                            else
                                builder.append(split1 + "\n" + split2 + "\n");

                        } else {
                            split1 = strings[i].substring(0, indexToSplit - 1);
                            split2 = strings[i].substring(indexToSplit, strings[i].length());
                            builder.append(split1 + "\n" + split2 + "\n");
                        }
                    } else {
                        //no limit break
                        builder.append(strings[i] + "\n");
                    }
                }
                ret = builder.toString();
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

            public String getMessage() {
                return message;
            }

            public void setMessage(String message) {
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
