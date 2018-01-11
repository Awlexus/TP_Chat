package tests

import protocol.Protocol
import protocol.ProtocolCallback
import java.net.DatagramPacket
import java.net.InetAddress

/**
 * Created by Awlex on 15.12.2017.
 */
fun main(args: Array<String>) {
    val set = HashMap<InetAddress, String>()
    val prot = Protocol("Matteo", object : ProtocolCallback {
        override fun groupCreated(randId: Int, others: Array<out InetAddress>) {
            TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        }

        override fun getIpsFromGroup(groupId: Int): Array<InetAddress> {
            TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        }

        override fun hello(packet: DatagramPacket, username: String) {
            set[packet.address] = username

            println("$username joined")
        }

        override fun world(packet: DatagramPacket, username: String) {
            set[packet.address] = username

            println("$username joined")
        }

        override fun goodbye(packet: DatagramPacket) {

            println("${set[packet.address]?:"You"} left")

            set.remove(packet.address)

        }

        override fun typing(packet: DatagramPacket, typing: Boolean) {
//            TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        }

        override fun message(packet: DatagramPacket, message: String) {
            println("${set[packet.address]?:"Du"}: $message")
        }

        override fun existsGroupWithId(packet: DatagramPacket, id: Int): Boolean {
            TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        }

        override fun createGroup(packet: DatagramPacket, id: Int, members: Array<InetAddress>) {
            TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        }

        override fun denyGroup(packet: DatagramPacket) {
            TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        }

        override fun groupMessage(packet: DatagramPacket, groupId: Int, message: String) {
            TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        }

    })
    prot.hello()
    do {
        val line = readLine()!!
        if (line == "exit")
            continue
        prot.message(line, prot.broadcastAddress)
    } while (line != "exit")

    prot.stop()
}