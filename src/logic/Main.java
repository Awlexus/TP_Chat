package logic;

import gui.*;
import logic.storage.UserUtil;
import protocol.Protocol;

import java.awt.*;
import java.io.File;
import java.nio.file.Paths;
import java.util.concurrent.atomic.AtomicInteger;

public class Main {

    private static MainWindow mainWindow;
    private static Contacts contacts;
    private static Groups groups;
    private static CallbackListener callbackListener;
    private static Protocol protocol;

    public static void main(String[] args) {
        String username = UserUtil.INSTANCE.getUsername();

        mainWindow = new MainWindow(null);

        AtomicInteger ai = new AtomicInteger(0);

        String repositoryPath = Paths.get("").toAbsolutePath().toString()+"\\savefiles";
        //noinspection ResultOfMethodCallIgnored
        new File(repositoryPath).mkdir();

        contacts = new Contacts(ai, repositoryPath);
        groups = new Groups(ai, repositoryPath);

        callbackListener = new CallbackListener(mainWindow, contacts, groups);

        contacts.readContacts();
        groups.readGroups();

        protocol = new Protocol(username, callbackListener);

        protocol.hello();

        mainWindow.addOnContactClickedListener(e -> {
            mainWindow.setChatByUserId(e.getId());
            callbackListener.currentChatId = e.getId();
        });

        mainWindow.addOnExitListener(() -> {
            protocol.stop();
            contacts.printContacts();
            groups.printGroups();
            System.exit(0);
        });

        mainWindow.addChatActionListener(new ChatActionListener() {
            @Override
            public void onSendPressed(SendEvent e) {
                if (e.getMessage().isEmpty() || callbackListener.currentChatId == -1)
                    return;

                Contact contact = contacts.getByID(callbackListener.currentChatId);

                ChatMessageBlueprint chatMessageBlueprint = new ChatMessageBlueprint(
                        Chat.chatMessageType.TO,
                        username, e.getMessage(),
                        null, Color.GREEN);
                mainWindow.addMessage(chatMessageBlueprint, contact.getId());
                contact.getMessages().add(chatMessageBlueprint);

                e.getTextField().setText("");
                mainWindow.setLastMessageText(e.getMessage(), contact.getId());
                protocol.sendTyping(false, contact.getIp());
                protocol.message(e.getMessage(), contact.getIp());
            }

            @Override
            public void onEditTextChanged(TextChangedEvent e) {
                Contact contact = contacts.getByID(callbackListener.currentChatId);
                if (contact != null) {
                    protocol.sendTyping(!e.getText().isEmpty(), contact.getIp());
                }
            }
        });
    }
}