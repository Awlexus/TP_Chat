package gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;

/**
 * @author Matteo Cosi
 * @since 03.12.2017
 */
class ChatScrolling extends MouseAdapter {
    Chat.ChatContent content;

    /**
     * Used to drag & drop the contacts
     */
    private Point fromCords;
    private Point toCords;

    public ChatScrolling(Chat.ChatContent content) {
        this.content = content;
    }

    public void mouseReleased(MouseEvent e) {
        content.repaint();

    }

    public void mousePressed(MouseEvent e) {
        fromCords = e.getLocationOnScreen();
    }


    @Override
    public void mouseDragged(MouseEvent e) {
        toCords = e.getLocationOnScreen();
        int offset = toCords.y - fromCords.y;
        boolean scrollDown = true;
        if (offset > 0)
            scrollDown = false;
        if (content.isRelocateValid(scrollDown)) {
            for (int i = 0; i < content.chatMessages.size(); i++) {
                Chat.ChatContent.ChatMessage relocate = content.chatMessages.get(i);
                relocate.setLocation(relocate.getX(), relocate.getY() + offset);
            }
        }
        fromCords = toCords;
    }

    @Override
    public void mouseWheelMoved(MouseWheelEvent e) {
        if (e.getWheelRotation() > 0) {
            if (content.isRelocateValid(true)) {
                for (int i = 0; i < content.chatMessages.size(); i++) {
                    Chat.ChatContent.ChatMessage relocate = content.chatMessages.get(i);
                    relocate.setLocation(relocate.getX(), relocate.getY() - e.getScrollAmount()*15);
                }
            }
        } else {
            if (content.isRelocateValid(false)) {
                for (int i = 0; i < content.chatMessages.size(); i++) {
                    Chat.ChatContent.ChatMessage relocate = content.chatMessages.get(i);
                    relocate.setLocation(relocate.getX(), relocate.getY() +e.getScrollAmount()*15);
                }
            }
        }
    }
}
