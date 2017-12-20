package logic

import java.awt.Color
import java.net.InetAddress

data class Contact(val id: Int, val ip: InetAddress, val username: String, val color: Color)