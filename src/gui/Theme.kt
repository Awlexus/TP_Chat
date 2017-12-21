package gui

import java.awt.*

/**
 * Constructor for Custom Theme
 *
 * @param primaryColor      server.ServerMain color of the Theme
 * @param primaryColorDark  Darker version of the server.ServerMain color
 * @param primaryColorLight Lighter version of the server.ServerMain color
 * @param accentColor       Contrast color
 * @param dark              if the design is dark, the font color is going to be white. Otherwise dark
 */
data class Theme(var primaryColor: Color, var primaryColorDark: Color, var primaryColorLight: Color, var accentColor: Color, var dark: Boolean) {

    enum class Themes(val primaryColor: Color, val primaryColorDark: Color, val primaryColorLight: Color, val accentColor: Color, val dark: Boolean) {
        BLUEGREEN(Color(0x2196F3), Color(0x1976D2), Color(0xBBDEFB), Color(0x8BC34A), true),
        GREENAMBER(Color(0x4CAF50), Color(0x388E3C), Color(0xC8E6C9), Color(0xFFC107), true),
        REDGREEN(Color(0xF44336), Color(0xD32F2F), Color(0xFFCDD2), Color(0x4CAF50), true),
        GRAYPINK(Color(0x8E8E8E), Color(0x616161), Color(0xD5D5D5), Color(0xFF4081), true),
        BLUEPINK(Color(0x3F51B5), Color(0x283593), Color(0xC5CAE9), Color(0xFF4081), true),
        GREEN(Color(0x3F51B5), Color(0x283593), Color(0xC5CAE9), Color(0xFF4081), true),
        ORANGE(Color(0xFF5722), Color(0xBF360C), Color(0xFFAB91), Color(0xBF360C), true),
        TEAL(Color(0x009688), Color(0x00695C), Color(0xB2DFDB), Color(0x1DE9B6), true),
        FOREST(Color(0x1E392A).brighter(), Color(0x1E392A), Color(0x3CC47C).brighter().brighter(), Color(0xE9C893), true),
        ULTRA_DARK(Color(0x303030), Color(0x000000), Color(0x888888), Color(0x29B6F6), true),
        DEEP_PURPLE(Color(0x673AB7), Color(0x512DA8), Color(0xB39DDB), Color(0xFFEB3B), true),
        GRAY_TEAL(Color(0x9E9E9E), Color(0x616161), Color(0xEEEEEE), Color(0x009688), true),
        MILAN(Color(0x303030), Color(0x111111), Color(0xA0A0A0), Color(0xFF0000), true),
        DARKBLUE(Color(0x1A237E), Color(0x3F51B5), Color(0xC5CAE9),Color(0xE8EAF6), true);
    }

    /**
     * Template Theme from the Enum: Themes
     *
     * @param theme Element from the Enum Themes
     */
    constructor(theme: Themes) : this(theme.primaryColor, theme.primaryColorDark, theme.primaryColorLight, theme.accentColor, theme.dark)

}
