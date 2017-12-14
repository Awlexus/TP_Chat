package tests;


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

        mainWindow.addContact("Test1", "hallo1fffff",Color.BLUE);
        mainWindow.addContact("Test2", "hallo2ffffffffffffffffaaaaabbbbbbbbbbsssssss",Color.RED);
        mainWindow.addContact("Test3", "d",Color.PINK);
        mainWindow.addContact("Test4", "hallo4",Color.YELLOW);
        mainWindow.addContact("Test5", "hallo5", 5,Color.cyan);
        mainWindow.addContact("Test6", "hallo6",Color.ORANGE);
        mainWindow.addContact("Test6", "hallo6",Color.ORANGE);
        for (int i = 0; i < 1000; i++) {
            mainWindow.addContact("Test"+i, "hallo6",new Color((int)(Math.random()*255),(int)(Math.random()*255),(int)(Math.random()*255)));
        }

        ChatMessageBlueprint[] blueprints = new ChatMessageBlueprint[102];
        for (int i = 0; i < 102; i++) {
            blueprints[i] = new ChatMessageBlueprint(Chat.chatMessageType.TO, "Tom"+i, new Message("smod tempor incidunt ut labore et dolore magna aliqua. Ut enim ad" +
                    "oluptate velit esse cillum dolore eu smod tempor incidunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrum exercitationem ullam corporis suscipit laboriosam, nisi ut aliquid ex ea commodi consequatur. Quis aute iure reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint obcaecat cupiditat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum"), null,Color.BLUE);
            i++;
            blueprints[i] = new ChatMessageBlueprint(Chat.chatMessageType.FROM, "Tom"+i, new Message("dolore magna aliqua. Ut enim ad minim veniam, quis nostrum exercitationem ullam corporis suscipit laboriosam, nisi ut aliquid ex ea commodi consequatur. Quis aute iure reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint obcaecat cupiditat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum"), null,Color.cyan);

        }
        mainWindow.addMessages(blueprints);
        mainWindow.removeContact(5);

        mainWindow.addOnContactClickedListener(new OnContactClickedListener() {
            @Override
            public void onContactClicked(ContactEvent e) {

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

        Collection<Emoji> emojis =EmojiManager.getAll();
        for (Emoji e :
                emojis) {
            System.out.println(e.getAliases());

        }
        String str = "An :man_in_tuxedo:awesome :beer:string &#128516;with a few :wink:emojis!";
        String result = EmojiParser.parseToUnicode(str);
        System.out.println(result);
*/
    }
}
