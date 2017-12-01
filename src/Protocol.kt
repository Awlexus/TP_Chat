import java.net.*
import kotlin.concurrent.thread

/**
 * Created by Awlex on 01.12.2017.
 */

class Protocol(val port: Int = 4321) : AutoCloseable {

    val localhost = InetAddress.getLocalHost()
    val broadcastAddress = NetworkInterface.getByInetAddress(localhost).interfaceAddresses[0].broadcast

    private val readerSocket = ServerSocket(port)
    private val readingThread = thread(start = true, isDaemon = true, name = "Server", block = {
        try {
            while (!Thread.interrupted()) {
                val client = readerSocket.accept()
                println("Client found")
                client.getInputStream().bufferedReader().readLines().forEach { parse(it, client) }
                client.close()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    })

    fun parse(line: String, client: Socket) {
        println(line)
        println(client.inetAddress)
        TODO("Not implemented")
    }

    fun broadcast(text: String) {
        DatagramSocket().use { socket ->
            socket.send(DatagramPacket(text.toByteArray(), text.length, broadcastAddress, port))
            socket.close()
        }
    }

    fun hello(userName: String = "") {
        broadcast("Hello $userName\n")
    }

    override fun close() {
        readerSocket.close()
        readingThread.interrupt()
    }
}