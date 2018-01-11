package logic

import logic.storage.IndexableData

data class Group(val protocolID: Int, val members: ArrayList<Contact>) : IndexableData()