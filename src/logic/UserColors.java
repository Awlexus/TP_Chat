package logic;

import java.awt.*;
import java.util.ArrayList;

public class UserColors {

    private static ArrayList<Color> colors;

    static {
        colors = new ArrayList<>();

        colors.add(Color.RED);
        colors.add(Color.RED.darker().darker());
        colors.add(Color.GREEN.darker().darker());
        colors.add(Color.cyan.darker());
        colors.add(Color.ORANGE.darker());
        colors.add(Color.BLUE);
        colors.add(Color.BLUE.darker().darker());
        colors.add(Color.YELLOW.darker());
        colors.add(Color.MAGENTA.darker().darker());
    }

    public static Color getRandomColor() {
        return colors.get((int)(Math.random()*colors.size()));
    }
}
