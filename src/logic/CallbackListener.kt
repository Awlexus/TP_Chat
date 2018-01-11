package logic

import gui.Chat
import gui.ChatMessageBlueprint
import gui.MainWindow
import protocol.ProtocolCallback
import protocol.getMacAddress
import java.net.DatagramPacket
import java.net.InetAddress
import java.util.*

class CallbackListener internal constructor(private val mainWindow: MainWindow,
                                            private val contacts: Contacts,
                                            private val groups: Groups) : ProtocolCallback {
    var currentChatId = -1

    init {
        contacts.setParent(this)
        groups.setParent(this)
    }

    override fun hello(packet: DatagramPacket, username: String) {
        val (_, id, _, contactName, color, messages) =
                contacts.getOrCreateContact(getMacAddress(packet)!!,
                        packet.address, username, UserColors.randomColor)
        // debug
        mainWindow.addContact(contactName, "is now online", color, id)
        mainWindow.addNewChatById(id)

        if (!messages.isEmpty())
            mainWindow.addMessages(messages.toTypedArray(), id)
    }

    override fun world(packet: DatagramPacket, username: String) {
        val (_, id, _, contactName, color, messages) = contacts.getOrCreateContact(getMacAddress(packet)!!,
                packet.address, username, UserColors.randomColor)

        // debug
        mainWindow.addNewChatById(id)
        mainWindow.addContact(contactName, "is now online", color, id)

        if (!messages.isEmpty())
            mainWindow.addMessages(messages.toTypedArray(), id)
    }

    override fun goodbye(packet: DatagramPacket) {
        val contact = contacts.getByMacAddress(packet) ?: return

        mainWindow.addMessage(ChatMessageBlueprint(Chat.chatMessageType.INFO, "ignored parameter",
                "${contact.username} has left the room.", "",
                contact.color), contact.id)
        mainWindow.setLastMessageText("is now offline", contact.id)
    }

    override fun typing(packet: DatagramPacket, typing: Boolean) {
        contacts.getByMacAddress(packet)?.id!!
                .let { id ->
                    if (typing) {
                        mainWindow.setContactWriting(id)
                    } else {
                        mainWindow.removeContactWriting(id)
                    }
                }
    }

    override fun message(packet: DatagramPacket, message: String) {
        val contact = contacts.getByMacAddress(packet)
        if (contact == null) {
            println("Message from unknown contact received: " + packet.address)
        } else {
            val chatMessageBlueprint = ChatMessageBlueprint(Chat.chatMessageType.FROM,
                    contact.username,
                    message, "", contact.color)

            mainWindow.addMessage(chatMessageBlueprint, contact.id)
            mainWindow.setLastMessageText(message, contact.id)

            contact.messages.add(chatMessageBlueprint)
        }
    }

    override fun existsGroupWithId(packet: DatagramPacket, id: Int): Boolean {
        // TODO: 19.12.2017 most likely should also return false, when a contact already uses this id
        // UPDATE: Nein!
        return groups.getByProtocolID(id) != null
    }

    override fun createGroup(packet: DatagramPacket, id: Int, members: Array<InetAddress>) {
        val membersAsContacts = ArrayList<Contact>()
        for (memberAddress in members) {
            val contact = contacts.getByMacAddress(getMacAddress(memberAddress)) ?: throw RuntimeException()

            membersAsContacts.add(contact)
        }
        groups.createGroup(id, membersAsContacts)
    }

    override fun denyGroup(packet: DatagramPacket) {
        // TODO: 15.12.2017 No clue what this means, or which steps should be taken.
    }

    override fun groupMessage(packet: DatagramPacket, groupId: Int, message: String) {
        // TODO: 19.12.2017 see todo
        val correspondingChatID = groups.getByProtocolID(groupId)!!.id
        mainWindow.addMessage(ChatMessageBlueprint(Chat.chatMessageType.FROM,
                contacts.getByMacAddress(packet)!!.username, message,
                "", contacts.getByMacAddress(packet)!!.color), correspondingChatID)
    }

    fun isUsedID(id: Int) = contacts.getByID(id) != null

    override fun getIpsFromGroup(groupId: Int)
            = groups.getByID(groupId).members.map { it.ip }.toTypedArray()

    override fun groupCreated(randId: Int, others: Array<out InetAddress>) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}
