package gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * @author Matteo Cosi
 * @since 03.12.2017
 */
 class ContactScrolling extends MouseAdapter {
    private Contacts.Contact c;
    private Contacts contacts;

    /**
     * Used to drag & drop the contacts
     */
    private Point fromCords;

    ContactScrolling(Contacts.Contact c, Contacts contacts) {
        this.c=c;
        this.contacts=contacts;
    }

    public void mouseClicked(MouseEvent e) {
        for (int i = 0; i < this.contacts.contactArrayList.size(); i++) {
            Contacts.Contact reset = this.contacts.contactArrayList.get(i);
            reset.setSelected(false);
            reset.removeColorName();
        }
        c.colorName();
        c.setSelected(true);
        for (int i = 0; i < this.contacts.onContactClickedListener.size(); i++) {
            this.contacts.onContactClickedListener.get(i).onContactClicked(new ContactEvent(c.getName(), c.getId(), c));
        }
    }

    @Override
    public void mouseEntered(MouseEvent e) {
        c.colorName();
    }

    @Override
    public void mouseExited(MouseEvent e) {
        if (!c.isSelected)
            c.removeColorName();
    }

    public void mouseReleased(MouseEvent e) {
        Contacts.Contact last = this.contacts.contactArrayList.get(this.contacts.contactArrayList.size() - 1);
        int valToCheck = last.getY();

        if (valToCheck + last.getHeight() + 2 < this.contacts.getHeight()) {
            this.contacts.removeAll();
            int reverse = this.contacts.contactArrayList.size() - 1;
            for (int i = 0; i < this.contacts.contactArrayList.size(); i++) {
                Contacts.Contact readd = this.contacts.contactArrayList.get(reverse);
                reverse--;
                readd.setLocation(0, (this.contacts.getHeight() - this.contacts.getWidth() / 3) - (this.contacts.getWidth() / 3) * i);
                this.contacts.add(readd);
            }
            this.contacts.repaint();
        }

        Contacts.Contact first = this.contacts.contactArrayList.get(0);
        valToCheck = first.getY();

        if (valToCheck > 2) {
            this.contacts.removeAll();
            for (int i = 0; i < this.contacts.contactArrayList.size(); i++) {
                Contacts.Contact readd = this.contacts.contactArrayList.get(i);
                readd.setLocation(0, (this.contacts.getWidth() / 3) * i);
                this.contacts.add(readd);
            }
            this.contacts.repaint();
        }

    }

    public void mousePressed(MouseEvent e) {
        fromCords = e.getPoint();
    }



    @Override
    public void mouseDragged(MouseEvent e) {
            Point toCords = e.getPoint();
            int offset = toCords.y - fromCords.y;
            boolean scrollDown = true;
            if (offset > 0)
                scrollDown = false;
            if (this.contacts.isRelocateValid(scrollDown)) {
                for (int i = 0; i < this.contacts.contactArrayList.size(); i++) {
                    Contacts.Contact relocate = this.contacts.contactArrayList.get(i);
                    relocate.setLocation(relocate.getX(), relocate.getY() + offset);


                }
            }
    }
}