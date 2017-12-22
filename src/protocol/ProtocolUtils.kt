package protocol

import java.net.DatagramPacket
import java.net.InetAddress
import java.net.NetworkInterface

fun getMacAddress(packet: DatagramPacket) = getMacAddress(packet.address)

fun getMacAddress(ip: InetAddress): String {
    val iface = NetworkInterface.getByInetAddress(ip)
    val mac = iface.hardwareAddress
    return String(mac)
}

fun DatagramPacket.getTextData() = String(this.data, this.offset, this.length)

fun DatagramPacket.getMessage() = getTextData().split(Regex("\\s+"), 2)[1]
