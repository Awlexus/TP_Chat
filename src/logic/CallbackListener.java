package logic;

import gui.Chat;
import gui.ChatMessageBlueprint;
import gui.MainWindow;
import org.jetbrains.annotations.NotNull;
import protocol.ProtocolCallback;

import java.awt.*;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.util.ArrayList;

public class CallbackListener implements ProtocolCallback {

    private MainWindow mainWindow;
    private Contacts contacts;
    private Groups groups;

    public int currentChatId = -1;

    public CallbackListener(MainWindow mainWindow, Contacts contacts, Groups groups) {
        this.mainWindow = mainWindow;

        contacts.setParent(this);
        groups.setParent(this);

        this.contacts = contacts;
        this.groups = groups;
    }

    @Override
    public void hello(@NotNull DatagramPacket packet, @NotNull String username) {
        Contact contact = contacts.createContact(packet.getAddress(), username,
                new Color((int)(Math.random() * 0x1000000)));
        // debug
        if (contact == null) throw new RuntimeException();
        mainWindow.addContact(contact.getUsername(), "added you", contact.getColor(), contact.getId());
        mainWindow.addNewChatById(contact.getId());
        System.out.println("Hello");
    }

    @Override
    public void world(@NotNull DatagramPacket packet, @NotNull String username) {
        Contact contact = contacts.createContact(packet.getAddress(), username,
                new Color((int)(Math.random() * 0x1000000)));
        // debug
        if (contact == null) throw new RuntimeException();
        mainWindow.addNewChatById(contact.getId());
        mainWindow.addContact(contact.getUsername(), "added you", contact.getColor(), contact.getId());
        System.out.println("World");
    }

    @Override
    public void goodbye(@NotNull DatagramPacket packet) {
        Contact contact = contacts.getByIP(packet.getAddress());
        // TODO: 19.12.2017 FIX THIS!
        if (contact == null)
            System.out.println("NULL_goodbye");
        mainWindow.addMessage(new ChatMessageBlueprint(Chat.chatMessageType.FROM, contact.getUsername(),
                "left the room", "",
                contact.getColor()), contact.getId());
        System.out.println("Goodbye");
    }

    @Override
    public void typing(@NotNull DatagramPacket packet, boolean typing) {
        // TODO: 15.12.2017 GUI is missing an option to remove a single message, clearing the chat and re-adding most messages also has visible effects
        System.out.println("Typing");
    }

    @Override
    public void message(@NotNull DatagramPacket packet, @NotNull String message) {
        System.out.println(currentChatId);
        Contact contact = contacts.getByID(currentChatId);
        // TODO: 19.12.2017 FIX THIS!
        if (contact == null)
            System.out.println("NULL_message");
        mainWindow.addMessage(new ChatMessageBlueprint(Chat.chatMessageType.FROM,
                contact.getUsername(),
                message,
                "", contact.getColor()), contact.getId());
        System.out.println("Message");
    }

    @Override
    public boolean existsGroupWithId(@NotNull DatagramPacket packet, int id) {
        return (groups.getByProtocolID(id) != null);
    }

    @Override
    public void createGroup(@NotNull DatagramPacket packet, int id, @NotNull InetAddress[] members) {
        ArrayList<Contact> membersAsContacts = new ArrayList<>();
        for (InetAddress memberAddress : members) {
            Contact contact = contacts.getByIP(memberAddress);

            // This should NEVER be true
            if (contact == null)
                throw new RuntimeException();

            membersAsContacts.add(contact);
        }
        groups.createGroup(id, membersAsContacts);
    }

    @Override
    public void denyGroup(@NotNull DatagramPacket packet) {
        // TODO: 15.12.2017 No clue what this means, or which steps should be taken.
    }

    @Override
    public void groupMessage(@NotNull DatagramPacket packet, int groupId, @NotNull String message) {
        int correspondingChatID = groups.getByProtocolID(groupId).getId();
        mainWindow.addMessage(new ChatMessageBlueprint(Chat.chatMessageType.FROM,
                contacts.getByIP(packet.getAddress()).getUsername(), message,
                "", contacts.getByIP(packet.getAddress()).getColor()), correspondingChatID);
    }

    public boolean isUsedID(int id) {
        return (contacts.getByID(id) != null || groups.getByID(id) != null);
    }
}
