package gui


import java.awt.Color
import java.io.Serializable

/**
 * @author Matteo Cosi
 * @since 02.12.2017
 */
data class ChatMessageBlueprint(var type: Chat.chatMessageType, var name: String, var message: String, var date: String, var color: Color) : Serializable
