package gui

/**
 * @author Matteo Cosi
 * @since 01.12.2017
 */
class Message(message: String) {
    var text: String
        internal set

    init {
        this.text = message
    }
}
