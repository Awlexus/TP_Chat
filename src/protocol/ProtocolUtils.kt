package protocol

import java.net.DatagramPacket
import java.net.InetAddress
import java.util.*

fun getMacAddress(packet: DatagramPacket) = getMacAdress(packet.address)

fun getMacAdress(ip: InetAddress): String? {
    Runtime.getRuntime().exec("arp -a")
    Scanner(Runtime.getRuntime().exec("arp -a ${ip.hostAddress}").inputStream).useDelimiter("\\A").use {
        val reg = Regex("([0-9a-fA-F]{2}-?){6}")
        return reg.find(it.next())?.value
    }
}
fun DatagramPacket.getTextData() = String(this.data, this.offset, this.length)

fun DatagramPacket.getMessage() = getTextData().split(Regex("\\s+"), 2)[1]
