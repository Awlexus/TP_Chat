package protocol

import java.net.*
import kotlin.concurrent.thread

/**
 * Created by Awlex on 01.12.2017.
 */

fun DatagramPacket.getTextData(): String = String(data)

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
            }
        }
        socket.close()
    })

    private fun receiveGoodbye(packet: DatagramPacket) {
        // Goodbye (。･∀･)ﾉ゛
        println("Goodbye ${packet.address.hostAddress}")
    }

    private fun receiveWorld(packet: DatagramPacket) {
        val text = packet.getTextData()
        println("Made a new friend called ${text.split(" ")[1]} at ${packet.address.hostAddress}")
    }

    private fun receiveHello(packet: DatagramPacket) {
        // Set reply text
        packet.data = "$WORLD $userName".toByteArray()

        // Send reply
        socket.send(packet)
    }

    private fun receiveMessage(packet: DatagramPacket) {
        var message = packet.getTextData()
        message = message.substring(message.indexOf(' '))
        println("${packet.address.hostAddress}: $message")
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

    fun send(text: String, ip: InetAddress) {
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