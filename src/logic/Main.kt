package logic

import gui.*
import logic.storage.UserUtil
import protocol.Protocol
import java.awt.Color
import java.io.File
import java.nio.file.Paths
import java.util.*

val username = UserUtil.username

val mainWindow = MainWindow(null)

val repositoryPath = "${Paths.get("").toAbsolutePath()}\\savefiles"

val contacts = Contacts()

val groups = Groups()

val callbackListener = CallbackListener(mainWindow, contacts, groups)

val protocol = Protocol(username, callbackListener)

fun main(args: Array<String>) {

    loadData()

    addListener()

    // Start Communication
    protocol.hello()
}

private fun addListener() {
    mainWindow.addOnContactClickedListener { e ->
        mainWindow.setChatByUserId(e.id)
        callbackListener.currentChatId = e.id
    }

    mainWindow.addMainWindowListener (object: MainWindowListener {
        override fun onExitClicked() {
            protocol.stop()
            contacts.save()
            groups.save()
            System.exit(0)
        }

        override fun onNewGroupCreated(event: NewGroupEvent?) {
            TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        }
    })

    mainWindow.addChatActionListener(object : ChatActionListener {
        override fun onSendPressed(e: SendEvent) {

            if (e.message.isEmpty() || callbackListener.currentChatId == -1)
                return

            val contact = contacts.getById(callbackListener.currentChatId)
                    ?: return

            val chatMessageBlueprint = ChatMessageBlueprint(
                    Chat.chatMessageType.TO, username, e.message, Date().toString(), Color.GREEN)

            mainWindow.addMessage(chatMessageBlueprint, contact.id)
            contact.messages.add(chatMessageBlueprint)

            e.textField.text = ""
            mainWindow.setLastMessageText(e.message, contact.id)
            protocol.sendTyping(false, contact.ip)
            protocol.message(e.message, contact.ip)
        }

        override fun onEditTextChanged(e: TextChangedEvent) {
            val contact = contacts.getById(callbackListener.currentChatId)
            if (contact != null) {
                protocol.sendTyping(!e.text.isEmpty(), contact.ip)
            }
        }
    })
}

private fun loadData() {
    // Create Savefiles, if they do not exist
    File(repositoryPath).mkdir()

    // Load data
    contacts.read()
    groups.read()
}