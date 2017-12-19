package logic;

import gui.*;
import logic.storage.DataRepository;
import protocol.Protocol;

import java.awt.*;
import java.nio.file.Paths;
import java.util.concurrent.atomic.AtomicInteger;

public class Main {

    private static MainWindow mainWindow;
    private static Contacts contacts;
    private static Groups groups;
    private static CallbackListener callbackListener;
    private static Protocol protocol;

    public static void main(String[] args) {
        mainWindow = new MainWindow(null);

        AtomicInteger ai = new AtomicInteger(0);
        DataRepository dataRepository = new DataRepository(Paths.get("").toAbsolutePath().toString());
        contacts = new Contacts(ai, dataRepository);
        groups = new Groups(ai, dataRepository);

        // TODO: 15.12.2017 try to read from files
        contacts.readContacts();
        groups.readGroups();

        callbackListener = new CallbackListener(mainWindow, contacts, groups);
        protocol = new Protocol("Me", callbackListener);

        protocol.hello();

        mainWindow.addOnContactClickedListener(e -> {
            mainWindow.setChatByUserId(e.getId());
            callbackListener.currentChatId = e.getId();
        });

        mainWindow.addOnExitListener(() -> {
            protocol.stop();
            contacts.printContacts();
            groups.printGroups();
        });

        mainWindow.addChatActionListener(new ChatActionListener() {
            @Override
            public void onSendPressed(SendEvent e) {
                if (e.getMessage().equals("") || callbackListener.currentChatId == -1)
                    return;
                mainWindow.addMessage(
                        new ChatMessageBlueprint(
                                Chat.chatMessageType.TO,
                                "Me", e.getMessage(),
                                null, Color.GREEN), 1);
                e.getTextField().setText("");
                protocol.send(e.getMessage(), contacts.getByID(callbackListener.currentChatId).getIp());
            }

            @Override
            public void onEditTextChanged(TextChangedEvent e) {
                // TODO: 15.12.2017 send "typing"
            }
        });
    }
}