package protocol

import java.lang.Math.abs
import java.net.*
import java.util.*
import kotlin.concurrent.thread

/**
 * Created by Awlex on 01.12.2017.
 */

fun DatagramPacket.getTextData(): String = String(data).substring(0, data.indexOf(0))

fun DatagramPacket.getMessage(): String {
    return getTextData().split(Regex("\\s+"), 2)[1]
}


class Protocol(val port: Int = 4321, val userName: String = "") {

    private val BUFFERSIZE = 1024

    // Addresses
    private val localhost = InetAddress.getLocalHost()
    private val broadcastAddress = NetworkInterface.getByInetAddress(localhost).interfaceAddresses[0].broadcast

    // This socket is responsible for sending and receiving discovery packages
    private val socket = DatagramSocket(port)

    // This thread waits for hello packages
    private val discoveryThread = thread(start = true, isDaemon = true, name = "Discovery-Thread", block = {

        // This package serves as buffer
        val packet = DatagramPacket(kotlin.ByteArray(BUFFERSIZE), BUFFERSIZE)

        while (!Thread.interrupted()) {

            // Wait until we receive a package
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
                else -> println(packet.getTextData()) // For debugging purposes
            }
        }
        socket.close()
    })

    // Group creation
    private var randId = -1
    private var reply = false
    private var acceptDeny = false

    private fun receiveHello(packet: DatagramPacket) {
        // Set reply text
        packet.data = "$WORLD $userName".toByteArray()

        // Send reply
        socket.send(packet)
    }

    private fun receiveWorld(packet: DatagramPacket) {
        val text = packet.getTextData()
        println("Made a new friend called ${packet.getMessage()} at ${packet.address.hostAddress}")
    }

    private fun receiveGoodbye(packet: DatagramPacket) {
        // Goodbye (。･∀･)ﾉ゛
        println("Goodbye ${packet.address.hostAddress}")
    }

    private fun receiveTyping(packet: DatagramPacket) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    private fun receiveMessage(packet: DatagramPacket) {
        var message = packet.getTextData()
        message = message.substring(message.indexOf(' '))
        println("${packet.address.hostAddress}: $message")
    }

    private fun receiveGroupExistsGroup(packet: DatagramPacket) {
        println("Request for creating a group with ID ${packet.getMessage()} by ${packet.address.hostAddress}")
    }

    private fun receiveGroupCreateGroup(packet: DatagramPacket) {
        println("group with ID ${packet.getMessage().split(" ")[0]} created by ${packet.address.hostAddress}")
        println("Members: ${packet.getMessage().split(Regex("\\s+"), 2)[1]}")
    }

    private fun receiveGroupDenyGroup(packet: DatagramPacket) {
        if (acceptDeny)
            reply = false
    }

    private fun receiveGroupMessage(packet: DatagramPacket) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    /**
     * Broadcasts a "Hello"-package
     */
    fun hello() {
        send("$HELLO $userName", broadcastAddress)
    }

    fun message(message: String, ip: InetAddress) {
        // TODO: add verification
        send("$MESSAGE $message", ip)
    }

    fun createGroup(vararg others: InetAddress = arrayOf(localhost)) {
        val rand = Random()

        do {
            // Reset State
            reply = false
            acceptDeny = false

            // Generate Random number
            randId = abs(rand.nextInt() % Short.MAX_VALUE)

            // Ask if a group this ID is known
            acceptDeny = true
            send("EG $randId", broadcastAddress)

            // Wait for replies
            Thread.sleep(500)
        } while (reply)

        // Generate message
        val text = "CG $randId ${
        others.joinToString(separator = " ", transform = { it.hostAddress }) // Join each IP
        }"

        // Send to all
        others.forEach {
            send(text, it)
        }
    }


    /**
     * Send a text to a IP-Adress
     */
    fun send(text: String, ip: InetAddress = broadcastAddress) {
        socket.send(DatagramPacket(text.toByteArray(), text.length, ip, port))
    }

    /**
     * Stops the auto-discovery protocol and broadcasts a Goodbye
     */
    fun stop() {
        socket.send(DatagramPacket(GOODBYE.toByteArray(), GOODBYE.length, broadcastAddress, port))
        discoveryThread.interrupt()
    }

}