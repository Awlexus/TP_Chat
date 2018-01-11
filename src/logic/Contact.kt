package logic

import gui.ChatMessageBlueprint
import logic.storage.IndexableData
import java.awt.Color
import java.net.InetAddress

data class Contact(
        val macaddr: String,
        var ip: InetAddress,
        var username: String,
        val color: Color,
        val messages: ArrayList<ChatMessageBlueprint>
) : IndexableData()