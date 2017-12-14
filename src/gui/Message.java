package gui;

import com.vdurmont.emoji.EmojiParser;

/**
 * @author Matteo Cosi
 * @since 03.12.2017
 */
public class Message {
    String text;

    public Message(String text) {
        this.text = text;
    }

    public String getText() {
        return EmojiParser.parseToUnicode(text);
    }


    public void setText(String text) {
        this.text = text;
    }
}
