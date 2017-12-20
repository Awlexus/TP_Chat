package logic;

import gui.*;
import logic.storage.DataRepository;
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
        String username = "Matteo";

        mainWindow = new MainWindow(null);

        AtomicInteger ai = new AtomicInteger(0);

        String repositoryPath = Paths.get("").toAbsolutePath().toString()+"\\savefiles";
        new File(repositoryPath).mkdir();

        contacts = new Contacts(ai, repositoryPath);
        groups = new Groups(ai, repositoryPath);



        callbackListener = new CallbackListener(mainWindow, contacts, groups);

        // TODO: 15.12.2017 try to read from files
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
        });

        mainWindow.addChatActionListener(new ChatActionListener() {
            @Override
            public void onSendPressed(SendEvent e) {
                if (e.getMessage().equals("") || callbackListener.currentChatId == -1)
                    return;
                mainWindow.addMessage(
                        new ChatMessageBlueprint(
                                Chat.chatMessageType.TO,
                                username, e.getMessage(),
                                null, Color.GREEN), 1);
                e.getTextField().setText("");
                protocol.message(e.getMessage(), contacts.getByID(callbackListener.currentChatId).getIp());
                // TODO: 20.12.2017 send typing = false
            }

            @Override
            public void onEditTextChanged(TextChangedEvent e) {
                // TODO: 15.12.2017 send "typing"
            }
        });
    }
}