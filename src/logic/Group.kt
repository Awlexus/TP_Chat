package logic

import java.io.Serializable

data class Group(val protocolID: Int, val id: Int, val members: ArrayList<Contact>) : Serializable