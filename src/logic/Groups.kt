package logic

import logic.storage.SerializableList
import java.util.*

class Groups : SerializableList<Group>() {

    private var parent: CallbackListener? = null

    fun setParent(parent: CallbackListener) {
        this.parent = parent
    }

    @Synchronized internal fun createGroup(protocolID: Int, members: ArrayList<Contact>): Group {

        Group(protocolID, members).let {
            add(it)
            return it
        }

    }

    internal fun getByProtocolID(protocolID: Int) = find { it.protocolID == protocolID }
}
