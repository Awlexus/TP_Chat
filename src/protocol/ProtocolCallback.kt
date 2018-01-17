package protocol

import java.net.DatagramPacket
import java.net.InetAddress

/**
 * Created by Awlex on 14.12.2017.
 */
interface ProtocolCallback {

    /**
     * Called when a Hello-packet was received
     *
     * @param packet Contains information about the sender
     * @param username Name of the person that send the packet
     */
    fun hello(packet: DatagramPacket, username: String)

    /**
     * Called when a World-packet was received
     *
     * @param packet Contains information about the sender
     * @param username Name of the person that send the packet
     */
    fun world(packet: DatagramPacket, username: String)

    /**
     * Called when a Goodbye-packet was received
     *
     * @param packet Contains information about the sender
     */
    fun goodbye(packet: DatagramPacket)

    /**
     * Called when a Typing-packet was received
     *
     * @param packet Contains information about the sender
     * @param typing Whether the person is typing or not
     */
    fun typing(packet: DatagramPacket, typing: Boolean)

    /**
     * Called when a Message-packet was received
     *
     * @param packet Contains information about the sender
     * @param message Contains the message
     */
    fun message(packet: DatagramPacket, message: String)

    /**
     * Called when a ExistsGroup-packet was received
     *
     * The callback has to return whether it's aware of a group with the given contactId
     *
     * @param packet Contains information about the sender
     * @param id The contactId whose existence has to be proven
     */
    fun existsGroupWithId(packet: DatagramPacket, id: Int): Boolean

    /**
     * Called when a CreateGroup-packet was received
     *
     * A group has been created and we are (probably) part of it.
     *
     * @param packet Contains information about the sender
     * @param id Group contactId
     * @param members The other persons in this this groups
     */
    fun createGroup(packet: DatagramPacket, id: Int, members: Array<InetAddress>)

    /**
     * Called when a DenyGroup-packet was received and our request to create a group has been denied ｡ﾟ(ﾟ´Д`ﾟ)ﾟ｡
     *
     * @param packet Contains information about the sender
     */
    fun denyGroup(packet: DatagramPacket)

    /**
     * Called when a GroupMessage-packet was received
     *
     * @param packet Contains information about the sender
     * @param groupId To which group this message goes
     * @param message The content of the message
     */
    fun groupMessage(packet: DatagramPacket, groupId: Int, message: String)

    fun getIpsFromGroup(groupId: Int): Array<InetAddress>
    fun groupCreated(randId: Int, others: Array<out InetAddress>)

}