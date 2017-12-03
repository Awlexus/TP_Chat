package tests;

import com.vdurmont.emoji.Emoji;
import com.vdurmont.emoji.EmojiManager;
import com.vdurmont.emoji.EmojiParser;
import gui.Chat;
import gui.ChatMessageBlueprint;
import gui.MainWindow;
import gui.Message;

import java.util.Collection;

/**
 * @author Matteo Cosi
 * @since 03.12.2017
 */
public class Tester {
    public static void main(String[] args) {
        MainWindow mainWindow = new MainWindow(null);

        mainWindow.addContact("Test1", "hallo1fffff");
        mainWindow.addContact("Test2", "hallo2ffffffffffffffffaaaaabbbbbbbbbbsssssss");
        mainWindow.addContact("Test3", "d");
        mainWindow.addContact("Test4", "hallo4");
        mainWindow.addContact("Test5", "hallo5", 5);
        mainWindow.addContact("Test6", "hallo6");
        mainWindow.addContact("Test7", "hallo6");
        mainWindow.addContact("Test8", "hallo6");
        mainWindow.addContact("Test9", "hallo6");
        ChatMessageBlueprint[] blueprints = new ChatMessageBlueprint[12];
        for (int i = 0; i < 12; i++) {
            blueprints[i] = new ChatMessageBlueprint(Chat.chatMessageType.TO, "Tom"+i, new Message("Lorem :joy: ipsum dolor sit amet, consectetur adipisci elit, sed eiusmod tempor incidunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrum exercitationem ullam corporis suscipit laboriosam, nisi ut aliquid ex ea commodi consequatur. Quis aute iure reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint obcaecat cupiditat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum"), null);
            i++;
            blueprints[i] = new ChatMessageBlueprint(Chat.chatMessageType.FROM, "Tom"+i, new Message("Lorem ipsum :smile: dolor sit amet, consectetur adipisci elit,:beer: sed eiusmod tempor :man:incidunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrum exercitationem ullam corporis suscipit laboriosam, nisi ut aliquid ex ea commodi consequatur. Quis aute iure reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint obcaecat cupiditat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum"), null);

        }
        mainWindow.addMessages(blueprints);
        mainWindow.removeContact(5);

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
