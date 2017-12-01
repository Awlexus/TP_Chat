package protocol

import java.net.*
import kotlin.concurrent.thread

/**
 * Created by Awlex on 01.12.2017.
 */

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
            val text = String(packet.data)

            // Check whether this is a request or an answer
            when {
                text.startsWith(HELLO) -> receiveHello(packet)
                text.startsWith(WORLD) -> receiveWorld(text)
                text.startsWith(GOODBYE) -> receiveGoodbye(packet)
            }
        }
        socket.close()
    })

    private fun receiveGoodbye(packet: DatagramPacket) {
        // Goodbye (。･∀･)ﾉ゛
        println("Goodbye ${packet.address.hostAddress}")
    }

    private fun receiveWorld(text: String) {
        println("Made a new friend called ${text.split(" ")[1]}")
    }

    private fun receiveHello(packet: DatagramPacket) {
        // Set reply text
        packet.data = "$WORLD $userName".toByteArray()

        // Send reply
        socket.send(packet)
    }

    /**
     * Broadcasts a "Hello"-package
     */
    fun hello() {
        val message = "$HELLO $userName"
        socket.send(DatagramPacket(message.toByteArray(), message.length, broadcastAddress, port))
    }

    /**
     * Stops the auto-discovery protocol and broadcasts a Goodbye
     */
    fun stop() {
        socket.send(DatagramPacket(GOODBYE.toByteArray(), GOODBYE.length, broadcastAddress, port))
        discoveryThread.interrupt()
    }

}