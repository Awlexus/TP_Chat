package gui

import javax.swing.JTextField

/**
 * Created by Awlex on 01.12.2017.
 */
class TextChangedEvent(val source: JTextField) {

    val text: String
        get() = source.text
}