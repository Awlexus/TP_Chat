package logic

import logic.storage.DataIOs
import logic.storage.DataRepository
import protocol.getMacAddress
import java.awt.Color
import java.net.DatagramPacket
import java.net.InetAddress
import java.util.concurrent.atomic.AtomicInteger

internal class Contacts(private val ai: AtomicInteger, private val repositoryPath: String) {

    private val contactList = ArrayList<Contact>()
    private var parent: CallbackListener? = null

    fun setParent(parent: CallbackListener) {
        this.parent = parent
    }

    @Synchronized
    fun printContacts() {
        try {
            DataIOs.print("$repositoryPath\\Contacts.ser", contactList)
        } catch (e: DataRepository.DataException) {
            e.printStackTrace()
        }

    }

    @Synchronized
    fun readContacts() {
        try {
            val newContactList = DataIOs.read("$repositoryPath\\Contacts.ser") as ArrayList<Contact>
            contactList.clear()

            newContactList.forEach {
                if (!parent!!.isUsedID(it.id))
                    contactList.add(it)
            }

        } catch (e: DataRepository.DataException) {
            println("Reading failed")
        } catch (e: ClassCastException) {
            println("Reading failed. Given path is no ArrayList<Contact>")
        }

    }

    @Synchronized
    fun getOrCreateContact(macaddress: String, ip: InetAddress, username: String, color: Color): Contact {
        getByMacAddress(macaddress)?.let {
            it.ip = ip
            it.username = username
            return it
        }

        while (parent!!.isUsedID(ai.getAndIncrement())) ;

        val newContact = Contact(macaddress, ai.get(), ip, username, color, ArrayList())
        this.contactList.add(newContact)

        // println(newContact)
        return newContact

    }

    fun getByID(id: Int) = contactList.find { it.id == id }

    fun getByMacAddress(macaddress: String?) = contactList.find { it.macaddr == macaddress }

    fun getByMacAddress(packet: DatagramPacket) = getByMacAddress(getMacAddress(packet))
}
