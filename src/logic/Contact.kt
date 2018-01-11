package logic

import gui.ChatMessageBlueprint
import java.awt.Color
import java.io.Serializable
import java.net.InetAddress

data class Contact(
        val macaddr: String, val id: Int, var ip: InetAddress, var username: String, val color: Color,
        val messages: ArrayList<ChatMessageBlueprint>
) : Serializable