package tests;


import com.vdurmont.emoji.EmojiParser;

import java.awt.*;

/**
 * @author Matteo Cosi
 * @since 03.12.2017
 */
public class Tester {
    public static void main(String[] args) {
/*
        MainWindow mainWindow = new MainWindow(null);

        mainWindow.addContact("Test1", "hallo1fffff", Color.BLUE, 1);
        mainWindow.addContact("Test2", "hallo1fffff", Color.BLUE, 2);


        mainWindow.addNewChatById(1);
        mainWindow.addNewChatById(2);
        mainWindow.setChatByUserId(1);
        ChatMessageBlueprint[] blueprints= new ChatMessageBlueprint[100];
        for (int i = 0; i < 100; i++) {
            blueprints[i]=new ChatMessageBlueprint(Chat.chatMessageType.FROM,"Test","lool","",Color.cyan.darker());
        }
        mainWindow.addMessages(blueprints,1);

        while (true){
            mainWindow.setContactWriting(1);
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            mainWindow.removeContactWriting(1);
            mainWindow.setLastMessageText("hallo",1);
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
*/
        String[] fontFamilies = GraphicsEnvironment.
                getLocalGraphicsEnvironment().
                getAvailableFontFamilyNames();
        for (String name : fontFamilies) {
            Font font = new Font(name, Font.PLAIN, 20);
            if (font.canDisplayUpTo("\uD83D\uDE00") < 0) {
                System.out.println(font.getFontName());
            }
        }



        String str = "An :man_in_tuxedo: awesome :beer:string &#128516;with a few :wink:emojis!";
        String result = EmojiParser.parseToUnicode(str);

        System.out.println(result);
    }
}
