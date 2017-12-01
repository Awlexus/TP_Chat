package gui;

import java.awt.*;

public class Theme {

    private Color primaryColor;
    private Color primaryColorDark;
    private Color primaryColorLight;
    private Color accentColor;
    private boolean dark;

    public enum Themes {
        BLUEGREEN, GREENAMBER, REDGREEN, GRAYPINK, BLUEPINK, GREEN, TEAL, ORANGE, FOREST, ULTRA_DARK, DEEP_PURPLE, GRAY_TEAL, MILAN
    }

    /**
     * Constructor for Custom Theme
     *
     * @param primaryColor      server.ServerMain color of the Theme
     * @param primaryColorDark  Darker version of the server.ServerMain color
     * @param primaryColorLight Lighter version of the server.ServerMain color
     * @param accentColor       Contrast color
     * @param dark              if the design is dark, the font color is going to be white. Otherwise dark
     */
    public Theme(Color primaryColor, Color primaryColorDark, Color primaryColorLight, Color accentColor, boolean dark) {
        this.primaryColor = primaryColor;
        this.primaryColorDark = primaryColorDark;
        this.primaryColorLight = primaryColorLight;
        this.accentColor = accentColor;
        this.dark = dark;
    }


    /**
     * Template Theme from the Enum: Themes
     *
     * @param theme Element from the Enum Themes
     */
    public Theme(Themes theme) {
        switch (theme) {
            case BLUEGREEN:
                primaryColor = hex2Color("2196F3");
                primaryColorDark = hex2Color("1976D2");
                primaryColorLight = hex2Color("BBDEFB");
                accentColor = hex2Color("8BC34A");
                dark = true;
                break;
            case GREENAMBER:
                primaryColor = hex2Color("4CAF50");
                primaryColorDark = hex2Color("388E3C");
                primaryColorLight = hex2Color("C8E6C9");
                accentColor = hex2Color("FFC107");
                dark = true;
                break;
            case REDGREEN:
                primaryColor = hex2Color("F44336");
                primaryColorDark = hex2Color("D32F2F");
                primaryColorLight = hex2Color("FFCDD2");
                accentColor = hex2Color("4CAF50");
                dark = true;
                break;
            case GRAYPINK:

                primaryColor = hex2Color("8E8E8E");
                primaryColorDark = hex2Color("616161");
                primaryColorLight = hex2Color("D5D5D5");
                accentColor = hex2Color("FF4081");
                dark = true;
                break;
            case BLUEPINK:

                primaryColor = hex2Color("3F51B5");
                primaryColorDark = hex2Color("283593");
                primaryColorLight = hex2Color("C5CAE9");
                accentColor = hex2Color("FF4081");
                dark = true;
                break;
            case GREEN:

                primaryColor = hex2Color("3F51B5");
                primaryColorDark = hex2Color("283593");
                primaryColorLight = hex2Color("C5CAE9");
                accentColor = hex2Color("FF4081");
                dark = true;
                break;
            case ORANGE:

                primaryColor = hex2Color("FF5722");
                primaryColorDark = hex2Color("BF360C");
                primaryColorLight = hex2Color("FFAB91");
                accentColor = hex2Color("BF360C");
                dark = true;
                break;
            case TEAL:

                primaryColor = hex2Color("009688");
                primaryColorDark = hex2Color("00695C");
                primaryColorLight = hex2Color("B2DFDB");
                accentColor = hex2Color("1DE9B6");
                dark = true;
                break;

            case FOREST:

                primaryColor = hex2Color("1E392A").brighter();
                primaryColorDark = primaryColor.darker();
                primaryColorLight = hex2Color("3CC47C").brighter().brighter();
                accentColor = hex2Color("E9C893");
                dark = true;
                break;


            case ULTRA_DARK:
                primaryColor = hex2Color("303030");
                primaryColorDark = hex2Color("000000");
                primaryColorLight = hex2Color("888888");
                accentColor = hex2Color("29B6F6");
                dark = true;
                break;

            case DEEP_PURPLE:
                primaryColor = hex2Color("673AB7");
                primaryColorDark = hex2Color("512DA8");
                primaryColorLight = hex2Color("B39DDB");
                accentColor = hex2Color("FFEB3B");
                dark = true;
                break;
            case GRAY_TEAL:
                primaryColor = hex2Color("9E9E9E");
                primaryColorDark = hex2Color("616161");
                primaryColorLight = hex2Color("EEEEEE");
                accentColor = hex2Color("009688");
                dark = true;
                break;
            case MILAN:
                primaryColor = hex2Color("303030");
                primaryColorDark = hex2Color("111111");
                primaryColorLight = hex2Color("A0A0A0");
                accentColor = hex2Color("FF0000");
                dark = true;
                break;

        }

    }

    /**
     * @return whether the Theme is Dark or not
     */
    public boolean isDark() {
        return dark;
    }

    /**
     * set whether the Theme is Dark or not
     *
     * @param dark used to tell what color the text should
     */
    public void setDark(boolean dark) {
        this.dark = dark;
    }

    /**
     * @return the primaryColor Color of the Theme
     */
    public Color getprimaryColor() {
        return primaryColor;
    }

    /**
     * sets the server.ServerMain Color
     *
     * @param primaryColor Color to set
     */
    public void setprimaryColor(Color primaryColor) {
        this.primaryColor = primaryColor;
    }

    /**
     * @return the darker version of the server.ServerMain Color
     */
    public Color getPrimaryColorDark() {
        return primaryColorDark;
    }

    /**
     * sets the darker version of the server.ServerMain Color
     *
     * @param primaryColorDark Color to set
     */
    public void setPrimaryColorDark(Color primaryColorDark) {
        this.primaryColorDark = primaryColorDark;
    }

    /**
     * @return the lighter version of the server.ServerMain Color
     */
    public Color getPrimaryColorLight() {
        return primaryColorLight;
    }

    /**
     * sets the lighter version of the server.ServerMain Color
     *
     * @param primaryColorLight Color to set
     */
    public void setPrimaryColorLight(Color primaryColorLight) {
        this.primaryColorLight = primaryColorLight;
    }

    /**
     * @return the accent color of the server.ServerMain Color
     */
    public Color getAccentColor() {
        return accentColor;
    }

    /**
     * sets the contrast Color
     *
     * @param accentColor Color to set
     */
    public void setAccentColor(Color accentColor) {
        this.accentColor = accentColor;
    }

    private static Color hex2Color(String colorStr) {
        return new Color(Integer.valueOf(colorStr.substring(0, 2), 16), Integer.valueOf(colorStr.substring(2, 4), 16),
                Integer.valueOf(colorStr.substring(4, 6), 16));
    }
}
