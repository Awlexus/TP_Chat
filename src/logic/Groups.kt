package logic

import logic.storage.DataIOs
import logic.storage.DataRepository
import java.util.*
import java.util.concurrent.atomic.AtomicInteger

class Groups(private val ai: AtomicInteger, private val repositoryPath: String) {

    private val groupList = ArrayList<Group>()
    private var parent: CallbackListener? = null

    fun setParent(parent: CallbackListener) {
        this.parent = parent
    }

    fun printGroups() {
        try {
            DataIOs.print("$repositoryPath\\Groups.ser", groupList)
        } catch (e: DataRepository.DataException) {
            println("Printing failed")
        }

    }

    @Synchronized
    fun readGroups() {
        try {
            val newGroupList = DataIOs.read("$repositoryPath\\Groups.ser") as ArrayList<Group>
            groupList.clear()

            newGroupList.forEach {
                if (!parent!!.isUsedID(it.id))
                    groupList.add(it)
            }
        } catch (e: DataRepository.DataException) {
            println("Reading failed")
        } catch (e: ClassCastException) {
            println("Reading failed. Given path is no ArrayList<Group>")
        }

    }

    @Synchronized internal fun createGroup(protocolID: Int, members: ArrayList<Contact>): Group {

        while (parent!!.isUsedID(ai.getAndIncrement()));
        val id = ai.get()

        val newGroup = Group(protocolID, id, members)
        this.groupList.add(newGroup)
        return newGroup

    }

    internal fun getByID(id: Int) = groupList.find { it.id == id }

    internal fun getByProtocolID(protocolID: Int) = groupList.find { it.protocolID == protocolID }
}
