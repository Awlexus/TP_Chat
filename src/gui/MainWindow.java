package gui;

import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.util.Vector;

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
    public static final double SCREEN_SPLITING_RATIO = 0.30;


    /**
     * Theme for this GUI
     */
    public static Theme theme = new Theme(Theme.Themes.BLUEPINK);

    /**
     * Used to drag & drop the top bar
     */
    Point topBarCords;


    /**
     * Font of the whole chat
     * Available:
     *  Dialog.plain
     DialogInput.plain
     Monospaced.plain
     SansSerif.plain
     Segoe UI Emoji
     Segoe UI Symbol
     Serif.plain
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


    public MainWindow(@Nullable Settings settings) {
        //load settings if not null
        if(settings!=null){
            UI_SCALING=settings.getUiScaling();
            TOP_SCALE=settings.getTopScaling();
            FONT=settings.getFont();
            theme = settings.getTheme();
        }

        setUndecorated(true);
        //Border

        setBounds(0, 0, (int) (W_WIDTH * UI_SCALING), (int) (W_HEIGHT * UI_SCALING));
        //Center in the desktop
        setLocationRelativeTo(null);

        c = getContentPane();
        c.setLayout(null);

        //instantiate the panels
        topBar = new TopBar("TP_Chat");
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

    /**
     * Adds a contact to the list
     *
     * @return true if it worked, otherwise false
     */
    public boolean addContact(String name, String lastMessage) {
        if (contacts == null) {
            return false;
        }
        contacts.addContact(name, lastMessage);
        this.repaint();
        return true;
    }

    /**
     * Adds a contact to the list
     *
     * @return true if it worked, otherwise false
     */
    public boolean addContact(String name, String lastMessage, int id) {
        if (contacts == null) {
            return false;
        }
        if (contacts.alreadyExistsContactId(id)) {
            return false;
        }
        contacts.addContact(name, lastMessage, id);
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


    /**
     * adds a new message at the bottom of the screen
     *
     * @param blueprint blueprint of all chat attributes
     */
    public void addMessage(ChatMessageBlueprint blueprint) {
        chat.addMessage(blueprint);
        repaint();
    }


    /**
     * adds n Messages without repainting every time, but only at the end
     *
     * @param blueprints blueprint of all chat attributes
     */
    public void addMessages(ChatMessageBlueprint[] blueprints) {
        chat.addMessages(blueprints);
        repaint();
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

        TopBar(String title) {
            setSize((int) (W_WIDTH * UI_SCALING), (int) (TOP_HEIGHT * TOP_SCALE * UI_SCALING));
            setBackground(theme.getPrimaryColorDark());
            setLayout(null);

            this.title = new JLabel(title);
            int titleFontSize = (int) ((TOP_HEIGHT - TOP_HEIGHT / 6) * TOP_SCALE * UI_SCALING);
            this.title.setFont(new Font(FONT, 0, titleFontSize));
            this.title.setLocation((int) (5 * UI_SCALING), (int) +UI_SCALING*2);
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


            this.exit = new JLabel("X");
            int exitFontSize = (int) ((TOP_HEIGHT - TOP_HEIGHT / 6) * TOP_SCALE * UI_SCALING);
            this.exit.setFont(new Font(FONT, 0, exitFontSize));
            this.exit.setLocation((int) (((W_WIDTH * UI_SCALING) - exitFontSize)), (int) +UI_SCALING*2);
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
                    System.exit(0);
                }

            });


            if (theme.getDark()) {
                this.exit.setForeground(Color.white);
                this.title.setForeground(Color.white);
            } else {
                this.exit.setForeground(Color.black);
                this.title.setForeground(Color.black);
            }

            this.add(this.title);
            this.add(this.exit);

        }


    }



}
