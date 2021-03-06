package gui;

import com.vdurmont.emoji.EmojiParser;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.util.ArrayList;

/**
 * @author Matteo Cosi
 * @since 30.11.2017
 */
public class MainWindow extends JFrame {

    /**
     * Scaling of the UI
     */
    public static double UI_SCALING = 4;


    /**
     * SCALING of the top bar
     */
    public static double TOP_SCALE = 1;

    /**
     * WIDTH of the main window
     */
    public static final int W_WIDTH = 360;

    /**
     * HEIGHT of the main window
     */
    public static final int W_HEIGHT = 240;

    /**
     * SCREEN_SPLITING_RATIO tells how to split the screen.
     *
     * @example 0.15 means 15% of the width is reserved for the
     * contacts and 85% is the size of the actual chat
     */
    public static final double SCREEN_SPLITING_RATIO = 0.20;


    /**
     * Theme for this GUI
     */
    public static Theme theme = new Theme(Theme.Themes.DARKBLUE);

    /**
     * Used to drag & drop the top bar
     */
    Point topBarCords;


    /**
     * Font of the whole chat
     * Available:
     * Dialog.plain
     * DialogInput.plain
     * Monospaced.plain
     * SansSerif.plain
     * Segoe UI Emoji
     * Segoe UI Symbol
     * Serif.plain
     */
    public static String FONT = "Segoe UI Emoji";

    /**
     * top bar
     */
    TopBar topBar;
    /**
     * container for the contacts
     */
    Contacts contacts = null;
    /**
     * actual chat
     */
    Chat chat = null;

    /**
     * Main Container
     */
    public static Container c;

    newGroupWindow newGroupWindow;

    static ArrayList<MainWindowListener> mainWindowListeners;


    public MainWindow(@Nullable Settings settings) {
        setupMainWindow(settings);
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
    }

    private void setupMainWindow(@Nullable Settings settings) {
        //load settings if not null
        if (settings != null) {
            UI_SCALING = settings.getUiScaling();
            TOP_SCALE = settings.getTopScaling();
            FONT = settings.getFont();
            theme = settings.getTheme();
        } else {
            Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();
            UI_SCALING = (dimension.width - 200) / W_WIDTH;
        }
        mainWindowListeners = new ArrayList<>();

        try {
            setUndecorated(true);
        } catch (Exception ignored) {
        }
        //Border


        setBounds(0, 0, (int) (W_WIDTH * UI_SCALING), (int) (W_HEIGHT * UI_SCALING));
        //Center in the desktop
        setLocationRelativeTo(null);

        c = getContentPane();
        c.setLayout(null);

        //instantiate the panels
        topBar = new TopBar("UatsApp");
        contacts = setupContactsPanel();
        chat = setupChatPanel();

        //add all components
        c.add(topBar);
        c.add(contacts);
        c.add(chat);

        //debug only


        topBarCords = null;
        topBar.addMouseListener(new MouseAdapter() {
            public void mouseReleased(MouseEvent e) {
                topBarCords = null;
            }

            public void mousePressed(MouseEvent e) {
                topBarCords = e.getPoint();
            }

        });
        topBar.addMouseMotionListener(new MouseMotionAdapter() {

            public void mouseDragged(MouseEvent e) {
                Point currCoords = e.getLocationOnScreen();
                setLocation(currCoords.x - topBarCords.x, currCoords.y - topBarCords.y);
            }
        });


        setVisible(true);
        topBar.repaint();
    }

    /**
     * Removes all contacts
     */
    public void clearContacts() {
        contacts.removeAll();
        contacts.repaint();
        contacts.getContactArrayList().clear();
    }

    /**
     * removes all messages from the chat
     */
    public void clearChat() {
        chat.clearChat();
    }

    /**
     * removes everything
     */
    public void clearAll() {
        clearContacts();
        clearChat();
        repaint();
    }

    /**
     * adds a contact click listener
     *
     * @param onContactClickedListener to add
     */
    public void addOnContactClickedListener(OnContactClickedListener onContactClickedListener) {
        contacts.addOnContactClickedListener(onContactClickedListener);
    }


    /**
     * removes a contact click listener
     *
     * @param onContactClickedListener to remove
     */
    public void removeOnContactClickedListener(OnContactClickedListener onContactClickedListener) {
        contacts.removeOnContactClickedListener(onContactClickedListener);
    }


    /**
     * adds a chat action listener
     *
     * @param chatActionListener to add
     */
    public void addChatActionListener(ChatActionListener chatActionListener) {
        chat.addChatActionListener(chatActionListener);
    }


    /**
     * removes a chat action listener
     *
     * @param chatActionListener to remove
     */
    public void removeChatActionListener(ChatActionListener chatActionListener) {
        chat.removeChatActionListener(chatActionListener);
    }



    public void addMainWindowListener(MainWindowListener exitListener) {
        mainWindowListeners.add(exitListener);
    }

    public void removeMainWindowListener(MainWindowListener exitListener) {
        mainWindowListeners.remove(exitListener);
    }

    /**
     * Adds a contact to the list
     *
     * @return true if it worked, otherwise false
     */
    public boolean addContact(String name, String lastMessage, Color color) {
        if (contacts == null) {
            return false;
        }
        contacts.addContact(name, lastMessage, color);
        this.repaint();
        return true;
    }

    /**
     * Adds a contact to the list
     *
     * @return true if it worked, otherwise false
     */
    public boolean addContact(String name, String lastMessage, Color color, int id) {
        if (contacts == null) {
            return false;
        }
        if (contacts.alreadyExistsContactId(id)) {
            return false;
        }
        contacts.addContact(name, lastMessage, id, color);
        this.repaint();
        return true;
    }

    public boolean removeContact(int id) {
        if (contacts == null) {
            return false;
        }
        //schau ob es die gibt
        if (!contacts.alreadyExistsContactId(id)) {
            return false;
        }
        contacts.removeContact(id);
        this.repaint();
        return true;
    }


    public void setContactWriting(int id) {
        contacts.setContactWriting(id);
    }

    public void removeContactWriting(int id) {
        contacts.removeContactWriting(id);
    }

    public void toggleContactWriting(int id) {
        contacts.toggleContactWriting(id);
    }

    public void setLastMessageText(String text, int id) {
        contacts.setLastMessageText(text, id);
    }

    /**
     * adds a new text at the bottom of the screen
     *
     * @param blueprint blueprint of all chat attributes
     */
    public void addMessage(ChatMessageBlueprint blueprint, int id) {
        chat.addMessage(blueprint, id);
        repaint();
    }

    /**
     * adds a new text at the bottom of the screen
     *
     * @param blueprint blueprint of all chat attributes
     */
    public void addDatei(DateiMessageBlueprint blueprint, int id) {
        chat.addDatei(blueprint, id);
        repaint();
    }


    /**
     * adds n Messages without repainting every time, but only at the end
     *
     * @param blueprints blueprint of all chat attributes
     */
    public void addMessages(ChatMessageBlueprint[] blueprints, int id) {
        chat.addMessages(blueprints, id);
        repaint();
    }


    public void setChatByUserId(int id) {
        chat.setChatByUserId(id);
    }

    public ChatMessageBlueprint[] getChatMessagesById(int id) {
        ArrayList<ChatMessageBlueprint> blueprints = new ArrayList<>();
        for (Chat.ChatContent.ChatMessage mesg : chat.getChatContents().get(id).chatMessages) {
            blueprints.add(new ChatMessageBlueprint(mesg.getType(), mesg.getName(), mesg.getMessage(), mesg.getDate(), mesg.getNameColor()));
        }
        return blueprints.toArray(new ChatMessageBlueprint[blueprints.size()]);
    }

    public void clearChatById(int id) {
        chat.clearChatById(id);
    }

    public void addNewChatById(int id) {
        chat.addNewUserChat(id);
    }

    @Deprecated
    public void setChat(Chat chat) {
        this.chat = chat;
        repaint();
        this.chat.repaint();
        chat.enable();
    }

    private Contacts setupContactsPanel() {
        Contacts ret = new Contacts();
        ret.setLayout(null);
        ret.setLocation(0, topBar.getHeight());
        ret.setSize((int) ((W_WIDTH * UI_SCALING) * SCREEN_SPLITING_RATIO), (int) (W_HEIGHT * UI_SCALING) - topBar.getHeight());
        return ret;
    }

    private Chat setupChatPanel() {
        Chat ret = new Chat();
        ret.setLayout(null);
        ret.setLocation(contacts.getWidth(), topBar.getHeight());
        ret.setSize((int) (W_WIDTH * UI_SCALING) - contacts.getWidth(), (int) (W_HEIGHT * UI_SCALING) - topBar.getHeight());
        ret.setupUI();
        return ret;
    }


    private class TopBar extends JPanel {


        int TOP_HEIGHT = W_HEIGHT / 12;

        JLabel title;
        JLabel exit;
        JLabel newGroup;


        TopBar(String title) {
            setSize((int) (W_WIDTH * UI_SCALING), (int) (TOP_HEIGHT * TOP_SCALE * UI_SCALING));
            setBackground(theme.getPrimaryColorDark().darker());
            setLayout(null);

            this.title = new JLabel(title);
            int titleFontSize = (int) ((TOP_HEIGHT - TOP_HEIGHT / 4) * TOP_SCALE * UI_SCALING);
            this.title.setFont(new Font(FONT, 0, titleFontSize));
            this.title.setLocation((int) (5 * UI_SCALING), (int) +UI_SCALING * 2);
            this.title.setSize(this.title.getPreferredSize());
            this.title.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseEntered(MouseEvent e) {
                    TopBar.this.title.setForeground(theme.getAccentColor());
                }

                @Override
                public void mouseExited(MouseEvent e) {
                    TopBar.this.title.setForeground(Color.white);
                }

            });


            this.exit = new JLabel("\u274C");
            int exitFontSize = (int) ((TOP_HEIGHT - TOP_HEIGHT / 6) * TOP_SCALE * UI_SCALING);
            this.exit.setFont(new Font(FONT, 0, exitFontSize));
            this.exit.setLocation((int) (((W_WIDTH * UI_SCALING) - (exitFontSize+ exitFontSize/2))), (int) +UI_SCALING * 2);
            this.exit.setSize(this.exit.getPreferredSize());
            this.exit.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseEntered(MouseEvent e) {
                    exit.setForeground(theme.getAccentColor());
                }

                @Override
                public void mouseExited(MouseEvent e) {
                    exit.setForeground(Color.white);
                }

                @Override
                public void mouseClicked(MouseEvent e) {
                    for (int i = 0; i < mainWindowListeners.size(); i++) {
                        mainWindowListeners.get(i).onExitClicked();
                    }
                }

            });
            this.newGroup = new JLabel(EmojiParser.parseToUnicode(":heavy_plus_sign:"));

            int settingsFontSize = (int) ((TOP_HEIGHT - TOP_HEIGHT / 4) * TOP_SCALE * UI_SCALING);
            this.newGroup.setFont(new Font(FONT, Font.PLAIN, settingsFontSize));
            this.newGroup.setLocation((int) (exit.getX() - UI_SCALING * 4 - 20 - settingsFontSize), (int) +UI_SCALING * 3);
            this.newGroup.setSize(this.newGroup.getPreferredSize());
            this.newGroup.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseEntered(MouseEvent e) {
                    newGroup.setForeground(theme.getAccentColor());
                }

                @Override
                public void mouseExited(MouseEvent e) {
                    newGroup.setForeground(Color.white);
                }

                @Override
                public void mouseClicked(MouseEvent e) {
                    openNewUserDialog();
                }

            });

            if (theme.getDark()) {
                this.exit.setForeground(Color.white);
                this.title.setForeground(Color.white);
                this.newGroup.setForeground(Color.white);
            } else {
                this.exit.setForeground(Color.black);
                this.title.setForeground(Color.black);
                this.newGroup.setForeground(Color.black);
            }

            this.add(this.title);
            this.add(this.exit);
            this.add(this.newGroup);

        }


    }

    private void openNewUserDialog() {
        newGroupWindow = new newGroupWindow(this, mainWindowListeners);
    }


}
