package tests;


import com.vdurmont.emoji.Emoji;
import com.vdurmont.emoji.EmojiManager;
import com.vdurmont.emoji.EmojiParser;
import gui.*;

import java.awt.*;
import java.util.Collection;

/**
 * @author Matteo Cosi
 * @since 03.12.2017
 */
public class Tester {
    public static void main(String[] args) {

        MainWindow mainWindow = new MainWindow(null);

        mainWindow.addContact("Test1", "hallo1fffff", Color.BLUE, 1);
        mainWindow.addContact("Test2", "hallo1fffff", Color.BLUE, 2);

        mainWindow.addMessage(new ChatMessageBlueprint(Chat.chatMessageType.FROM, "Test", "hhhhhhhhhhhhhaaaaaaaaaaaaaaaalllllllllllllloooooo ooo", null, Color.cyan), 0);
        mainWindow.addMessage(new ChatMessageBlueprint(Chat.chatMessageType.FROM, "Test", "hhhhhhhhhhhhhaaaaaaaaaaaaaaaalllllllllllllloooooo ooo", null, Color.cyan), 0);


        mainWindow.addNewChatById(1);
        mainWindow.addNewChatById(2);
        mainWindow.setChatByUserId(2);

        mainWindow.addOnContactClickedListener(new OnContactClickedListener() {
            @Override
            public void onContactClicked(ContactEvent e) {
                mainWindow.setChatByUserId(e.getId());
                mainWindow.addMessage(new ChatMessageBlueprint(Chat.chatMessageType.FROM, "Test" + e.getId(), "hhhhhhhhhhhhhaaaaaaaaaaaaaaaalllllllllllllloooooo ooo", null, Color.cyan), e.getId());
            }
        });

        /*
        String[] fontFamilies = GraphicsEnvironment.
                getLocalGraphicsEnvironment().
                getAvailableFontFamilyNames();
        for (String name : fontFamilies) {
            Font font = new Font(name, Font.PLAIN, 20);
            if (font.canDisplayUpTo("\uD83D\uDE00") < 0) {
                System.out.println(font.getFontName());
            }
        }
/*



        String str = "An :man_in_tuxedo:awesome :beer:string &#128516;with a few :wink:emojis!";
        String result = EmojiParser.parseToUnicode(str);

        System.out.println(result);
*/
    }
}
