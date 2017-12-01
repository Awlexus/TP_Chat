package gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

/**
 * @author Matteo Cosi
 * @since 30.11.2017
 */
public class Contacts extends JPanel {

    /**
     * Container for the Contacts
     */
    private ArrayList<Contact> contactArrayList = null;

    /**
     * Used to drag & drop the contacts
     */
    private Point fromCords;
    private Point toCords;

    private ArrayList<OnContactClickedListener> onContactClickedListener;


    public Contacts() {
        contactArrayList = new ArrayList<>();
        onContactClickedListener = new ArrayList<>();
        setBackground(MainWindow.theme.getPrimaryColorLight());
    }

    public void addOnContactClickedListener(OnContactClickedListener onContactClickedListener) {
        this.onContactClickedListener.add(onContactClickedListener);
    }
    public void removeOnContactClickedListener(OnContactClickedListener onContactClickedListener) {
        this.onContactClickedListener.remove(onContactClickedListener);
    }



    public void addContact(String name, String lastMessage) {
        Contact c = new Contact(name, lastMessage, getWidth(), getWidth() / 3, contactArrayList.size());
        c.setLocation(0, (getWidth() / 3) * contactArrayList.size());
        c.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                for (int i = 0; i < contactArrayList.size(); i++) {
                    Contact reset = contactArrayList.get(i);
                    reset.setSelected(false);
                    reset.setBorder(BorderFactory.createMatteBorder(
                            (int) MainWindow.UI_SCALING, (int) MainWindow.UI_SCALING, (int) MainWindow.UI_SCALING, (int) MainWindow.UI_SCALING, MainWindow.theme.getPrimaryColorDark()));
                }
                c.setBorder(BorderFactory.createMatteBorder(
                        (int) MainWindow.UI_SCALING, (int) MainWindow.UI_SCALING, (int) MainWindow.UI_SCALING, (int) MainWindow.UI_SCALING, MainWindow.theme.getAccentColor()));
                c.setSelected(true);
                for (int i = 0; i < onContactClickedListener.size(); i++) {
                    onContactClickedListener.get(i).onContactClicked(new ContactEvent(c.getName(), c.getId(),c));
                }
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                c.setBorder(BorderFactory.createMatteBorder(
                        (int) MainWindow.UI_SCALING, (int) MainWindow.UI_SCALING, (int) MainWindow.UI_SCALING, (int) MainWindow.UI_SCALING, MainWindow.theme.getAccentColor()));

            }

            @Override
            public void mouseExited(MouseEvent e) {
                if (!c.isSelected)
                    c.setBorder(BorderFactory.createMatteBorder(
                            (int) MainWindow.UI_SCALING, (int) MainWindow.UI_SCALING, (int) MainWindow.UI_SCALING, (int) MainWindow.UI_SCALING, MainWindow.theme.getPrimaryColorDark()));

            }

            public void mouseReleased(MouseEvent e) {
                //TODO contains bug if there is only one contact

                Contact last = contactArrayList.get(contactArrayList.size() - 1);
                int valToCheck = last.getY();

                if (valToCheck + last.getHeight() + 2 < Contacts.this.getHeight()) {
                    Contacts.this.removeAll();
                    int reverse = contactArrayList.size() - 1;
                    for (int i = 0; i < contactArrayList.size(); i++) {
                        Contact readd = contactArrayList.get(reverse);
                        reverse--;
                        readd.setLocation(0, (getHeight() - getWidth() / 3) - (getWidth() / 3) * i);
                        Contacts.this.add(readd);
                    }
                    Contacts.this.repaint();
                }

                Contact first = contactArrayList.get(0);
                valToCheck = first.getY();

                if (valToCheck > 2) {
                    Contacts.this.removeAll();
                    for (int i = 0; i < contactArrayList.size(); i++) {
                        Contact readd = contactArrayList.get(i);
                        readd.setLocation(0, (getWidth() / 3) * i);
                        Contacts.this.add(readd);
                    }
                    Contacts.this.repaint();
                }

            }

            public void mousePressed(MouseEvent e) {
                fromCords = e.getPoint();
            }
        });
        c.addMouseMotionListener(new MouseAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                toCords = e.getPoint();
                int offset = toCords.y - fromCords.y;
                boolean scrollDown = true;
                if (offset > 0)
                    scrollDown = false;
                if (isRelocateValid(scrollDown)) {
                    for (int i = 0; i < contactArrayList.size(); i++) {
                        Contact relocate = contactArrayList.get(i);
                        relocate.setLocation(relocate.getX(), relocate.getY() + offset);
                    }
                }
            }
        });
        contactArrayList.add(c);
        this.add(c);
    }

    public ArrayList<Contact> getContactArrayList() {
        return contactArrayList;
    }

    public boolean isRelocateValid(boolean scrollDown) {
        if (!scrollDown) {
            Contact first = contactArrayList.get(0);
            int valToCheck = first.getY();

            if (valToCheck > 0)
                return false;
        } else {
            Contact last = contactArrayList.get(contactArrayList.size() - 1);
            int valToCheck = last.getY();
            if (valToCheck + last.getHeight() < Contacts.this.getHeight())
                return false;
        }
        return true;
    }



    class Contact extends JPanel {
        JLabel name;
        JLabel lastMessage;
        JLabel arrow;
        int width;
        int height;
        int id;
        boolean isSelected = false;

        public Contact(String name, String lastMessage, int width, int height, int id) {
            this.id = id;
            this.name = new JLabel(name);
            if (lastMessage.length() > 20) {
                lastMessage = lastMessage.substring(0, 16) + "...";
            }
            this.lastMessage = new JLabel(lastMessage);
            this.width = width;
            this.height = height;
            this.setSize(width, height);
            setLayout(null);
            setupContactUI();
        }

        private void setupContactUI() {
            setBorder(BorderFactory.createMatteBorder(
                    (int) MainWindow.UI_SCALING, (int) MainWindow.UI_SCALING, (int) MainWindow.UI_SCALING, (int) MainWindow.UI_SCALING, MainWindow.theme.getPrimaryColorDark()));
            this.setBackground(MainWindow.theme.getPrimaryColorLight());
            name.setFont(new Font(MainWindow.FONT, 0, height / 2));
            name.setSize(name.getPreferredSize());
            name.setLocation(10, 0);//TODO make 10 dynamic
            name.setForeground(Color.BLACK);

            lastMessage.setFont(new Font(MainWindow.FONT, 0, height / 4));
            lastMessage.setSize(lastMessage.getPreferredSize());
            lastMessage.setLocation(30, name.getHeight()); //TODO make 30 dynamic
            lastMessage.setForeground(MainWindow.theme.getprimaryColor());

            arrow = new JLabel(">");

            arrow.setFont(new Font(MainWindow.FONT, 0, height / 2));
            arrow.setSize(arrow.getPreferredSize());
            arrow.setLocation(width - arrow.getWidth() - 10, this.getHeight() / 2 - arrow.getHeight() / 2);
            arrow.setForeground(MainWindow.theme.getAccentColor());


            this.add(name);
            this.add(lastMessage);
            this.add(arrow);
        }

        public void setSelected(boolean selected) {
            isSelected = selected;
        }

        public int getId() {
            return id;
        }

        public String getName() {
            return this.name.getText();
        }
    }
}
