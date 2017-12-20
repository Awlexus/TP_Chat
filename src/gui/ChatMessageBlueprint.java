package gui;

import org.jetbrains.annotations.Nullable;

import java.awt.*;

/**
 * @author Matteo Cosi
 * @since 02.12.2017
 */
public class ChatMessageBlueprint {
    Chat.chatMessageType type;
    String name;
    String message;
    String date;
    Color color;

    public ChatMessageBlueprint(Chat.chatMessageType type, String name, String message, String date,Color color) {
        this.type = type;
        this.name = name;
        this.message = message;
        this.date = date;
        this.color=color;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
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

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
