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
    ArrayList<Contact> contactArrayList = null;



    ArrayList<OnContactClickedListener> onContactClickedListener;


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

    /**
     * checks if the id exists
     */
    public boolean alreadyExistsContactId(int id) {
        boolean ret = false;
        for (Contact contact : contactArrayList) {
            if (contact.getId() == id)
                return true;
        }
        return ret;
    }

    public void addContact(String name, String lastMessage, int id) {
        Contact c = new Contact(name, lastMessage, getWidth(), getWidth() / 3, id);
        c.setLocation(0, (getWidth() / 3) * contactArrayList.size());
        ContactScrolling scrolling=new ContactScrolling(c,this);
        c.addMouseListener(scrolling);
        c.addMouseMotionListener(scrolling);
        contactArrayList.add(c);
        this.add(c);
    }

    public void addContact(String name, String lastMessage) {
        int id=contactArrayList.size();
        while (alreadyExistsContactId(id)){
            id++;
        }
        this.addContact(name, lastMessage,id);
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

    public void removeContact(int id) {
        boolean found=false;
        for (int i = 0; i < contactArrayList.size(); i++) {
            Contact c = contactArrayList.get(i);
            if(found){
                c.setLocation(c.getX(),c.getY()-c.getHeight());
            }
            if(c.getId()==id){
                contactArrayList.remove(i);
                this.remove(c);
                found=true;
                i--;
            }
        }
        this.repaint();
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
                    0, 0, (int) MainWindow.UI_SCALING, (int) MainWindow.UI_SCALING, MainWindow.theme.getPrimaryColorDark()));
            this.setBackground(MainWindow.theme.getPrimaryColorLight());
            name.setFont(new Font(MainWindow.FONT, 0, height / 2));
            name.setSize(name.getPreferredSize());
            name.setLocation((int) (MainWindow.UI_SCALING * 2), 0);
            name.setForeground(Color.BLACK);

            lastMessage.setFont(new Font(MainWindow.FONT, 0, height / 4));
            lastMessage.setSize(lastMessage.getPreferredSize());
            lastMessage.setLocation((int) (MainWindow.UI_SCALING * 6), name.getHeight());
            lastMessage.setForeground(MainWindow.theme.getPrimaryColor());

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
