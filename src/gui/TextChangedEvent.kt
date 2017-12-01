package gui

import javax.swing.JTextField

/**
 * @author Matteo Cosi
 * @since 01.12.2017
 */
class TextChangedEvent(val source: JTextField) {

    val text: String
        get() = source.text
}