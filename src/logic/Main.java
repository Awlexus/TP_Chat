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
    private static int currentChatId = -1;
    private static Protocol protocol;

    public static void main(String[] args) {
        mainWindow = new MainWindow(null);

        AtomicInteger ai = new AtomicInteger(0);
        DataRepository dataRepository = new DataRepository(Paths.get("").toAbsolutePath().toString());
        // TODO: 15.12.2017 try to read from files before creating
        contacts = new Contacts(ai, dataRepository);
        groups = new Groups(ai, dataRepository);

        protocol = new Protocol("Me", new CallbackListener(mainWindow, contacts, groups));

        protocol.hello();

        mainWindow.addOnContactClickedListener(e -> {
            mainWindow.setChatByUserId(e.getId());
            currentChatId = e.getId();
        });

        mainWindow.addOnExitListener(() -> {
            protocol.stop();
            contacts.print();
        });

        mainWindow.addChatActionListener(new ChatActionListener() {
            @Override
            public void onSendPressed(SendEvent e) {
                if (e.getMessage().equals("") || currentChatId == -1)
                    return;
                mainWindow.addMessage(
                        new ChatMessageBlueprint(
                                Chat.chatMessageType.TO,
                                "Me", e.getMessage(),
                                null, Color.GREEN), 1);
                e.getTextField().setText("");
                protocol.send(e.getMessage(), contacts.getByID(currentChatId).getIp());
            }

            @Override
            public void onEditTextChanged(TextChangedEvent e) {
                // TODO: 15.12.2017 send "typing"
            }
        });
    }
}