package protocol

import com.vdurmont.emoji.EmojiParser
import java.lang.Math.abs
import java.net.DatagramPacket
import java.net.DatagramSocket
import java.net.InetAddress
import java.net.NetworkInterface
import java.util.*
import java.util.concurrent.atomic.AtomicBoolean
import java.util.concurrent.atomic.AtomicInteger
import kotlin.concurrent.thread

/**
 * Created by Awlex on 01.12.2017.
 */

fun DatagramPacket.getTextData(): String = String(this.data, this.offset, this.length)


fun DatagramPacket.getMessage(): String = getTextData().split(Regex("\\s+"), 2)[1]


class Protocol(val port: Int = 4321, val userName: String = "", val callback: ProtocolCallback?) {

    private val BUFFERSIZE = 1024

    // Addresses
    private val localhost = InetAddress.getLocalHost()
    val broadcastAddress = NetworkInterface.getByInetAddress(localhost).interfaceAddresses[0].broadcast
        get

    // This socket is responsible for sending and receiving discovery packages
    private val socket = DatagramSocket(port)

    // This thread waits for hello packages
    private val discoveryThread = thread(start = true, isDaemon = true, name = "Discovery-Thread", block = {

        // This package serves as buffer
        val byteArray = ByteArray(BUFFERSIZE)

        while (!Thread.interrupted()) {

            // Wait until we receive a package
            val packet = DatagramPacket(byteArray, BUFFERSIZE)
            socket.receive(packet)

            // Extract Data
            val text = packet.getTextData()

            // Check whether this is a request or an answer

            when {
                text.startsWith(HELLO) -> receiveHello(packet)
                text.startsWith(WORLD) -> receiveWorld(packet)
                text.startsWith(GOODBYE) -> receiveGoodbye(packet)
                text.startsWith(MESSAGE) -> receiveMessage(packet)
                text.startsWith(TYPING) -> receiveTyping(packet)
                text.startsWith(GROUP_EXISTSGROUP) -> receiveGroupExistsGroup(packet)
                text.startsWith(GROUP_CREATEGROUP) -> receiveGroupCreateGroup(packet)
                text.startsWith(GROUP_DENYGROUP) -> receiveGroupDenyGroup(packet)
                text.startsWith(GROUP_MESSAGE) -> receiveGroupMessage(packet)
                else -> println("looool:" + packet.getTextData()) // For debugging purposes
            }
        }
        socket.close()
    })

    // Group creation
    private var randId = AtomicInteger(-1)
    private var reply = AtomicBoolean(false)
    private var acceptDeny = AtomicBoolean(false)

    private fun receiveHello(packet: DatagramPacket) {

        if (packet.address.hostAddress == localhost.hostAddress)
            return

        val username = packet.getMessage()
        // println("Hello from $username at ${packet.address.hostAddress}")

        // Set reply text
        packet.data = "$WORLD $userName".toByteArray()

        // Send reply
        socket.send(packet)

        // TODO: Save Ip and username
        callback?.hello(packet, username)
    }

    private fun receiveWorld(packet: DatagramPacket) {
        val username = packet.getMessage()
        callback?.world(packet, username)
    }

    private fun receiveGoodbye(packet: DatagramPacket) {
        // Goodbye (。･∀･)ﾉ゛
        if (packet.address.hostAddress != localhost.hostAddress)
            callback?.goodbye(packet)
    }

    private fun receiveTyping(packet: DatagramPacket) {
        callback?.typing(packet, packet.getMessage().toBoolean())
    }

    private fun receiveMessage(packet: DatagramPacket) {
        val message = EmojiParser.parseToUnicode(packet.getMessage())
        callback?.message(packet, message)
    }

    private fun receiveGroupExistsGroup(packet: DatagramPacket) {
        val id = packet.getMessage()
        if (callback?.existsGroupWithId(packet, id.toInt()) == true)
            send("$GROUP_DENYGROUP $id")
    }

    private fun receiveGroupCreateGroup(packet: DatagramPacket) {
        val data = packet.getMessage().split(Regex("\\s+"))
        val id = data[0]
        val members = Array<InetAddress>(data.size - 1, { index -> InetAddress.getByName(data[index + 1]) })

        callback?.createGroup(packet, id.toInt(), members)
    }

    private fun receiveGroupDenyGroup(packet: DatagramPacket) {
        if (acceptDeny.get() && packet.getMessage().toInt() == randId.get()) {
            reply.set(false)
            discoveryThread.interrupt()
        }
        callback?.denyGroup(packet)
    }

    private fun receiveGroupMessage(packet: DatagramPacket) {
        val data = packet.getMessage().split(Regex("\\s+"), 2)
        val groupId = data[0]
        val message = data[1]

        callback?.groupMessage(packet, groupId.toInt(), message)
    }

    /**
     * Broadcasts a "Hello"-package
     */
    fun hello() {
        send("$HELLO $userName", broadcastAddress)
    }

    /**
     * Sends a message to the target ip
     */
    fun message(message: String, ip: InetAddress) {
        // TODO: add verification
        send("$MESSAGE $message", ip)
    }

    /**
     * Creates a new group
     *
     * Research this here
     * https://docs.google.com/document/d/1Rlr0l2YYXf594fVcFG7vmmmJhar2uk9tj4fGYFio3Jc/edit?usp=sharing
     */
    fun createGroup(vararg others: InetAddress = arrayOf(localhost)) {
        val rand = Random()

        do {
            // Reset State
            reply.set(false)

            // Generate Random number
            randId.set(abs(rand.nextInt() % Short.MAX_VALUE))

            // Ask if a group this ID is known
            acceptDeny.set(true)
            send("EG $randId", broadcastAddress)

            // Wait for replies
            try {
                Thread.sleep(500)
            } catch (ignored: InterruptedException) {
            }
        } while (reply.get())

        // Generate message
        val text = "CG $randId ${
        others.joinToString(separator = " ", transform = { it.hostAddress }) // Join each IP
        }"

        // Send to all
        others.forEach {
            send(text, it)
        }
        acceptDeny.set(false)
    }

    /**
     * Sends a message to a defined group
     */
    fun sendGroupMessage(message: String, groupId: Int) {
        val text = "$GROUP_MESSAGE $groupId $message"
        for (ip in callback?.getIdsFromGroup(groupId))
            send(text, ip)
    }

    /**
     * Send whether the client is typing
     */
    fun sendTyping(typing: Boolean, ip: InetAddress) {
        send("$TYPING $typing ", ip)
    }

    /**
     * Send a text to a IP-Adress
     */
    fun send(text: String, ip: InetAddress = broadcastAddress) {
        socket.send(DatagramPacket(text.toByteArray(), text.toByteArray().size, ip, port))
    }

    /**
     * Stops the auto-discovery protocol and broadcasts a Goodbye
     */
    fun stop() {
        discoveryThread.interrupt()
        socket.send(DatagramPacket(GOODBYE.toByteArray(), GOODBYE.length, broadcastAddress, port))
        try {
            socket.close()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun addToGroup(ip: InetAddress, groupId: Int) {
        val text = "$ADD_TO_GROUP $groupId ${ip.hostAddress}"
        for (ip in callback?.getIdsFromGroup(groupId))
            send(text, ip)
    }

    constructor(userName: String) : this(4321, userName, null)

    constructor(userName: String, protocolCallback: ProtocolCallback) : this(4321, userName, protocolCallback)
}