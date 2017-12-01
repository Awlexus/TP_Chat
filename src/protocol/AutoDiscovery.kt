package protocol

import java.net.DatagramPacket
import java.net.DatagramSocket
import java.net.InetAddress
import java.net.NetworkInterface
import kotlin.concurrent.thread

/**
 * Created by Awlex on 01.12.2017.
 */

class AutoDiscovery(private val port: Int = 4322, val userName: String = "") {

    enum class Commands(val text: String) {
        HELLO("Hello"),
        WORLD("World"),
        GOODBYE("Goodbye");

        override fun toString(): String {
            return text
        }
    }

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
                text.startsWith(Commands.HELLO.text) -> {

                    // Set reply text
                    packet.data = "${Commands.WORLD} $userName".toByteArray()

                    // Send reply
                    socket.send(packet)
                }
                text.startsWith(Commands.WORLD.text) -> println("Made a new friend called ${text.split(" ")[1]}")
                text.startsWith(Commands.GOODBYE.text) -> // Goodbye (。･∀･)ﾉ゛
                    println("Goodbye ${packet.address.hostAddress}")
            }
        }
        socket.close()
    })

    /**
     * Broadcasts a "Hello"-package
     */
    fun hello() {
        val message = "${Commands.HELLO} $userName"
        socket.send(DatagramPacket(message.toByteArray(), message.length, broadcastAddress, port))
    }

    /**
     * Stops the auto-discovery protocol and broadcasts a Goodbye
     */
    fun stop() {
        socket.send(DatagramPacket(Commands.GOODBYE.text.toByteArray(), Commands.GOODBYE.name.length, broadcastAddress, port))
        discoveryThread.interrupt()
    }
}