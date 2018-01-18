package logic

import logic.storage.SerializableList
import protocol.getMacAddress
import java.awt.Color
import java.net.DatagramPacket
import java.net.InetAddress

class Contacts : SerializableList<Contact>() {

    @Synchronized
    fun getOrCreateContact(macaddress: String, ip: InetAddress, username: String, color: Color): Contact {
        getByMacAddress(macaddress)?.let {
            it.ip = ip
            it.username = username
            return it
        }

        Contact(macaddress, ip, username, color, ArrayList()).let {
            add(it)
            return it
        }

    }

    fun getByMacAddress(macaddress: String?) = find { it.macaddr == macaddress }

    fun getByMacAddress(packet: DatagramPacket) = getByMacAddress(getMacAddress(packet))
}
