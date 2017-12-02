package gui;

import org.jetbrains.annotations.Nullable;

/**
 * @author Matteo Cosi
 * @since 02.12.2017
 */
public class ChatMessageBlueprint {
    Chat.chatMessageType type;
    String name;
    Message message;
    String date;

    public ChatMessageBlueprint(Chat.chatMessageType type, String name, Message message, String date) {
        this.type = type;
        this.name = name;
        this.message = message;
        this.date = date;
    }

    public Chat.chatMessageType getType() {
        return type;
    }

    public void setType(Chat.chatMessageType type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Message getMessage() {
        return message;
    }

    public void setMessage(Message message) {
        this.message = message;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
