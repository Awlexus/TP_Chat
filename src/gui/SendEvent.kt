package gui

import javax.swing.JComponent
import javax.swing.JTextField

/**
 * @author Matteo Cosi
 * @since 17.01.2018
 */
data class SendEvent(val source: JComponent, val message: String, val textField: JTextField)
