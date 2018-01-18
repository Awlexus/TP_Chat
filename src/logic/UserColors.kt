package logic

import java.awt.Color

object UserColors {

    private var colors = arrayOf(
            Color.RED,
            Color.RED.darker().darker(),
            Color.GREEN.darker().darker(),
            Color.cyan.darker(),
            Color.ORANGE.darker(),
            Color.BLUE,
            Color.BLUE.darker().darker(),
            Color.YELLOW.darker(),
            Color.MAGENTA.darker().darker()
    )

    val randomColor: Color
        get() = colors[(Math.random() * colors.size).toInt()]

}
